pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

rootProject.name = "netherboard"

include("core")
include("bukkit")
include("minestom")

project(":core").name = "core"
project(":bukkit").name = "bukkit"
project(":minestom").name = "minestom"