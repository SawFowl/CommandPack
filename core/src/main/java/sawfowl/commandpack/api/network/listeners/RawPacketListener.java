package sawfowl.commandpack.api.network.listeners;

import sawfowl.commandpack.api.game.server.player.CPServerPlayer;
import sawfowl.commandpack.api.network.packets.RawPacket;

public interface RawPacketListener {

	void read(CPServerPlayer player, RawPacket packet);

}
