package fr.minuskube.netherboard.internal.team;

public class TeamNameData {

    private final String prefix;
    private final String name;
    private final String entry;
    private final String suffix;

    public TeamNameData(String name, String prefix, String entry, String suffix) {
        this.name = name;
        this.prefix = prefix;
        this.entry = entry;
        this.suffix = suffix;
    }

    public String getName() { return name; }
    public String getPrefix() { return prefix; }
    public String getEntry() { return entry; }
    public String getSuffix() {  return suffix; }

}
