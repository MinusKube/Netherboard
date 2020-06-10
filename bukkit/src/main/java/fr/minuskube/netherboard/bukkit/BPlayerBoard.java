package fr.minuskube.netherboard.bukkit;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.api.PlayerBoard;
import fr.minuskube.netherboard.bukkit.util.NMS;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BPlayerBoard implements PlayerBoard<String, Integer, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BPlayerBoard.class);

    private Player player;
    private Scoreboard scoreboard;

    private String name;

    private Objective objective;
    private Objective buffer;

    private Map<Integer, String> lines = new HashMap<>();

    private boolean deleted = false;

    public BPlayerBoard(Player player, String name) {
        this(player, null, name);
    }

    public BPlayerBoard(Player player, Scoreboard scoreboard, String name) {
        this.player = player;
        this.scoreboard = scoreboard;

        if(this.scoreboard == null) {
            Scoreboard sb = player.getScoreboard();

            if(sb == null || sb == Bukkit.getScoreboardManager().getMainScoreboard())
                sb = Bukkit.getScoreboardManager().getNewScoreboard();

            this.scoreboard = sb;
        }

        this.name = name;

        String subName = player.getName().length() <= 14
                ? player.getName()
                : player.getName().substring(0, 14);

        this.objective = this.scoreboard.getObjective("sb" + subName);
        this.buffer = this.scoreboard.getObjective("bf" + subName);

        if(this.objective == null)
            this.objective = this.scoreboard.registerNewObjective("sb" + subName, "dummy");
        if(this.buffer == null)
            this.buffer = this.scoreboard.registerNewObjective("bf" + subName, "dummy");

        this.objective.setDisplayName(name);
        sendObjective(this.objective, ObjectiveMode.CREATE);
        sendObjectiveDisplay(this.objective);

        this.buffer.setDisplayName(name);
        sendObjective(this.buffer, ObjectiveMode.CREATE);

        this.player.setScoreboard(this.scoreboard);
    }

    @Override
    public String get(Integer score) {
        if(this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");

        return this.lines.get(score);
    }

    @Override
    public void set(String name, Integer score) {
        if(this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");

        String oldName = this.lines.get(score);

        if(name.equals(oldName))
            return;

        this.lines.entrySet()
                .removeIf(entry -> entry.getValue().equals(name));

        if(oldName != null) {
            if(NMS.getVersion().getMajor().equals("1.7")) {
                sendScore(this.objective, oldName, score, true);
                sendScore(this.objective, name, score, false);
            }
            else {
                sendScore(this.buffer, oldName, score, true);
                sendScore(this.buffer, name, score, false);

                swapBuffers();

                sendScore(this.buffer, oldName, score, true);
                sendScore(this.buffer, name, score, false);
            }
        }
        else {
            sendScore(this.objective, name, score, false);
            sendScore(this.buffer, name, score, false);
        }

        this.lines.put(score, name);
    }

    @Override
    public void setAll(String... lines) {
        if(this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");

        for(int i = 0; i < lines.length; i++) {
            String line = lines[i];

            set(line, lines.length - i);
        }

        Set<Integer> scores = new HashSet<>(this.lines.keySet());
        for (int score : scores) {
            if (score <= 0 || score > lines.length) {
                remove(score);
            }
        }
    }

    @Override
    public void clear() {
        new HashSet<>(this.lines.keySet()).forEach(this::remove);
        this.lines.clear();
    }

    private void swapBuffers() {
        sendObjectiveDisplay(this.buffer);

        Objective temp = this.buffer;

        this.buffer = this.objective;
        this.objective = temp;
    }

    private void sendObjective(Objective obj, ObjectiveMode mode) {
        try {
            Object objHandle = NMS.getHandle(obj);

            Object packetObj = NMS.PACKET_OBJ.newInstance(
                    objHandle,
                    mode.ordinal()
            );

            NMS.sendPacket(packetObj, player);
        } catch(InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException e) {

            LOGGER.error("Error while creating and sending objective packet. (Unsupported Minecraft version?)", e);
        }
    }

    private void sendObjectiveDisplay(Objective obj) {
        try {
            Object objHandle = NMS.getHandle(obj);

            Object packet = NMS.PACKET_DISPLAY.newInstance(
                    1,
                    objHandle
            );

            NMS.sendPacket(packet, player);
        } catch(InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException e) {

            LOGGER.error("Error while creating and sending display packet. (Unsupported Minecraft version?)", e);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void sendScore(Objective obj, String name, int score, boolean remove) {
        try {
            Object sbHandle = NMS.getHandle(scoreboard);
            Object objHandle = NMS.getHandle(obj);

            Object sbScore = NMS.SB_SCORE.newInstance(
                    sbHandle,
                    objHandle,
                    name
            );

            NMS.SB_SCORE_SET.invoke(sbScore, score);

            Map scores = (Map) NMS.PLAYER_SCORES.get(sbHandle);

            if(remove) {
                if(scores.containsKey(name))
                    ((Map) scores.get(name)).remove(objHandle);
            }
            else {
                if(!scores.containsKey(name))
                    scores.put(name, new HashMap());

                ((Map) scores.get(name)).put(objHandle, sbScore);
            }

            switch(NMS.getVersion().getMajor()) {
                case "1.7": {
                    Object packet = NMS.PACKET_SCORE.newInstance(
                            sbScore,
                            remove ? 1 : 0
                    );

                    NMS.sendPacket(packet, player);
                    break;
                }

                case "1.8":
                case "1.9":
                case "1.10":
                case "1.11":
                case "1.12": {
                    Object packet;

                    if(remove) {
                        packet = NMS.PACKET_SCORE_REMOVE.newInstance(
                                name,
                                objHandle
                        );
                    }
                    else {
                        packet = NMS.PACKET_SCORE.newInstance(
                                sbScore
                        );
                    }

                    NMS.sendPacket(packet, player);
                    break;
                }

                default: {
                    Object packet = NMS.PACKET_SCORE.newInstance(
                            remove ? NMS.ENUM_SCORE_ACTION_REMOVE : NMS.ENUM_SCORE_ACTION_CHANGE,
                            obj.getName(),
                            name,
                            score
                    );

                    NMS.sendPacket(packet, player);
                    break;
                }
            }
        } catch(InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException e) {

            LOGGER.error("Error while creating and sending remove packet. (Unsupported Minecraft version?)", e);
        }
    }

    @Override
    public void remove(Integer score) {
        if(this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");

        String name = this.lines.get(score);

        if(name == null)
            return;

        this.scoreboard.resetScores(name);
        this.lines.remove(score);
    }

    @Override
    public void delete() {
        if(this.deleted)
            return;

        Netherboard.instance().removeBoard(player);

        sendObjective(this.objective, ObjectiveMode.REMOVE);
        sendObjective(this.buffer, ObjectiveMode.REMOVE);

        this.objective.unregister();
        this.objective = null;

        this.buffer.unregister();
        this.buffer = null;

        this.lines = null;

        this.deleted = true;
    }

    @Override
    public Map<Integer, String> getLines() {
        if(this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");

        return new HashMap<>(lines);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        if(this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");

        this.name = name;

        this.objective.setDisplayName(name);
        this.buffer.setDisplayName(name);

        sendObjective(this.objective, ObjectiveMode.UPDATE);
        sendObjective(this.buffer, ObjectiveMode.UPDATE);
    }

    public Player getPlayer() {
        return player;
    }

    public Scoreboard getScoreboard() { return scoreboard; }


    private enum ObjectiveMode { CREATE, REMOVE, UPDATE }

}
