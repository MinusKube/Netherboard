package fr.minuskube.netherboard.plugin;

import fr.minuskube.netherboard.BoardManager;
import fr.minuskube.netherboard.internal.BukkitBoardManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class NetherboardBukkitPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getServicesManager().register(
                BoardManager.class,
                new BukkitBoardManager(),
                this,
                ServicePriority.Normal
        );

        Bukkit.getPluginManager().registerEvents(new Listener() {

            @EventHandler
            public void onPlayerJoin(PlayerJoinEvent event) {
                Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

                Team team = scoreboard.registerNewTeam("teamName");
                team.setDisplayName("DisplayName");
                team.setPrefix("A");
                team.setSuffix("A");
                team.addEntry("ABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJ");

                Objective obj = scoreboard.registerNewObjective("test", "dummy");
                obj.getScore("teamName").setScore(0);
                obj.getScore("DisplayName").setScore(1);
                obj.getScore("ABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJ").setScore(2);
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);

                Player player = event.getPlayer();
                player.setScoreboard(scoreboard);
            }

            @EventHandler
            public void onPlayerInteract(PlayerInteractEvent event) {
                if (event.getItem() != null && event.getItem().getType() == Material.CARROT_ITEM) {
                    Team team = event.getPlayer().getScoreboard().getTeam("teamName");
                    team.setPrefix(team.getPrefix() + "A");
                    team.setSuffix(team.getPrefix());
                    Bukkit.broadcastMessage(team.getPrefix().length() + "");
                }
            }

        }, this);
    }

}
