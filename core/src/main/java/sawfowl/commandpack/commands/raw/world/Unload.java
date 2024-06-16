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
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.DefaultWorldKeys;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentData;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.commands.raw.arguments.RawOptional;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;

public class Unload extends AbstractWorldCommand {

	public Unload(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		ServerWorld world = args.getWorld(0).get();
		if(!world.isLoaded()) exceptionAppendUsage(cause, getWorld(locale).getNotLoaded(world.key().asString()));
		for(ServerPlayer player : Sponge.server().onlinePlayers()) if(player.world().key().asString().equalsIgnoreCase(world.key().asString())) {
			if(plugin.getMainConfig().getSpawnData().isPresent()) {
				plugin.getMainConfig().getSpawnData().get().getLocationData().moveHere(player);
			} else player.setLocation(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().location(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().spawnPosition()));
		}
		Sponge.server().worldManager().unloadWorld(world).thenRunAsync(() -> {
			audience.sendMessage(getWorld(locale).getUnload(world.key().asString()));
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
		return "unload";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/world unload <World>").clickEvent(ClickEvent.suggestCommand("/world unload"));
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(createWorldArgument());
	}

	private RawArgument<ServerWorld> createWorldArgument() {
		return RawArgument.of(
			ServerWorld.class,
			(cause, args) -> Sponge.server().worldManager().worlds().stream().filter(w -> w.isLoaded() && !w.key().asString().equals(DefaultWorldKeys.DEFAULT.asString())).map(w -> w.key().asString()),
			(cause, args) -> args.length >= 1 ? Sponge.server().worldManager().worlds().stream().filter(w -> w.isLoaded() && !w.key().asString().equals(DefaultWorldKeys.DEFAULT.asString()) && w.key().asString().equals(args[0])).findFirst() : Optional.empty(),
			new RawArgumentData<>("World", CommandTreeNodeTypes.DIMENSION.get().createNode(), 0, null, null),
			RawOptional.notOptional(),
			locale -> getExceptions(locale).getWorldNotPresent()
		);
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
