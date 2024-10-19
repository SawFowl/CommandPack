package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.world;

import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.text.Component;

public interface GameRule {

	Component getSuccess(String gamerule, ServerWorld world, Object value);

	Component getList(String value);

	Component getIncorrectValue(String value);

	Component getUnknownType(String gamerule);

}
