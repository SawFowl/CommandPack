package sawfowl.commandpack.api.mixin.network;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import net.kyori.adventure.builder.AbstractBuilder;

/**
 * The interface is used to send a custom data packet to a player<br>
 * for further processing of this data on his client by some mod.
 * 
 * @author SawFowl
 */
public interface CustomPacket extends DataSerializable {

	/**
	 * Creating your data package.
	 * 
	 * @param resourceLocation - Packet ID. Usually has the form modid:chanel.
	 * @param data - Package data provided as a {@link String}
	 */
	static CustomPacket of(String resourceLocation, String data) {
		return builder().create(resourceLocation, data);
	}

	/**
	 * Creating your data package.
	 * You can use {@link JsonParser#parseString} to reverse the conversion on the client.
	 * 
	 * @param resourceLocation - Packet ID. Usually has the form modid:chanel.
	 * @param data - Package data provided as a {@link JsonElement}.
	 */
	static CustomPacket of(String resourceLocation, JsonElement data) {
		return of(resourceLocation, data.toString());
	}

	/**
	 * Creating your data package.
	 * 
	 * @param resourceLocation - Packet ID. Usually has the form modid:chanel.
	 * @param data - Package data provided as a {@link String}
	 */
	static CustomPacket of(ResourceKey resourceLocation, String data) {
		return builder().create(resourceLocation.asString(), data);
	}

	/**
	 * Creating your data package.
	 * You can use {@link JsonParser#parseString} to reverse the conversion on the client.
	 * 
	 * @param resourceLocation - Packet ID. Usually has the form modid:chanel.
	 * @param data - Package data provided as a {@link JsonElement}.
	 */
	static CustomPacket of(ResourceKey resourceLocation, JsonElement data) {
		return of(resourceLocation.asString(), data.toString());
	}

	private static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	void sendTo(ServerPlayer player);

	interface Builder extends AbstractBuilder<CustomPacket>, org.spongepowered.api.util.Builder<CustomPacket, Builder> {

		CustomPacket create(String resourceLocation, String data);

	}

}
