package sawfowl.commandpack.configure.locales.abstractlocale.commands.world;

import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.text.Component;

public interface SpawnLogic {

	Component getEnable(ServerWorld world);

	Component getDisable(ServerWorld world);

}
