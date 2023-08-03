package sawfowl.commandpack.api.data.punishment;

import java.time.Instant;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;

public interface Warn extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	Instant getCreated();

	String getCreatedTimeString();

	long getCreationTime();

	boolean isIndefinitely();

	Optional<Instant> getExpiration();

	Optional<String> getExpirationTimeString();

	@Nullable
	long getExpirationTime();

	Optional<Component> getSource();

	Optional<Component> getReason();

	boolean isExpired();

	interface Builder extends AbstractBuilder<Warn>, org.spongepowered.api.util.Builder<Warn, Builder> {

		Builder created(Instant value);

		Builder source(Component name);

		Builder source(ServerPlayer value);

		Builder expiration(Instant value);

		Builder reason(Component value);

		Warn from(Warn warn);

	}

}
