package fr.minuskube.netherboard.api.impl;

import com.google.common.base.Preconditions;
import fr.minuskube.netherboard.api.PlayerBoard;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.UUID;

public class SpongeBoardManager extends AbstractBoardManager {

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
            Player spongePlayer = Sponge.getServer().getPlayer((String) player)
                    .orElseThrow(() -> new IllegalArgumentException("The given name doesn't match any online player."));

            return spongePlayer.getUniqueId();
        }

        throw new IllegalArgumentException("The given player type is not supported. " +
                "It must be one of these types: UUID, Player, String (player's name)");
    }

}
