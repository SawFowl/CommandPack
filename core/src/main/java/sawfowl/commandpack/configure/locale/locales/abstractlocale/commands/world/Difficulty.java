package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.world;

import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.text.Component;

public interface Difficulty {

	Component getPeaceful(ServerWorld world);

	Component getLow(ServerWorld world);

	Component getNormal(ServerWorld world);

	Component getHard(ServerWorld world);

}
