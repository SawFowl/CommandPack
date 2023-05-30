package sawfowl.commandpack.commands.raw.world;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.DefaultWorldKeys;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawCompleterSupplier;
import sawfowl.commandpack.api.commands.raw.arguments.RawResultSupplier;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Unload extends AbstractWorldCommand {

	public Unload(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		ServerWorld world = getWorld(args, 0).get();
		if(!world.isLoaded()) exceptionAppendUsage(cause, TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_UNLOADED), Placeholders.WORLD, args[0]));
		for(ServerPlayer player : Sponge.server().onlinePlayers()) if(player.world().key().asString().equalsIgnoreCase(world.key().asString())) {
			if(plugin.getMainConfig().getSpawnData().isPresent()) {
				plugin.getMainConfig().getSpawnData().get().getLocationData().moveHere(player);
			} else player.setLocation(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().location(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().spawnPosition()));
		}
		Sponge.server().worldManager().unloadWorld(world).thenRunAsync(() -> {
			audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_UNLOAD), Placeholders.WORLD, args[0]));
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
		return text("&c/world unload <World>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(createWorldArgument());
	}

	private RawArgument<ServerWorld> createWorldArgument() {
		return RawArgument.of(ServerWorld.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(String[] args) {
				return Sponge.server().worldManager().worlds().stream().filter(w -> !w.key().asString().equals(DefaultWorldKeys.DEFAULT.asString())).map(w -> w.key().asString());
			}
		}, new RawResultSupplier<ServerWorld>() {

			@Override
			public Optional<ServerWorld> get(String[] args) {
				return args.length >= 1 ? Sponge.server().worldManager().world(ResourceKey.resolve(args[0])).filter(w -> !w.key().asString().equals(DefaultWorldKeys.DEFAULT.asString())) : Optional.ofNullable(null);
			}
		}, false, false, 0, LocalesPaths.COMMANDS_EXCEPTION_WORLD_NOT_PRESENT);
	}

}
