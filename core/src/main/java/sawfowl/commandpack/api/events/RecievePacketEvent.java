package sawfowl.commandpack.api.events;

import java.util.UUID;

import org.spongepowered.api.event.Event;

import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;

public interface RecievePacketEvent extends Event {

	UUID getPlayerUniqueId();

	MixinServerPlayer getMixinPlayer();

	String getPacketName();

	byte[] getData();

	String getDataAsString();

}
