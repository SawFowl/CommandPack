package sawfowl.commandpack.api.mixin.network;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import com.google.gson.JsonElement;

import net.kyori.adventure.builder.AbstractBuilder;

public interface CustomPacket extends DataSerializable {

	static CustomPacket of(String resourceLocation, String data) {
		return builder().create(resourceLocation, data);
	}

	static CustomPacket of(String resourceLocation, JsonElement data) {
		return builder().create(resourceLocation, data.toString());
	}

	private static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	void sendTo(ServerPlayer player);

	interface Builder extends AbstractBuilder<CustomPacket>, org.spongepowered.api.util.Builder<CustomPacket, Builder> {

		CustomPacket create(String resourceLocation, String data);

	}

}
