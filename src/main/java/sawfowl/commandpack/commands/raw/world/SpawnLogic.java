package sawfowl.commandpack.commands.raw.world;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class SpawnLogic extends AbstractWorldCommand {

	public SpawnLogic(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		ServerWorld world = getWorld(args, 0).get();
		boolean enable = getBoolean(args, 1).get();
		world.properties().setPerformsSpawnLogic(enable);
		audience.sendMessage(TextUtils.replace(getText(locale, enable ? LocalesPaths.COMMANDS_WORLD_SPAWN_LOGIC_ENABLE : LocalesPaths.COMMANDS_WORLD_SPAWN_LOGIC_DISABLE), Placeholders.WORLD, world.key().asString()));
	}

	@Override
	public Component shortDescription(Locale locale) {
		return null;
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return null;
	}

	@Override
	public String command() {
		return "spawnlogic";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/world spawnlogic <world> <Value>").clickEvent(ClickEvent.suggestCommand("/world spawnlogic"));
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			RawArguments.createWorldArgument(false, false, 0, null, LocalesPaths.COMMANDS_EXCEPTION_WORLD_NOT_PRESENT),
			RawArguments.createBooleanArgument(false, false, 1, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

}
