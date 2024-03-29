# CommandPack [![](https://jitpack.io/v/SawFowl/CommandPack.svg)](https://jitpack.io/#SawFowl/CommandPack)
####  ***[LocaleAPI](https://ore.spongepowered.org/Semenkovsky_Ivan/LocaleAPI) - required.***

Commands and Permissions -> https://github.com/SawFowl/CommandPack/wiki/Commands-and-Permissions---EN


Currently implemented:
* Multilanguage. Ability to translate the plugin into any language. All languages will be used at the same time.
* Customizable ability to save inventory and experience of the player at death(metaperms).
* Customizable restrictions on entity spawns.
* Logging the list of mods installed on the client player connects to the server.
* Configurable restrictions on allowable mods on the player’s client that connects to the server.
* Command execution conditions, as well as adding command aliases.  The economy is supported.
* All teleportation commands, including teleportation requests and teleportation to random coordinates. 
* System of server and private warps.
* Kits. The economy is supported.
* AFK
* Opening with a menu command functional blocks such as: workbench, anvil, enderchest, enchantment table.
* Viewing and changing the inventory of other players by the administrator, including the enderchest and backpack.
* The execution commands logging looks like in Nucleus.
* Viewing server information.
* View information and reload plugins.
* Viewing information about mods. Plugins are separated from mods.
* Change of time, weather, game mode. Removed game mode identifiers have been returned.
* Worlds Management(full).
* Join/Leave messages
* Join commands
* Flexible punishment system with support for h2 and MySQL databases (large customization).
* Sponge Economy API Implementation.
* And much more... For more information, see the list of commands at the link above.

##### For developers:
JavaDoc -> https://sawfowl.github.io/CommandPack/

##### Get API
```java
	private CommandPack commandPack;
	@Listener
	public void onCommandPackPostApiEvent(CommandPack.PostAPI event) {
		commandPack = event.getAPI();
	}
```


##### Gradle
```gradle
repositories {
	...
	maven { 
		name = "JitPack"
		url 'https://jitpack.io' 
	}
}
dependencies {
	...
	implementation 'com.github.SawFowl:CommandPack:2.2'
}
```


