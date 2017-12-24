package fr.minuskube.netherboard.api.impl;

import com.google.common.base.Preconditions;
import fr.minuskube.netherboard.api.PlayerBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitBoardManager extends AbstractBoardManager {

    @Override
    protected PlayerBoard createBoard(UUID uuid) {
        return null;
    }

    @Override
    protected UUID getUniqueId(Object player) {
        Preconditions.checkNotNull(player, "The given player must not be null.");

        if(player instanceof UUID)
            return (UUID) player;
        if(player instanceof Player)
            return ((Player) player).getUniqueId();
        if(player instanceof String) {
            Player bukkitPlayer = Bukkit.getPlayer((String) player);

            if(bukkitPlayer == null)
                throw new IllegalArgumentException("The given name doesn't match any online player.");

            return bukkitPlayer.getUniqueId();
        }

        throw new IllegalArgumentException("The given player type is not supported. " +
                "It must be one of these types: UUID, Player, String (player's name)");
    }

}
