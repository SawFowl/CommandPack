package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.world;

import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.text.Component;

public interface GameRule {

	Component getSuccess(Component gamerule, ServerWorld world, int radius);

	Component getList(String value);

	Component getIncorrectValue(Component value);

	Component getUnknownType(Component gamerule);

}
