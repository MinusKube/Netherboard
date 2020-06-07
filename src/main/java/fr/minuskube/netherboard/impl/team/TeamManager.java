package fr.minuskube.netherboard.impl.team;

public interface TeamManager {

    void createTeam(int score, String line);
    void updateTeam(int score, String line);
    void deleteTeam(int score, String line);

}
