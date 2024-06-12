package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.serverstat;

import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.text.Component;

public interface Worlds {

	Component getTitle();

	Component getWorldInfo(ServerWorld world, Component tps, Component ticks);

}
