package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.text.Component;

public interface Time {

	Component getMorning(ServerWorld world);

	Component getDay(ServerWorld world);

	Component getEvening(ServerWorld world);

	Component getNight(ServerWorld world);

	Component getAdd(ServerWorld world);

}
