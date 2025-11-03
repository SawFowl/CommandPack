package sawfowl.commandpack.api.network.packets;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import net.kyori.adventure.builder.AbstractBuilder;

public interface RawPacket {

	String data();

	ResourceKey channel();

	/**
	 * Creating your data package.
	 * 
	 * @param channel - Packet ID. Usually has the form modid:chanel.
	 * @param data - Package data provided as a {@link String}
	 */
	static RawPacket of(ResourceKey channel, String data) {
		return builder().create(channel, data);
	}

	/**
	 * Creating your data package.
	 * You can use {@link JsonParser#parseString} to reverse the conversion on the client.
	 * 
	 * @param channel - Packet ID. Usually has the form modid:chanel.
	 * @param data - Package data provided as a {@link JsonElement}.
	 */
	static RawPacket of(ResourceKey channel, JsonElement data) {
		return of(channel, data.toString());
	}

	private static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	void sendTo(ServerPlayer player);

	interface Builder extends AbstractBuilder<RawPacket>, org.spongepowered.api.util.Builder<RawPacket, Builder> {

		RawPacket create(ResourceKey channel, String data);

	}

}
