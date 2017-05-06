package fr.minuskube.netherboard.sponge;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.api.PlayerBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.critieria.Criteria;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlots;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;

import java.util.HashMap;
import java.util.Map;

public class SPlayerBoard implements PlayerBoard<Text, Integer, Text> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SPlayerBoard.class);

    private Player player;
    private Scoreboard scoreboard;

    private Text name;

    private Objective objective;
    private Objective buffer;

    private Map<Integer, Text> lines = new HashMap<>();

    private boolean deleted = false;

    public SPlayerBoard(Player player, Text name) {
        this(player, null, name);
    }

    public SPlayerBoard(Player player, Scoreboard scoreboard, Text name) {
        this.player = player;
        this.scoreboard = scoreboard;

        if(this.scoreboard == null) {
            Scoreboard sb = player.getScoreboard();

            if(sb == null || sb == Sponge.getServer().getServerScoreboard().orElse(null))
                sb = Scoreboard.builder().build();

            this.scoreboard = sb;
        }

        this.name = name;

        String subName = player.getName().length() <= 14
                ? player.getName()
                : player.getName().substring(0, 16);

        this.objective = this.scoreboard.getObjective("sb" + subName)
                .orElseGet(() -> {

            Objective obj = Objective.builder()
                    .name("sb" + subName)
                    .displayName(name)
                    .criterion(Criteria.DUMMY)
                    .build();

            this.scoreboard.addObjective(obj);
            return obj;
        });

        this.buffer = this.scoreboard.getObjective("bf" + subName)
                .orElseGet(() -> {

            Objective obj = Objective.builder()
                    .name("bf" + subName)
                    .displayName(name)
                    .criterion(Criteria.DUMMY)
                    .build();

            this.scoreboard.addObjective(obj);
            return obj;
        });

        this.scoreboard.updateDisplaySlot(this.objective, DisplaySlots.SIDEBAR);
        this.player.setScoreboard(this.scoreboard);
    }

    @Override
    public Text get(Integer score) {
        if(this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");

        return this.lines.get(score);
    }

    @Override
    public void set(Text name, Integer score) {
        if(this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");

        Text oldName = this.lines.get(score);

        if(name.equals(oldName))
            return;

        if(oldName != null) {
            this.buffer.removeScore(oldName);
            this.buffer.getOrCreateScore(name).setScore(score);

            swapBuffers();

            this.buffer.removeScore(oldName);
            this.buffer.getOrCreateScore(name).setScore(score);
        }
        else {
            this.objective.getOrCreateScore(name).setScore(score);
            this.buffer.getOrCreateScore(name).setScore(score);
        }

        this.lines.put(score, name);
    }

    private void swapBuffers() {
        this.scoreboard.updateDisplaySlot(this.buffer, DisplaySlots.SIDEBAR);

        Objective temp = this.buffer;

        this.buffer = this.objective;
        this.objective = temp;
    }

    @Override
    public void remove(Integer score) {
        if(this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");

        Text name = this.lines.get(score);

        if(name == null)
            return;

        this.scoreboard.removeScores(name);
        this.lines.remove(score);
    }

    @Override
    public void delete() {
        if(this.deleted)
            return;

        Netherboard.instance().deleteBoard(player);

        this.scoreboard.removeObjective(this.objective);
        this.objective = null;

        this.scoreboard.removeObjective(this.buffer);
        this.buffer = null;

        this.lines = null;

        this.deleted = true;
    }

    @Override
    public Map<Integer, Text> getLines() {
        if(this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");

        return new HashMap<>(lines);
    }

    @Override
    public Text getName() {
        return name;
    }

    @Override
    public void setName(Text name) {
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
