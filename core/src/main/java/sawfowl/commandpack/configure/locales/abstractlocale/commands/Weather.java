package sawfowl.commandpack.configure.locales.abstractlocale.commands;

import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.text.Component;

public interface Weather {

	Component getSun(ServerWorld world);

	Component getRain(ServerWorld world);

	Component getThunder(ServerWorld world);

}
