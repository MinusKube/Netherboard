package fr.minuskube.netherboard.bukkit.impl;

import fr.minuskube.netherboard.bukkit.BukkitPlayerBoard;
import org.bukkit.entity.Player;

import java.util.Map;

public class BukkitPlayerBoardImpl implements BukkitPlayerBoard {

    private Player player;

    public BukkitPlayerBoardImpl(Player player) {
        this.player = player;
    }

    @Override
    public String get(String score) {
        return null;
    }

    @Override
    public void set(String name, String score) {

    }

    @Override
    public void remove(String score) {

    }

    @Override
    public void delete() {

    }

    @Override
    public Integer getName() {
        return null;
    }

    @Override
    public void setName(Integer name) {

    }

    @Override
    public Map<String, String> getLines() {
        return null;
    }

    public Player getPlayer() { return player; }
}
