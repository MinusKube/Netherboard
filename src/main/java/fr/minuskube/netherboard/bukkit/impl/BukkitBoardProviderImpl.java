package fr.minuskube.netherboard.bukkit.impl;

import fr.minuskube.netherboard.api.PlayerBoard;
import fr.minuskube.netherboard.api.impl.AbstractBoardProvider;
import fr.minuskube.netherboard.bukkit.BukkitBoardProvider;
import org.bukkit.Bukkit;

import java.util.UUID;

public class BukkitBoardProviderImpl extends AbstractBoardProvider<String, Integer, String>
        implements BukkitBoardProvider {

    @Override
    protected PlayerBoard<String, Integer, String> createBoard(UUID uuid) {
        return new BukkitPlayerBoardImpl(Bukkit.getPlayer(uuid));
    }

}
