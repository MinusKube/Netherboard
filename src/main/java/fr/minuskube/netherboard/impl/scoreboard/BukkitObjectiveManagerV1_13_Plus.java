package fr.minuskube.netherboard.impl.scoreboard;

import com.google.common.base.Preconditions;
import fr.minuskube.netherboard.util.MinecraftConstants;
import org.bukkit.entity.Player;
import org.joor.Reflect;

public class BukkitObjectiveManagerV1_13_Plus implements ObjectiveManager {

    /*

        Init:
            Scoreboard Objective (Current)
            Scoreboard Objective (Buffer)

        SetScore:
            Teams (Delete)

            Teams (Create)
                1.7 (44 characters):
                    Prefix: 16 first characters
                    Entry: 14 next characters + §x
                    Suffix: Last color + 14 next characters
                1.8-1.12 (68 characters):
                    Prefix: 16 first characters
                    Entry: 38 next characters + §x
                    Suffix: Last color + 14 next characters
                1.13+ (infinite chars):
                    Prefix: All characters
                    Entry: §x

            Update Score (Buffer)

        Display:
            Display Scoreboard (Buffer)
            Switch Current & Buffer
            Update Score (Buffer) (Multiple times)

     */

    private interface PlayerConnectionProxy {
        void sendPacket(Object packet);
    }

    private final Player player;
    private final PlayerConnectionProxy playerConnection;

    private String currentObjective;
    private String bufferObjective;

    public BukkitObjectiveManagerV1_13_Plus(Player player) {
        this.player = player;

        this.playerConnection = Reflect.on(player)
                .call("getHandle")
                .field("playerConnection")
                .as(PlayerConnectionProxy.class);
    }

    @Override
    public void create(String name) {
        sendObjectivePacket(ObjectiveMode.CREATE, "netherboardA", name);
        sendObjectivePacket(ObjectiveMode.CREATE, "netherboardB", name);

        this.currentObjective = "netherboardA";
        this.bufferObjective  = "netherboardB";
    }

    @Override
    public void updateLine(int score, String newLine) {
        // TODO: Update buffer objective
    }

    @Override
    public void display() {
        // TODO: Check if Preconditions is available in every version of Spigot from 1.7 to latest
        Preconditions.checkNotNull(this.currentObjective, "The current objective has not been set (forgot to call #create(String)?).");
        Preconditions.checkNotNull(this.bufferObjective , "The buffer objective has not been set (forgot to call #create(String)?).");

        // Display Buffer
        sendDisplayPacket(this.bufferObjective);

        // Swap Current & Buffer
        String tempObjective = bufferObjective;
        this.bufferObjective  = this.currentObjective;
        this.currentObjective = tempObjective;

        // TODO: Update buffer objective
    }

    private void sendObjectivePacket(ObjectiveMode mode, String name, String displayName) {
        /*
            This method invokes the equivalent of the given code with Reflection:

                var criteria = IScoreboardCriteria.DUMMY;
                var displayNameComponent = new ChatComponentText(displayName);
                var criteriaHealthDisplay = EnumScoreboardHealthDisplay.INTEGER;

                var objective = new ScoreboardObjective(null, name, criteria, displayNameComponent, criteriaHealthDisplay);
                var packetObjective = new PacketPlayOutScoreboardObjective(objective, mode.getId());

                EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
                nmsPlayer.playerConnection.sendPacket(packetTeam);
         */

        String nmsPackage = MinecraftConstants.VERSION.getNMSPackage();

        Object criteria = Reflect.onClass(nmsPackage + ".IScoreboardCriteria")
                .get("DUMMY");

        Object displayNameComponent = Reflect.onClass(nmsPackage + ".ChatComponentText")
                .create(displayName)
                .get();

        Object criteriaHealthDisplay = Reflect.onClass(nmsPackage + ".IScoreboardCriteria.EnumScoreboardHealthDisplay")
                .get("INTEGER");

        Object objective = Reflect.onClass(nmsPackage + ".ScoreboardObjective")
                .create(null, name, criteria, displayNameComponent, criteriaHealthDisplay)
                .get();

        Object packetObjective = Reflect.onClass(nmsPackage + ".PacketPlayOutScoreboardObjective")
                .create(objective, mode.getId());

        playerConnection.sendPacket(packetObjective);
    }

    private void sendDisplayPacket(String objectiveName) {
        /*
            This method invokes the equivalent of the given code with Reflection:

                var packetDisplay = new PacketPlayOutScoreboardDisplayObjective(0, null);
                packetDisplay.b = objectiveName;

                EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
                nmsPlayer.playerConnection.sendPacket(packetTeam);
         */

        String nmsPackage = MinecraftConstants.VERSION.getNMSPackage();

        Object packetDisplay = Reflect.onClass(nmsPackage + ".PacketPlayOutScoreboardDisplayObjective")
                .create(0, null)
                .set("b", objectiveName)
                .get();

        playerConnection.sendPacket(packetDisplay);
    }

}
