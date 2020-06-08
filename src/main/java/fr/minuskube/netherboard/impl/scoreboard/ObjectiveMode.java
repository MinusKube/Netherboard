package fr.minuskube.netherboard.impl.scoreboard;

public enum ObjectiveMode {

    CREATE(0),
    REMOVE(1),
    UPDATE(2);

    private final int id;

    ObjectiveMode(int id) {
        this.id = id;
    }

    public int getId() { return id; }

}
