# Netherboard

Scoreboard API for your Minecraft Sponge and Bukkit Plugins.  
**You can use this as a Plugin, or just add it to your dependencies.**

## Features:
- Works for all the **Bukkit** versions since **1.7** (even **1.13**!).
- Works with **Sponge**! (Tell me if a version is not compatible)
- No blinking!
- Max characters per line:
  - **1.7:** 16 (Working on it to allow 48 characters)
  - **1.8+**: 40 (Working on it to allow 72 characters, *not sure if possible, we'll see*)
  
## TODO:
- Allow more characters per line (Using teams)

## Examples:
### Bukkit

Create a board:
```java
BPlayerBoard board = Netherboard.instance().createBoard(player, "My Scoreboard");
```

Create a board with your own Scoreboard object:
```java
BPlayerBoard board = Netherboard.instance().createBoard(player, scoreboard, "My Scoreboard");
```

Get a player's board:
```java
BPlayerBoard board = Netherboard.instance().getBoard(player);
```

When you have your board, you can do whatever you want with it:
```java
// Set a line
// If there is already a line with this score, it will replace it.
board.set("Test Score", 5);

// Get a line from its score
board.get(5);

// Remove a line
board.remove(5);

// Change the name of the board
board.setName("My New Scoreboard");

// Delete the board
board.delete();
```

### Sponge
Same thing than Bukkit, but the object is called `SPlayerBoard` and the methods requires `Text` objects instead of `String` ones.  
*NB: With Sponge, please do not create boards for different players with the same `scoreboard`.*

## Usage
To use the Netherboard API, either:
  - put it in the `plugins` folder of your server, add it to your dependencies in your plugin.yml (e.g. `depend: [SmartInvs]`) and add it to the dependencies in your IDE.
  - add it to the dependencies in your IDE and include it in your plugin jar.

You can download the latest version on the [Releases page](https://github.com/MinusKube/Netherboard/releases) on Github.

You can also use a build system:
### Gradle
```gradle
repositories {
    mavenCentral()
}

dependencies {
    compile 'fr.minuskube:netherboard-bukkit:1.1.0'
    
    // Or if you use Sponge:
    // compile 'fr.minuskube:netherboard-sponge:1.1.0'
}
```

### Maven
```xml
<dependency>
  <groupId>fr.minuskube</groupId>
  <artifactId>netherboard-bukkit</artifactId>
  
  <!--
  Or if you use Sponge:
  
  <artifactId>netherboard-sponge</artifactId>
  -->
  
  <version>1.1.0</version>
</dependency>
```

## Issues:
If you have a problem with the API, or you want to request a feature, make an issue [here](https://github.com/MinusKube/netherboard/issues).
