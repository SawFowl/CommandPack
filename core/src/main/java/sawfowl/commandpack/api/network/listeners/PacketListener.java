package sawfowl.commandpack.api.network.listeners;

import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;
import sawfowl.commandpack.api.network.packets.SerializedPacket;

public interface PacketListener<T> {

	void read(MixinServerPlayer player, SerializedPacket<T> packet);

}
