package sawfowl.commandpack.api.network.listeners;

import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;
import sawfowl.commandpack.api.network.packets.RawPacket;

public interface RawPacketListener {

	void read(MixinServerPlayer player, RawPacket packet);

}
