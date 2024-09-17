package sawfowl.commandpack.api.data.punishment;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;

public interface Mute extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	Instant getCreated();

	String getCreatedTimeString();

	boolean isIndefinitely();

	UUID getUniqueId();

	String getName();

	Optional<Instant> getExpiration();

	Optional<String> getExpirationTimeString();

	Optional<Component> getSource();

	Optional<Component> getReason();

	boolean isExpired();
	
	interface Builder extends AbstractBuilder<Mute>, org.spongepowered.api.util.Builder<Mute, Builder> {

		Builder created(Instant value);

		Builder target(ServerPlayer value);

		Builder target(User value);

		Builder target(UUID uuid, String name);

		Builder source(Component name);

		Builder source(ServerPlayer value);

		Builder expiration(Instant value);

		Builder reason(Component value);

		Mute from(Mute mute);

	}

}
