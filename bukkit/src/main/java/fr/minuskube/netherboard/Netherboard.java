package fr.minuskube.netherboard;

import fr.minuskube.netherboard.api.PlayerBoard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;

public class Netherboard {

    private static Netherboard instance;

    private Map<Player, PlayerBoard> boards = new HashMap<>();

    private Netherboard() {}

    public PlayerBoard createBoard(Player player, String name) {
        return createBoard(player, null, name);
    }

    public PlayerBoard createBoard(Player player, Scoreboard scoreboard, String name) {
        if(boards.containsKey(player))
            boards.get(player).delete();

        PlayerBoard board = new BPlayerBoard(player, scoreboard, name);

        boards.put(player, board);
        return board;
    }

    public void deleteBoard(Player player) {
        boards.remove(player);
    }

    public PlayerBoard getBoard(Player player) {
        return boards.get(player);
    }

    public static Netherboard instance() {
        if(instance == null)
            instance = new Netherboard();

        return instance;
    }

}
