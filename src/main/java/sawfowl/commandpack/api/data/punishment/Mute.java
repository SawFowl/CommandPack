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

	Instant getCreationDate();

	boolean isIndefinitely();

	UUID getUniqueId();

	String getName();

	Optional<Instant> getExpirationDate();

	Optional<Component> getSource();

	Optional<Component> getReason();
	
	interface Builder extends AbstractBuilder<Mute>, org.spongepowered.api.util.Builder<Mute, Builder> {

		Builder creationDate(Instant value);

		Builder target(ServerPlayer value);

		Builder target(User value);

		Builder source(Component name);

		Builder source(ServerPlayer value);

		Builder expirationDate(Instant value);

		Builder reason(Component value);

	}

}
