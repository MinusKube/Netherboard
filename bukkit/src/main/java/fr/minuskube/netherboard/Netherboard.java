package fr.minuskube.netherboard;

import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;

/**
 * The main class of the Netherboard API for Bukkit,
 * you'll need to use it if you want to create boards.
 *
 * To create a board, get the instance using <code>Netherboard.instance()</code>
 * and call one of the <code>createBoard()</code> methods.
 */
public class Netherboard {

    private static Netherboard instance;

    private Map<Player, BPlayerBoard> boards = new HashMap<>();

    private Netherboard() {}

    /**
     * Creates a board to a player.
     *
     * @param player    the player
     * @param name      the name of the board
     * @return          the newly created board
     */
    public BPlayerBoard createBoard(Player player, String name) {
        return createBoard(player, null, name);
    }

    /**
     * Creates a board to a player, using a predefined scoreboard.
     *
     * @param player        the player
     * @param scoreboard    the scoreboard to use
     * @param name          the name of the board
     * @return              the newly created board
     */
    public BPlayerBoard createBoard(Player player, Scoreboard scoreboard, String name) {
        if(boards.containsKey(player))
            boards.get(player).delete();

        BPlayerBoard board = new BPlayerBoard(player, scoreboard, name);

        boards.put(player, board);
        return board;
    }

    /**
     * Deletes the board of a player.
     *
     * @param player the player
     */
    public void deleteBoard(Player player) {
        if(boards.get(player) != null)
            boards.get(player).delete();

        boards.remove(player);
    }

    /**
     * Gets the board of a player.
     *
     * @param player    the player
     * @return          the player board, or null if the player has no board
     */
    public BPlayerBoard getBoard(Player player) {
        return boards.get(player);
    }

    /**
     * Returns the instance of the Netherboard class.
     *
     * @return the instance
     */
    public static Netherboard instance() {
        if(instance == null)
            instance = new Netherboard();

        return instance;
    }

}
