package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.world;

import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.text.Component;

public interface GameMode {

	Component getSurvival(ServerWorld world);

	Component getCreative(ServerWorld world);

	Component getAdventure(ServerWorld world);

	Component getSpectator(ServerWorld world);

}
