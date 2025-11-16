package sawfowl.commandpack.apiclasses.network;

import java.nio.charset.StandardCharsets;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.network.ServerConnectionState;
import org.spongepowered.api.network.channel.ChannelBuf;
import org.spongepowered.api.network.channel.raw.play.RawPlayDataHandler;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;
import sawfowl.commandpack.api.network.listeners.PacketListener;
import sawfowl.commandpack.api.network.packets.RawPacket;
import sawfowl.commandpack.api.network.packets.SerializedPacket;
import sawfowl.commandpack.apiclasses.network.SerializedPacketBuilder.SerializedPacketImpl;

public class SpongeChannelHandler implements RawPlayDataHandler<ServerConnectionState.Game> {

	private final ResourceKey channel;
	private final CommandPackInstance plugin;
	public SpongeChannelHandler(CommandPackInstance plugin, ResourceKey channel) {
		this.plugin = plugin;
		this.channel = channel;
	}

	@Override
	public void handlePayload(ChannelBuf data, ServerConnectionState.Game state) {
		handle(MixinServerPlayer.cast(state.player()), new RawPacketImpl(channel, data, data.available() > 0 ? new String(data.readBytes(data.available()), StandardCharsets.UTF_8) : ""));
	}

	private void handle(MixinServerPlayer player, RawPacket rawPacket) {
		plugin.getPayloadsService().getRawListeners(rawPacket.channel()).forEach(listener -> listener.read(player, rawPacket));
		handleSerialized(player, rawPacket, plugin.getPayloadsService().containsSerializer(rawPacket.channel()), plugin.getPayloadsService().containsBufferSerializer(rawPacket.channel()));
	}

	@SuppressWarnings("unchecked")
	private void handleSerialized(MixinServerPlayer player, RawPacket rawPacket, boolean stringSerializer, boolean bufferSerializer) {
		if(stringSerializer || bufferSerializer) for(PacketListener<?> listener : plugin.getPayloadsService().getListeners(rawPacket.channel())) listener.read(player, serialize(rawPacket, bufferSerializer));
	}

	@SuppressWarnings("rawtypes")
	private SerializedPacket serialize(RawPacket packet, boolean bufferSerializer) {
		return ((SerializedPacketImpl) (bufferSerializer 
			?
			SerializedPacket.ofBuffer(packet.channel(), plugin.getPayloadsService().getBufferSerializer(packet.channel()))
			:
			SerializedPacket.of(packet.channel(), plugin.getPayloadsService().getSerializer(packet.channel()))))
		.apply(packet.getBuffer(), packet.getDataAsString());
	}
	
}
