package fr.minuskube.netherboard.impl.scoreboard;

public interface ObjectiveManager {

    void create(String name);
    void updateLine(int score, String newLine);
    void display();

}
