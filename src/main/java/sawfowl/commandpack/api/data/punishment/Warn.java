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

	Instant getCreationDate();

	long getCreationTime();

	boolean isIndefinitely();

	Optional<Instant> getExpirationDate();

	@Nullable
	long getExpirationTime();

	Optional<Component> getSource();

	Optional<Component> getReason();

	boolean isExpired();

	interface Builder extends AbstractBuilder<Warn>, org.spongepowered.api.util.Builder<Warn, Builder> {

		Builder creationDate(Instant value);

		Builder source(Component name);

		Builder source(ServerPlayer value);

		Builder expirationDate(Instant value);

		Builder reason(Component value);

		Warn from(Warn warn);

	}

}
