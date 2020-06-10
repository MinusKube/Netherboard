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

    private final Map<Player, BPlayerBoard> boards = new HashMap<>();

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
        deleteBoard(player);

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
        if(boards.containsKey(player))
            boards.get(player).delete();
    }

    /**
     * Removes the board of a player from the boards map.<br>
     * <b>WARNING: Do not use this to delete the board of a player!</b>
     *
     * @param player the player
     */
    public void removeBoard(Player player) {
        boards.remove(player);
    }

    /**
     * Checks if the player has a board.
     *
     * @param player the player
     * @return       <code>true</code> if the player has a board, otherwise <code>false</code>
     */
    public boolean hasBoard(Player player) {
        return boards.containsKey(player);
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
     * Gets all the boards mapped to their player.
     * This returns a copy of the current boards map,
     * thus modifying the given Map will not have any effect on the boards.
     *
     * @return a new map with all the player boards
     */
    public Map<Player, BPlayerBoard> getBoards() {
        return new HashMap<>(boards);
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
