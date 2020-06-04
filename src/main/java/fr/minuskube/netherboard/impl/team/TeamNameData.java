package fr.minuskube.netherboard.impl.team;

public class TeamNameData {

    private final String prefix;
    private final String name;
    private final String suffix;

    public TeamNameData(String prefix, String name, String suffix) {
        this.prefix = prefix;
        this.name = name;
        this.suffix = suffix;
    }

    public String getPrefix() { return prefix; }
    public String getName() { return name; }
    public String getSuffix() {  return suffix; }

}
