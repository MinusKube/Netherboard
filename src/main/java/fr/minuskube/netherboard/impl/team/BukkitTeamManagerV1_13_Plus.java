package fr.minuskube.netherboard.impl.team;

import fr.minuskube.netherboard.util.MinecraftColorCode;
import fr.minuskube.netherboard.util.MinecraftConstants;
import org.bukkit.entity.Player;
import org.joor.Reflect;
import org.joor.ReflectException;

public class BukkitTeamManagerV1_13_Plus implements TeamManager {

    /*

        TODO: Support other versions
        TODO: Better exception handling
        TODO: Add correct prefix/entry/suffix handling

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
                line,
                String.valueOf(MinecraftColorCode.fromIndex(score)),
                ""
        );
    }

    @Override
    public void createTeam(int score) {
        String teamName = "team" + score;

        try {
            sendTeamPacket(teamName, TeamMode.CREATE);
        } catch (ReflectException e) {
            throw new IllegalStateException("Unable to send team creation packet. (Unsupported version?)");
        }
    }

    @Override
    public void deleteTeam(int score) {
        String teamName = "team" + score;

        try {
            sendTeamPacket(teamName, TeamMode.REMOVE);
        } catch (ReflectException e) {
            throw new IllegalStateException("Unable to send team deletion packet. (Unsupported version?)");
        }
    }

    private void sendTeamPacket(String teamName, TeamMode mode) throws ReflectException {
        /*
            ScoreboardTeam team = new ScoreboardTeam(null, teamName);
            PacketPlayOutScoreboardTeam packetTeam = new PacketPlayOutScoreboardTeam(team, mode.getId());

            EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
            nmsPlayer.playerConnection.sendPacket(packetTeam);
         */

        String nmsPackage = MinecraftConstants.VERSION.getNMSPackage();

        Object team = Reflect.onClass(nmsPackage + ".ScoreboardTeam")
                .create(null, teamName)
                .get();
        Object packetTeam = Reflect.onClass(nmsPackage + ".PacketPlayOutScoreboardTeam")
                .create(team, mode.getId())
                .get();

        this.playerConnection.sendPacket(packetTeam);
    }

}
