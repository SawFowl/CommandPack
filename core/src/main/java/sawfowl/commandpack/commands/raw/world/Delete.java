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
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Delete extends AbstractWorldCommand {

	public Delete(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		ServerWorld world = getWorld(args, 0).get();
		for(ServerPlayer player : Sponge.server().onlinePlayers()) if(player.world().key().asString().equalsIgnoreCase(world.key().asString())) {
			if(plugin.getMainConfig().getSpawnData().isPresent()) {
				plugin.getMainConfig().getSpawnData().get().getLocationData().moveHere(player);
			} else player.setLocation(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().location(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().spawnPosition()));
		}
		Sponge.server().worldManager().deleteWorld(world.key()).thenRunAsync(() -> {
			audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_WORLD_DELETE).replace(Placeholders.WORLD, args[0]).get());
		});
	}

	@Override
	public Component shortDescription(Locale locale) {
		return null;
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return text("&c/world delete <World>");
	}

	@Override
	public String command() {
		return "delete";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/world delete <World>").clickEvent(ClickEvent.suggestCommand("/world delete"));
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(createWorldArgument());
	}

	private RawArgument<ServerWorld> createWorldArgument() {
		return RawArgument.of(
			ServerWorld.class,
			CommandTreeNodeTypes.RESOURCE_LOCATION.get().createNode(),
			(cause, args) -> Sponge.server().worldManager().worlds().stream().filter(w -> w.key().namespace().equals("sponge")).map(w -> w.key().asString()),
			(cause, args) -> args.length >= 1 ? Sponge.server().worldManager().worlds().stream().filter(w -> w.key().namespace().equals("sponge") && w.key().asString().equals(args[0])).findFirst() : Optional.empty(),
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
