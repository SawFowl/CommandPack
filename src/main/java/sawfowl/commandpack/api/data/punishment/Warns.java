package sawfowl.commandpack.api.data.punishment;

import java.util.List;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.builder.AbstractBuilder;
import sawfowl.commandpack.configure.configs.punishment.WarnsData;

public interface Warns extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	UUID getUniqueId();

	String getName();

	List<Warn> getWarns();

	int totalWarns();

	int inAllTime();

	void addWarn(Warn warn);

	void removeWarn(Warn warn);

	void checkExpired();

	interface Builder extends AbstractBuilder<Warns>, org.spongepowered.api.util.Builder<Warns, Builder> {

		Builder target(ServerPlayer value);

		Builder target(User value);

		Builder warn(Warn value);

		WarnsData from(Warns warns);

	}

}
