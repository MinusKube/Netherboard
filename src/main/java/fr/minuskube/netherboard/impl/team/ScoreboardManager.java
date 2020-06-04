package fr.minuskube.netherboard.impl.team;

public interface ScoreboardManager {

    /*

        Init:
            Scoreboard Objective (Current)
            Scoreboard Objective (Buffer)

        SetScore:
            Teams (Delete)

            Teams (Create)
                1.7:
                    Prefix: 16 first characters
                    Entry: 14 next characters + §x
                    Suffix: Last color + 14 next characters
                1.8-1.12:
                    Prefix: 16 first characters
                    Entry: 38 next characters + §x
                    Suffix: Last color + 14 next characters
                1.13+:
                    Prefix: All characters
                    Entry: §x

            Update Score (Buffer)

        Display:
            Display Scoreboard (Buffer)
            Switch Current & Buffer
            Update Score (Buffer) (Multiple times)

     */

    void init();
    void updateLine(int score, String newLine);
    void display();
    void switchBuffer();

}
