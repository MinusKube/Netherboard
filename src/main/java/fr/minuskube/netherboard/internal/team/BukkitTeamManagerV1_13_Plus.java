package fr.minuskube.netherboard.internal.team;

import fr.minuskube.netherboard.util.MinecraftColorCode;
import fr.minuskube.netherboard.util.MinecraftConstants;
import org.bukkit.entity.Player;
import org.joor.Reflect;
import org.joor.ReflectException;

import java.util.Set;

public class BukkitTeamManagerV1_13_Plus implements TeamManager {

    /*

        TODO: Support other versions
        TODO: Better exception handling
        TODO: Load reflection classes only once

     */

    private interface PlayerConnectionProxy {
        void sendPacket(Object packet);
    }

    private final Player player;
    private final PlayerConnectionProxy playerConnection;

    public BukkitTeamManagerV1_13_Plus(Player player) {
        this.player = player;

        this.playerConnection = Reflect.on(player)
                .call("getHandle")
                .field("playerConnection")
                .as(PlayerConnectionProxy.class);
    }

    private TeamNameData calculateTeamNameData(int score, String line) {
        // Since team prefixes and suffixes have no limit in 1.13+,
        // put the whole line in it.

        return new TeamNameData(
                "netherboard" + score,

                line,
                String.valueOf(MinecraftColorCode.fromIndex(score)),
                ""
        );
    }

    @Override
    public String getEntryForLine(int score, String line) {
        return calculateTeamNameData(score, line).getEntry();
    }

    @Override
    public void createTeam(int score, String line) {
        TeamNameData nameData = calculateTeamNameData(score, line);

        try {
            sendTeamPacket(TeamMode.CREATE, nameData);
        } catch (ReflectException e) {
            throw new IllegalStateException("Unable to send team creation packet. (Unsupported version?)");
        }
    }

    @Override
    public void updateTeam(int score, String line) {
        TeamNameData nameData = calculateTeamNameData(score, line);

        try {
            sendTeamPacket(TeamMode.UPDATE, nameData);
        } catch (ReflectException e) {
            throw new IllegalStateException("Unable to send team update packet. (Unsupported version?)");
        }
    }

    @Override
    public void deleteTeam(int score, String line) {
        TeamNameData nameData = calculateTeamNameData(score, line);

        try {
            sendTeamPacket(TeamMode.REMOVE, nameData);
        } catch (ReflectException e) {
            throw new IllegalStateException("Unable to send team deletion packet. (Unsupported version?)");
        }
    }

    private void sendTeamPacket(TeamMode mode, TeamNameData nameData) throws ReflectException {
        /*
            This method invokes the equivalent of the given code with Reflection:

                var team = new ScoreboardTeam(null, nameData.getName());

                if (mode != TeamMode.REMOVE) {
                    var prefix = new ChatComponentText(nameData.getPrefix());
                    team.e = prefix;

                    var suffix = new ChatComponentText(nameData.getSuffix());
                    team.f = suffix;

                    Set<String> entrySet = team.c;
                    entrySet.add(nameData.getEntry());
                }

                var packetTeam = new PacketPlayOutScoreboardTeam(team, mode.getId());

                EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
                nmsPlayer.playerConnection.sendPacket(packetTeam);
         */

        String nmsPackage = MinecraftConstants.VERSION.getNMSPackage();

        Object team = Reflect.onClass(nmsPackage + ".ScoreboardTeam")
                .create(null, nameData.getName())
                .get();

        if (mode != TeamMode.REMOVE) {
            Reflect chatComponentText = Reflect.onClass(nmsPackage + ".ChatComponentText");

            Object prefix = chatComponentText.create(nameData.getPrefix()).get();
            Reflect.on(team).set("e", prefix);

            Object suffix = chatComponentText.create(nameData.getSuffix()).get();
            Reflect.on(team).set("f", suffix);

            Set<String> entrySet = Reflect.on(team).get("c");
            entrySet.add(nameData.getEntry());
        }

        Object packetTeam = Reflect.onClass(nmsPackage + ".PacketPlayOutScoreboardTeam")
                .create(team, mode.getId())
                .get();

        this.playerConnection.sendPacket(packetTeam);
    }

}
