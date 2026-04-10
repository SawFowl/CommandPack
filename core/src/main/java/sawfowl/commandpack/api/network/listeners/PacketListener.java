package sawfowl.commandpack.api.network.listeners;

import sawfowl.commandpack.api.game.server.player.CPServerPlayer;
import sawfowl.commandpack.api.network.packets.SerializedPacket;

public interface PacketListener<T> {

	void read(CPServerPlayer player, SerializedPacket<T> packet);

}
