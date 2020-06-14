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
     * <b>Warning:</b> The score
     *
     * @param score the score of the line
     * @param line  the line
     */
    void set(int score, BoardLine line);

    /**
     * Sets a line at the given score.
     * This will update the line if it already exists, and create it if it doesn't.
     *
     * <p>
     *     This method is equivalent to:<br>
     *     <blockquote><pre>set(score, BoardLine.of(line))</pre></blockquote>
     * </p>
     *
     * @param score the score of the line
     * @param line  the line
     */
    void set(int score, String line);

    /**
     * Sets all the lines of the board.
     * This will clear all of the current board lines, then set
     * all of the given lines, from top to down, by giving them each a score
     * determined by {@code lines.length - index}.
     *
     * @param lines the new board lines
     */
    void setAll(BoardLine... lines);

    /**
     * Sets all the lines of the board.
     * This will clear all of the current board lines, then set
     * all of the given lines, from top to down, by giving them each a score
     * determined by {@code lines.length - index}.
     *
     * <p>
     *     This method is equivalent to calling the {@link #setAll(BoardLine...)} overload
     *     with an array of the same lines, but converted to {@link BoardLine}s using {@link BoardLine#of(String)}.
     * </p>
     *
     * @param lines the new board lines
     */
    void setAll(String... lines);

    /**
     * Removes the line at the given score.
     *
     * @param score the score of the line to remove
     */
    void remove(int score);

    /**
     * Totally deletes the board, after this, the player won't see the board anymore
     * and you'll not be able to use this instance anymore,
     * you'll need to create another PlayerBoard if you want to create the board again.
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
     * Sets the name of the board.
     *
     * <p>
     *     This method is equivalent to:<br>
     *     <blockquote><pre>setName(BoardLine.of(name))</pre></blockquote>
     * </p>
     *
     * @param name the new name of the board
     */
    void setName(String name);

    /**
     * Gets the current lines of the board.
     *
     * @return the lines of the board
     */
    Map<Integer, BoardLine> getLines();

    /**
     * Sets all the lines of the board using a Map, with the key corresponding
     * to the score, and the value corresponding to the line.
     *
     * @param lines the new lines with their score
     */
    void setLines(Map<Integer, BoardLine> lines);

}
