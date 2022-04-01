plugins {
    id("io.papermc.paperweight.userdev") version "1.3.5"
}

tasks.jar {
    archiveFileName.set("Netherboard-Bukkit-" + project.version + ".jar")
    archiveBaseName.set("netherboard-bukkit")
}

repositories {
    mavenLocal()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    paperDevBundle("1.18.2-R0.1-SNAPSHOT")
    implementation(project(":core"))
}