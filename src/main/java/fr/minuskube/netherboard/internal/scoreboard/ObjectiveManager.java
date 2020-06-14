package fr.minuskube.netherboard.internal.scoreboard;

public interface ObjectiveManager {

    void create(String name);
    void updateLine(int score, String line);
    void removeLine(int score, String line);
    void display();

}
