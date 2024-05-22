package sawfowl.commandpack.commands.raw.world;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.ResourceKey;
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
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Load extends AbstractWorldCommand {

	public Load(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		ResourceKey world = args.<ResourceKey>get(0).get();
		if(!Sponge.server().worldManager().offlineWorldKeys().stream().map(ResourceKey::asString).filter(k -> k.equals(world.asString())).findFirst().isPresent()) exceptionAppendUsage(cause, getText(locale, LocalesPaths.COMMANDS_WORLD_LOADED).replace(Placeholders.WORLD, world).get());
		Sponge.server().worldManager().loadWorld(world).thenRunAsync(() -> {
			audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_WORLD_LOAD).replace(Placeholders.WORLD, world).get());
		});
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
		return "load";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/world load <UnloadedWorld>").clickEvent(ClickEvent.suggestCommand("/world load"));
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(createWorldArgument());
	}

	private RawArgument<ResourceKey> createWorldArgument() {
		return RawArgument.of(
			ResourceKey.class,
			CommandTreeNodeTypes.DIMENSION.get().createNode(),
			(cause, args) -> Sponge.server().worldManager().worlds().stream().filter(w -> !w.isLoaded() && !w.key().asString().equals(DefaultWorldKeys.DEFAULT.asString())).map(w -> w.key().asString()),
			(cause, args) -> args.length >= 1 ? Sponge.server().worldManager().worlds().stream().filter(w -> !w.isLoaded() && !w.key().asString().equals(DefaultWorldKeys.DEFAULT.asString()) && w.key().asString().equals(args[0])).findFirst().map(ServerWorld::key) : Optional.empty(),
			"World",
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
