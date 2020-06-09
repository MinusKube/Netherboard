package fr.minuskube.netherboard.internal.team;

public enum TeamMode {

    CREATE(0),
    REMOVE(1),
    UPDATE(2);

    private final int id;

    TeamMode(int id) {
        this.id = id;
    }

    public int getId() { return id; }

}
