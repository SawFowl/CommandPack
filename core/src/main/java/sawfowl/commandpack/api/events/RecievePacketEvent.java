package sawfowl.commandpack.api.events;

import java.util.UUID;

import org.spongepowered.api.event.Event;
import org.spongepowered.api.profile.GameProfile;

import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;

/**
 * This event is called when data packets are transmitted from the client to the server.
 * 
 * @author SawFowl
 */
public interface RecievePacketEvent extends Event {

	/**
	 * UUID of the player from whom the data packet was received.
	 */
	UUID getPlayerUniqueId();

	/**
	 * The player from whom the data packet was received.
	 * @return {@link MixinServerPlayer}
	 */
	MixinServerPlayer getMixinPlayer();

	/**
	 * Getting the profile of the player from whom the data packet was received.
	 * @return {@link GameProfile}
	 */
	GameProfile getPlayerProfile();

	/**
	 * Data packet identifier.
	 */
	String getPacketName();

	/**
	 * RawSettings data.
	 */
	byte[] getData();

	/**
	 * Returns the number of readable bytes which is equal to (this.writerIndex - this.readerIndex).
	 */
	int readableBytes();

	/**
	 * Data converted to a string.<br>
	 * The method should be safe (with standard Minecraft packages it is, WeCui package is also processed normally),<br>
	 * but it is desirable to test it before full use.<br>
	 * Be sure to check the package ID before using this method,<br>
	 * it will save you from working with unnecessary data and reduce the risk of failed decryption to a minimum.<br>
	 */
	String getDataAsString();

	/**
	 * Returns true if and only if (this.writerIndex - this.readerIndex) is greater than 0.
	 * @return
	 */
	boolean isReadable();

}
