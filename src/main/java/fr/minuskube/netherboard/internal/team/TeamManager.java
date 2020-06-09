package fr.minuskube.netherboard.internal.team;

public interface TeamManager {

    String getEntryForLine(int score, String line);

    void createTeam(int score, String line);
    void updateTeam(int score, String line);
    void deleteTeam(int score, String line);

}
