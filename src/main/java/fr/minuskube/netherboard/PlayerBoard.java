package fr.minuskube.netherboard;

import fr.minuskube.netherboard.line.BoardLine;

import java.util.Map;

/**
 * Represents a Player's Scoreboard.
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
     * Sets a line at the given score.
     * This will update the line if it already exists, and create it if it doesn't.
     *
     * @param score the score of the line
     * @param line  the line
     */
    void set(int score, BoardLine line);

    /**
     * Removes the line at the given score.
     *
     * @param score the score of the line to remove
     */
    void remove(int score);

    /**
     * Totally deletes the board, after this, you can't use this instance anymore,
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

    /**
     * Sets all the lines of the board.
     *
     * @param lines the new lines with their score
     */
    void setLines(Map<Integer, BoardLine> lines);

}
