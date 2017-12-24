package fr.minuskube.netherboard.api;

import java.util.Optional;

/**
 * Represents a provider which can be used to
 * create player boards.
 *
 * <p>
 *  The player can be of any of these types:
 *
 *  <ul>
 *      <li>Player</li>
 *      <li>UUID</li>
 *      <li>String (the player's name)</li>
 *      <li>or other, depending of the implementation</li>
 *  </ul>
 * </p>
 */
public interface BoardManager {

    /**
     * Creates a board and assign it to the given player.
     *
     * @param player the player, see {@link BoardManager} for more infos
     * @return the newly created board
     */
    PlayerBoard newBoard(Object player);

    /**
     * Gets the player board, if the player has one.
     *
     * @param player the player, see {@link BoardManager} for more infos
     * @return an <code>Optional</code> containing the player's board,
     *         or <code>Optional.empty()</code> if the player doesn't have any
     */
    Optional<PlayerBoard> getBoard(Object player);

    /**
     * Checks if the player has a board.<br>
     * Similar to <code>getBoard(player).isPresent()</code>.
     *
     * @param player the player, see {@link BoardManager} for more infos
     * @return <code>true</code> if the player has a board
     */
    boolean hasBoard(Object player);

    /**
     * Deletes the player board.
     *
     * @param player the player, see {@link BoardManager} for more infos
     * @return <code>true</code> if the board has been deleted successfully,
     *         or <code>false</code> if the player doesn't have any board
     */
    boolean deleteBoard(Object player);

}
