package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.text.Component;

public interface Weather {

	Component getSun(ServerWorld world);

	Component getRain(ServerWorld world);

	Component getThunder(ServerWorld world);

}
