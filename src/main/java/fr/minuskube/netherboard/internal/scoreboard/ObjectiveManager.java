package fr.minuskube.netherboard.internal.scoreboard;

public interface ObjectiveManager {

    void create(String name);
    void updateLine(int score, String newLine);
    void removeLine(int score);
    void display();

}
