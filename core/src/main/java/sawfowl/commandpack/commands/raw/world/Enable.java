package sawfowl.commandpack.commands.raw.world;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.command.registrar.tree.CommandTreeNodeTypes;
import org.spongepowered.api.world.DefaultWorldKeys;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Enable extends AbstractWorldCommand {

	public Enable(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		ServerWorld world = getWorld(args, 0).get();
		world.properties().setLoadOnStartup(true);
		audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_WORLD_ENABLE).replace(Placeholders.WORLD, args[0]).get());
		plugin.getAPI().updateCommandTree("world");
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
		return "enable";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/world enable <World>").clickEvent(ClickEvent.suggestCommand("/world enable"));
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(createWorldArgument());
	}

	private RawArgument<ServerWorld> createWorldArgument() {
		return RawArgument.of(
			ServerWorld.class,
			CommandTreeNodeTypes.DIMENSION.get().createNode(),
			(cause, args) -> Sponge.server().worldManager().worlds().stream().filter(w -> !w.key().asString().equals(DefaultWorldKeys.DEFAULT.asString())).map(w -> w.key().asString()),
			() -> Sponge.server().worldManager().worlds().stream().filter(w -> !w.key().asString().equals(DefaultWorldKeys.DEFAULT.asString())).map(w -> w.key().asString()),
			(cause, args) -> args.length >= 1 ? Sponge.server().worldManager().worlds().stream().filter(w -> !w.key().asString().equals(DefaultWorldKeys.DEFAULT.asString()) && w.key().asString().equals(args[0])).findFirst() : Optional.empty(),
			null,
			false,
			false,
			0,
			null,
			LocalesPaths.COMMANDS_EXCEPTION_WORLD_NOT_PRESENT
		);
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
