package sawfowl.commandpack.api.mixin.network;

import java.util.List;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.localeapi.api.Text;

/**
 * The interface provides additional options for player interaction such as:<br>
 * * Sending a custom data packet.<br>
 * * Send messages as a string or contained in the {@link Text} interface.<br>
 * * Getting a list of player's mods if they are available and if the server uses Forge.<br>
 * <br>
 * All existing methods in the {@link ServerPlayer} interface will still be available.
 * 
 * @author SawFowl
 */
public interface MixinServerPlayer extends ServerPlayer {

	/**
	 * Converting the type of the player class to get additional methods.
	 */
	public static MixinServerPlayer cast(ServerPlayer player) {
		return (MixinServerPlayer) player;
	}

	/**
	 * Sending a data packet to the player.
	 */
	void sendPacket(CustomPacket packet);

	/**
	 * Additional method for sending messages to a player.<br>
	 * A placeholder will be automatically applied with the player's name substituted.
	 */
	void sendMessage(Text message);

	/**
	 * Additional method for sending messages to a player.<br>
	 * A placeholder will be automatically applied with the player's name substituted.
	 */
	void sendMessage(String message);

	/**
	 * Getting a list of mods from a player. Will work only on the server with Forge.<br>
	 * If you have a vanilla server, the list will always be empty.<br>
	 * The list will also be empty if the player has no mods.
	 */
	List<String> getModList();

	long getPing();

}
