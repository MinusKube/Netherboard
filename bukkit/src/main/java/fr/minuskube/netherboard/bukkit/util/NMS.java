package fr.minuskube.netherboard.bukkit.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class NMS {

    private static final Logger LOGGER = LoggerFactory.getLogger(NMS.class);

    private static final String OLD_PACKAGE_NAME;
    private static final Version VERSION;

    public static final Field PLAYER_SCORES;

    public static final Constructor<?> PACKET_SCORE_REMOVE;
    public static final Constructor<?> PACKET_SCORE;

    public static final Object ENUM_SCORE_ACTION_CHANGE;
    public static final Object ENUM_SCORE_ACTION_REMOVE;

    public static final Constructor<?> SB_SCORE;
    public static final Method SB_SCORE_SET;

    public static final Constructor<?> PACKET_OBJ;
    public static final Constructor<?> PACKET_DISPLAY;

    public static final Field PLAYER_CONNECTION;
    public static final Method SEND_PACKET;

    static {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        String ver = name.substring(name.lastIndexOf('.') + 1);
        VERSION = new Version(ver);
        OLD_PACKAGE_NAME = "net.minecraft.server." + ver;

        Field playerScores = null;

        Constructor<?> packetScoreRemove = null;
        Constructor<?> packetScore = null;

        Object enumScoreActionChange = null;
        Object enumScoreActionRemove = null;

        Constructor<?> sbScore = null;
        Method sbScoreSet = null;

        Constructor<?> packetObj = null;
        Constructor<?> packetDisplay = null;

        Field playerConnection = null;
        Method sendPacket = null;

        try {
            Class<?> packetScoreClass = getClass("net.minecraft.network.protocol.game", "PacketPlayOutScoreboardScore");
            Class<?> packetDisplayClass = getClass("net.minecraft.network.protocol.game", "PacketPlayOutScoreboardDisplayObjective");
            Class<?> packetObjClass = getClass("net.minecraft.network.protocol.game", "PacketPlayOutScoreboardObjective");

            Class<?> scoreClass = getClass("net.minecraft.world.scores", "ScoreboardScore");

            Class<?> sbClass = getClass("net.minecraft.world.scores", "Scoreboard");
            Class<?> objClass = getClass("net.minecraft.world.scores", "ScoreboardObjective");

            Class<?> playerClass = getClass("net.minecraft.server.level", "EntityPlayer");
            Class<?> playerConnectionClass = getClass("net.minecraft.server.network", "PlayerConnection");
            Class<?> packetClass = getClass("net.minecraft.network.protocol", "Packet");


            sbScore = scoreClass.getConstructor(sbClass, objClass, String.class);
            sbScoreSet = scoreClass.getMethod("setScore", int.class);

            packetObj = packetObjClass.getConstructor(objClass, int.class);

            packetDisplay = packetDisplayClass.getConstructor(int.class, objClass);

            sendPacket = playerConnectionClass.getMethod("sendPacket", packetClass);

            if (VERSION.isBelow1_17()) {
                playerScores = sbClass.getDeclaredField("playerScores");
                playerScores.setAccessible(true);

                playerConnection = playerClass.getField("playerConnection");
            }
            else {
                playerScores = sbClass.getDeclaredField("j");
                playerScores.setAccessible(true);

                playerConnection = playerClass.getField("b");
            }

            switch (VERSION.getMajor()) {
                case "1.7":
                    packetScore = packetScoreClass.getConstructor(scoreClass, int.class);
                    break;
                case "1.8":
                case "1.9":
                case "1.10":
                case "1.11":
                case "1.12":
                    packetScore = packetScoreClass.getConstructor(scoreClass);
                    packetScoreRemove = packetScoreClass.getConstructor(String.class, objClass);
                    break;
                default:
                    Class<?> scoreActionClass = getClass("net.minecraft.server", "ScoreboardServer$Action");

                    packetScore = packetScoreClass.getConstructor(scoreActionClass,
                            String.class, String.class, int.class);

                    enumScoreActionChange = scoreActionClass.getEnumConstants()[0];
                    enumScoreActionRemove = scoreActionClass.getEnumConstants()[1];
                    break;
            }
        } catch(Exception e) {
            LOGGER.error("Error while loading NMS methods. (Unsupported Minecraft version?)", e);
        }

        PLAYER_SCORES = playerScores;

        PACKET_SCORE_REMOVE = packetScoreRemove;
        PACKET_SCORE = packetScore;

        ENUM_SCORE_ACTION_CHANGE = enumScoreActionChange;
        ENUM_SCORE_ACTION_REMOVE = enumScoreActionRemove;

        SB_SCORE = sbScore;
        SB_SCORE_SET = sbScoreSet;

        PACKET_OBJ = packetObj;
        PACKET_DISPLAY = packetDisplay;

        PLAYER_CONNECTION = playerConnection;
        SEND_PACKET = sendPacket;
    }

    public static Version getVersion() {
        return VERSION;
    }

    public static Class<?> getClass(String realPackageName, String name) throws ClassNotFoundException {
        String packageName = VERSION.isBelow1_17() ? OLD_PACKAGE_NAME : realPackageName;

        return Class.forName(packageName + "." + name);
    }

    private static final Map<Class<?>, Method> HANDLES = new HashMap<>();
    public static Object getHandle(Object obj) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {

        Class<?> clazz = obj.getClass();

        if(!HANDLES.containsKey(clazz)) {
            Method method = clazz.getDeclaredMethod("getHandle");

            if(!method.isAccessible())
                method.setAccessible(true);

            HANDLES.put(clazz, method);
        }

        return HANDLES.get(clazz).invoke(obj);
    }

    public static void sendPacket(Object packet, Player... players) {
        for(Player p : players) {
            try {
                Object playerConnection = PLAYER_CONNECTION.get(getHandle(p));
                SEND_PACKET.invoke(playerConnection, packet);
            } catch(IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                LOGGER.error("Error while sending packet to player. (Unsupported Minecraft version?)", e);
            }
        }
    }

    public static class Version {

        private final String name;

        private final String major;
        private final String minor;

        Version(String name) {
            this.name = name;

            String[] splitName = name.split("_");

            this.major = splitName[0].substring(1) + "." + splitName[1];
            this.minor = splitName[2].substring(1);
        }

        public String getName() { return name; }

        public String getMajor() { return major; }
        public String getMinor() { return minor; }

        public boolean isBelow1_17() {
            return  major.equals("1.7")  ||
                    major.equals("1.8")  ||
                    major.equals("1.9")  ||
                    major.equals("1.10") ||
                    major.equals("1.11") ||
                    major.equals("1.12") ||
                    major.equals("1.13") ||
                    major.equals("1.14") ||
                    major.equals("1.15") ||
                    major.equals("1.16");
        }

    }

}
