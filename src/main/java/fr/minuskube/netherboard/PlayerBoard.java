package fr.minuskube.netherboard;

import fr.minuskube.netherboard.line.BoardLine;

import java.util.Map;

/**
 * Represents a Player's Scorebard.
 */
public interface PlayerBoard {

    /**
     * Gets the line at a given score.
     *
     * @param score the score of the line
     * @return      the line, or null if the line doesn't exist.
     */
    BoardLine get(int score);

    /**
     * Sets a line with its name and its score.
     * This will update the line if it already exists, and create it if it doesn't.
     *
     * @param line  the line
     * @param score the score of the line
     */
    void set(BoardLine line, int score);

    /**
     * Removes a line from its score.
     *
     * @param score the score of the line to remove
     */
    void remove(int score);

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
    BoardLine getName();

    /**
     * Sets the name of the board.
     *
     * @param nameLine the new name of the board
     */
    void setName(BoardLine nameLine);

    /**
     * Gets the current lines of the board.
     *
     * @return the lines of the board
     */
    Map<Integer, BoardLine> getLines();

}
