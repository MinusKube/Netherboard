package fr.minuskube.netherboard.bukkit;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.api.PlayerBoard;
import fr.minuskube.netherboard.bukkit.util.NMS;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class BPlayerBoard implements PlayerBoard {

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
                : player.getName().substring(0, 16);

        this.objective = this.scoreboard.getObjective("sb" + subName);
        this.buffer = this.scoreboard.getObjective("bf" + subName);

        if(this.objective == null)
            this.objective = this.scoreboard.registerNewObjective("sb" + subName, "dummy");
        if(this.buffer == null)
            this.buffer = this.scoreboard.registerNewObjective("bf" + subName, "dummy");

        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(name);

        this.buffer.setDisplayName(ChatColor.RED + name);

        this.player.setScoreboard(this.scoreboard);
    }

    @Override
    public String get(int score) {
        if(this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");

        return this.lines.get(score);
    }

    @Override
    public void set(String name, int score) {
        if(this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");

        String oldName = this.lines.get(score);

        if(oldName != null) {
            sendRemove(this.buffer, oldName, score);
            this.buffer.getScore(name).setScore(score);

            swapBuffers();

            sendRemove(this.buffer, oldName, score);
            this.buffer.getScore(name).setScore(score);
        }
        else {
            this.objective.getScore(name).setScore(score);
            this.buffer.getScore(name).setScore(score);
        }

        this.lines.put(score, name);
    }

    private void swapBuffers() {
        this.buffer.setDisplaySlot(DisplaySlot.SIDEBAR);

        Objective temp = this.buffer;

        this.buffer = this.objective;
        this.objective = temp;
    }

    private void sendRemove(Objective obj, String name, int score) {
        try {
            Object sbHandle = NMS.getHandle(scoreboard);
            Object objHandle = NMS.getHandle(obj);

            Map scores = (Map) NMS.PLAYER_SCORES.get(sbHandle);
            ((Map) scores.get(name)).remove(objHandle);

            switch(NMS.getVersion().getMajor()) {
                case "1.7": {
                    Object sbScore = NMS.SB_SCORE.newInstance(
                            sbHandle,
                            objHandle,
                            name
                    );

                    NMS.SB_SCORE_SET.invoke(sbScore, score);

                    Object packet = NMS.PACKET_SCORE.newInstance(sbScore, 1);
                    NMS.sendPacket(packet, player);
                    break;
                }

                default: {
                    Object packet = NMS.PACKET_SCORE.newInstance(
                            name,
                            objHandle
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
    public void remove(int score) {
        if(this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");

        String name = this.lines.get(score);

        this.scoreboard.resetScores(name);
        this.lines.remove(score);
    }

    @Override
    public void delete() {
        if(this.deleted)
            return;

        Netherboard.instance().deleteBoard(player);

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
    }

    public Player getPlayer() {
        return player;
    }

    public Scoreboard getScoreboard() { return scoreboard; }

}
