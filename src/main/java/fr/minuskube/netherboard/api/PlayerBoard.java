package fr.minuskube.netherboard.api;

import java.util.Map;

/**
 * Represents a Player's Scorebard
 *
 * @param <V> The type of a line <b>V</b>alue
 * @param <S> The type of a line <b>S</b>core
 * @param <N> The type of the board <b>N</b>ame
 */
public interface PlayerBoard<V, S, N> {

    /**
     * Gets the value of the line from its score.
     *
     * @param score the score of the line
     * @return      the value of the line, or null if the line doesn't exist.
     */
    V get(N score);

    /**
     * Sets a line with its name and its score.
     * This will update the line if it already exists, and create it if it doesn't.
     *
     * @param name  the name of the line
     * @param score the score of the line
     */
    void set(V name, N score);

    /**
     * Removes a line from its score.
     *
     * @param score the score of the line to remove
     */
    void remove(N score);

    /**
     * Totally deletes the board, after this, you can't use this instance again,
     * you'll need to create another PlayerBoard if you want to create the scoreboard again.
     */
    void delete();

    /**
     * Gets the name of the board.
     *
     * @return the name of the board
     */
    S getName();

    /**
     * Sets the name of the board.
     *
     * @param name the new name of the board
     */
    void setName(S name);

    /**
     * Gets the current lines of the board.
     *
     * @return the lines of the board
     */
    Map<N, V> getLines();

}
