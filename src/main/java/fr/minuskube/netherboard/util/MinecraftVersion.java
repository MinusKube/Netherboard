package fr.minuskube.netherboard.util;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.reflect.FieldUtils;

import java.util.Optional;
import java.util.stream.Stream;

public class MinecraftVersion {

    public static final MinecraftVersion V1_7  = new MinecraftVersion("v1_7_R1",  1, 7);
    public static final MinecraftVersion V1_8  = new MinecraftVersion("v1_8_R1",  1, 8);
    public static final MinecraftVersion V1_13 = new MinecraftVersion("v1_13_R1", 1, 13);

    private final String packageName;
    private final int major;
    private final int minor;

    private MinecraftVersion(String packageName, int major, int minor) {
        this.packageName = packageName;
        this.major = major;
        this.minor = minor;
    }

    public boolean isHigherOrEqualThan(MinecraftVersion other) {
        return major > other.major || (major == other.major && minor >= other.minor);
    }

    public String getNMSPackage() {
        return "net.minecraft.server." + packageName;
    }

    public String getCBPackage() {
        return "org.bukkit.craftbukkit." + packageName;
    }

    private static Optional<MinecraftVersion> tryFetchBukkit() {
        try {
            Class<?> bukkitClass = ClassUtils.getClass("org.bukkit.Bukkit");
            Class<?> serverClass = FieldUtils.readStaticField(bukkitClass, "getServer").getClass();

            String packageName = ClassUtils.getPackageName(serverClass);
            String versionName = packageName.substring(packageName.lastIndexOf('.') + 1);

            String[] splitVersion = versionName.split("_");

            MinecraftVersion version = new MinecraftVersion(
                    versionName,
                    Integer.parseInt(splitVersion[0]),
                    Integer.parseInt(splitVersion[1])
            );

            return Optional.of(version);
        } catch (ClassNotFoundException | IllegalAccessException | NumberFormatException e) {
            return Optional.empty();
        }
    }

    private static Optional<MinecraftVersion> tryFetchSponge() {
        return Optional.empty();
    }

    public static MinecraftVersion fetchCurrent() {
        return Stream.of(tryFetchBukkit(), tryFetchSponge())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Unable to fetch any Minecraft Version. (Unsupported version?)"));
    }

}
