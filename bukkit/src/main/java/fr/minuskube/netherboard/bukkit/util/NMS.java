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

    private static String packageName;
    private static Version version;

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
        version = new Version(ver);

        packageName = "net.minecraft.server." + ver;

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
            Class<?> packetScoreClass = getClass("PacketPlayOutScoreboardScore");
            Class<?> packetDisplayClass = getClass("PacketPlayOutScoreboardDisplayObjective");
            Class<?> packetObjClass = getClass("PacketPlayOutScoreboardObjective");

            Class<?> scoreClass = getClass("ScoreboardScore");
            Class<?> scoreActionClass = getClass("ScoreboardServer$Action");

            Class<?> sbClass = getClass("Scoreboard");
            Class<?> objClass = getClass("ScoreboardObjective");

            Class<?> playerClass = getClass("EntityPlayer");
            Class<?> playerConnectionClass = getClass("PlayerConnection");
            Class<?> packetClass = getClass("Packet");

            playerScores = sbClass.getDeclaredField("playerScores");
            playerScores.setAccessible(true);

            sbScore = scoreClass.getConstructor(sbClass, objClass, String.class);
            sbScoreSet = scoreClass.getMethod("setScore", int.class);

            switch(version.getMajor()) {
                case "1.7":
                    packetScore = packetScoreClass.getConstructor(scoreClass, int.class);

                    packetObj = packetObjClass.getConstructor(int.class, objClass);
                    break;
                case "1.13":
                    packetScore = packetScoreClass.getConstructor(scoreActionClass,
                            String.class, String.class, int.class);

                    enumScoreActionChange = scoreActionClass.getEnumConstants()[0];
                    enumScoreActionRemove = scoreActionClass.getEnumConstants()[1];

                    packetObj = packetObjClass.getConstructor(objClass, int.class);
                    break;
                default:
                    packetScore = packetScoreClass.getConstructor(scoreClass);
                    packetScoreRemove = packetScoreClass.getConstructor(String.class, objClass);

                    packetObj = packetObjClass.getConstructor(objClass, int.class);
                    break;
            }

            packetDisplay = packetDisplayClass.getConstructor(int.class, objClass);

            playerConnection = playerClass.getField("playerConnection");
            sendPacket = playerConnectionClass.getMethod("sendPacket", packetClass);
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
        return version;
    }

    public static Class<?> getClass(String name) {
        try {
            return Class.forName(packageName + "." + name);
        } catch(ClassNotFoundException e) {
            return null;
        }
    }

    private static Map<Class<?>, Method> handles = new HashMap<>();
    public static Object getHandle(Object obj) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {

        Class<?> clazz = obj.getClass();

        if(!handles.containsKey(clazz)) {
            Method method = clazz.getDeclaredMethod("getHandle");

            if(!method.isAccessible())
                method.setAccessible(true);

            handles.put(clazz, method);
        }

        return handles.get(clazz).invoke(obj);
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

        private String name;

        private String major;
        private String minor;

        Version(String name) {
            this.name = name;

            String[] splitted = name.split("_");

            this.major = splitted[0].substring(1) + "." + splitted[1];
            this.minor = splitted[2].substring(1);
        }

        public String getName() { return name; }

        public String getMajor() { return major; }
        public String getMinor() { return minor; }

    }

}
