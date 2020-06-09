package fr.minuskube.netherboard.plugin;

import fr.minuskube.netherboard.BoardManager;
import fr.minuskube.netherboard.internal.SpongeBoardManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "netherboard",
        name = "Netherboard",
        version = "2.0.0",
        authors = "MinusKube"
)
public class NetherboardSpongePlugin {

    @Listener
    public void onGamePreInitialize(GamePreInitializationEvent event) {
        Sponge.getServiceManager().setProvider(
                this,
                BoardManager.class,
                new SpongeBoardManager()
        );
    }

}
