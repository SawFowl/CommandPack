package sawfowl.commandpack.commands.raw.world;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawArgument;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Teleport extends AbstractWorldCommand {

	public Teleport(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		if(args.length == 0) exceptionAppendUsage(cause, locale, LocalesPaths.COMMANDS_EXCEPTION_WORLD_NOT_PRESENT);
		Optional<ServerWorld> optWorld = Sponge.server().worldManager().world(ResourceKey.resolve(args[0]));
		if(!optWorld.isPresent()) exceptionAppendUsage(cause, locale, LocalesPaths.COMMANDS_EXCEPTION_WORLD_NOT_PRESENT);
		Optional<ServerPlayer> player = args.length == 2 ? Sponge.server().player(args[1]) : Optional.empty();
		ServerLocation location = findSafe(ServerLocation.of(optWorld.get(), optWorld.get().properties().spawnPosition()));
		if(isPlayer) {
			if(player.isPresent()) {
				if(!player.get().uniqueId().equals(((ServerPlayer) audience).uniqueId())) {
					player.get().setLocation(location);
					player.get().sendMessage(TextUtils.replace(getText(player.get().locale(), LocalesPaths.COMMANDS_WORLD_TELEPORT), Placeholders.WORLD, optWorld.get().key().asString()));
					audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_TELEPORT_OTHER), new String[] {Placeholders.PLAYER, Placeholders.WORLD}, new Object[] {player.get().name(), optWorld.get().key().asString()}));
				} else {
					delay(((ServerPlayer) audience), locale, consumer -> {
						((ServerPlayer) audience).setLocation(location);
						audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_TELEPORT), Placeholders.WORLD, optWorld.get().key().asString()));
					});
				}
			} else {
				delay(((ServerPlayer) audience), locale, consumer -> {
					((ServerPlayer) audience).setLocation(location);
					audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_TELEPORT), Placeholders.WORLD, optWorld.get().key().asString()));
				});
			}
		} else {
			if(!player.isPresent()) exceptionAppendUsage(cause, locale, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT);
			player.get().setLocation(location);
			player.get().sendMessage(TextUtils.replace(getText(player.get().locale(), LocalesPaths.COMMANDS_WORLD_TELEPORT), Placeholders.WORLD, optWorld.get().key().asString()));
			audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_TELEPORT_OTHER), new String[] {Placeholders.PLAYER, Placeholders.WORLD}, new Object[] {player.get().name(), optWorld.get().key().asString()}));
		}
	}

	@Override
	public List<CommandCompletion> complete(CommandCause cause, List<String> args, Mutable arguments, String currentInput) throws CommandException {
		if(!plugin.getMainConfig().isAutoCompleteRawCommands()) return getEmptyCompletion();
		if(args.isEmpty()) return Sponge.server().worldManager().worlds().stream().map(ServerWorld::key).map(ResourceKey::asString).map(CommandCompletion::of).collect(Collectors.toList());
		if(args.size() == 1) {
			if(currentInput.endsWith(" ")) {
				return Sponge.server().onlinePlayers().stream().map(ServerPlayer::name).map(CommandCompletion::of).collect(Collectors.toList());
			} else return Sponge.server().worldManager().worlds().stream().map(ServerWorld::key).map(ResourceKey::asString).filter(w -> (w.startsWith(args.get(0)) || (w.contains(":") && !w.endsWith(":") && w.split(":")[1].startsWith(args.get(0))) || (args.get(0).contains(w) && !args.get(0).contains(w + " ")))).map(w -> CommandCompletion.of(w, text("&3World"))).collect(Collectors.toList());
		}
		if(args.size() == 2 && !currentInput.endsWith(" ")) return Sponge.server().onlinePlayers().stream().map(ServerPlayer::name).filter(p -> (p.startsWith(args.get(1)))).map(p -> CommandCompletion.of(p, text("&3Player"))).collect(Collectors.toList());
		return getEmptyCompletion();
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
		return "tp";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/world teleport <world>");
	}

	private ServerLocation findSafe(ServerLocation location) {
		if(location.block().type().equals(BlockTypes.AIR.get())) {
			for(double y = location.y(); y > 5; y--) {
				ServerLocation find = ServerLocation.of(location.world(), location.x(), y, location.z());
				if(isSafe(find) && !find.block().type().equals(BlockTypes.BEDROCK.get())) return find;
			}
		} else {
			for(double y = location.y(); y < location.world().height(); y++) {
				ServerLocation find = ServerLocation.of(location.world(), location.x(), y, location.z());
				if(isSafe(find) && !find.block().type().equals(BlockTypes.BEDROCK.get())) return find;
			}
		}
		return location;
	}

	private boolean isSafe(ServerLocation location) {
		return location.block().type().equals(BlockTypes.AIR.get()) && ServerLocation.of(location.world(), location.x(), location.y() + 1, location.z()).block().type().equals(BlockTypes.AIR.get()) && !ServerLocation.of(location.world(), location.x(), location.y() - 1, location.z()).block().type().equals(BlockTypes.AIR.get());
	}

	@Override
	public List<RawArgument<?>> getArguments() {
		return null;
	}

}
