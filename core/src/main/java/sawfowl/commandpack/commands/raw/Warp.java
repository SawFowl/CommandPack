package sawfowl.commandpack.commands.raw;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

@Register
public class Warp extends AbstractRawCommand {

	public Warp(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		if(args.getInput().length == 0) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_NAME_NOT_PRESENT);
		Optional<ServerPlayer> optTarget = cause.hasPermission(Permissions.WARP_STAFF) && args.getInput().length >= 2 ? getPlayer(args.getInput()[args.getInput().length - 1]) : Optional.empty();
		String[] input = args.getInput();
		if(optTarget.isPresent()) input = Arrays.copyOf(input, input.length - 1);
		String find = "";
		for(String string : input) find = find.isEmpty() ? string : find + " " + string;
		Optional<sawfowl.commandpack.api.data.player.Warp> optWarp = args.get(0);
		if(!optWarp.isPresent()) exception(locale, LocalesPaths.COMMANDS_WARP_NOT_FOUND);
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) audience;
			if(optTarget.isPresent()) {
				boolean equal = optTarget.get().uniqueId().equals(player.uniqueId());
				if(optWarp.isPresent()) {
					if(!equal) {
						teleport(optWarp.get(), optTarget.get());
						player.sendMessage(getText(locale, LocalesPaths.COMMANDS_WARP_SUCCESS_STAFF).replace(new String[] {Placeholders.PLAYER, Placeholders.WARP}, text(optTarget.get().name()), optWarp.get().asComponent()).get());
						optTarget.get().sendMessage(getText(optTarget.get().locale(), LocalesPaths.COMMANDS_WARP_SUCCESS_OTHER).replace(Placeholders.WARP, optWarp.get().asComponent()).get());
					} else {
						delay(player, locale, consumer -> {
							plugin.getPlayersData().getTempData().setPreviousLocation(player);
							teleport(optWarp.get(), player);
							player.sendMessage(getText(locale, LocalesPaths.COMMANDS_WARP_SUCCESS).replace(Placeholders.WARP, optWarp.get().asComponent()).get());
						});
					}
				}
			} else {
				delay(player, locale, consumer -> {
					if(optWarp.isPresent()) {
						teleport(optWarp.get(), player);
						player.sendMessage(getText(locale, LocalesPaths.COMMANDS_WARP_SUCCESS).replace(Placeholders.WARP, optWarp.get().asComponent()).get());
					}
				});
			}
		} else {
			if(args.getInput().length == 1) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT);
			if(!optTarget.isPresent()) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT);
			if(optWarp.isPresent()) {
				teleport(optWarp.get(), optTarget.get());
				audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_WARP_SUCCESS_STAFF).replace(new String[] {Placeholders.PLAYER, Placeholders.WARP}, text(optTarget.get().name()), optWarp.get().asComponent()).get());
				optTarget.get().sendMessage(getText(optTarget.get().locale(), LocalesPaths.COMMANDS_WARP_SUCCESS_OTHER).replace(Placeholders.WARP, optWarp.get().asComponent()).get());
			}
		}
	}
/*
	@Override
	public List<CommandCompletion> complete(CommandCause cause, List<String> args, Mutable arguments, String currentInput) throws CommandException {
		if(!plugin.getMainConfig().isAutoCompleteRawCommands() || (plugin.getPlayersData().getAdminWarps().isEmpty() && plugin.getPlayersData().getPlayersWarps().isEmpty())) return getEmptyCompletion();
		boolean isStaff = cause.hasPermission(Permissions.WARP_STAFF);
		List<String> warps = plugin.getPlayersData().getAdminWarps().keySet().stream().filter(warp -> ((currentInput.isEmpty() || warp.startsWith(currentInput) || (isStaff && args.size() > 1 && warp.startsWith(currentInput.replace(" " + args.get(args.size() - 1), "")))) && (isStaff || cause.hasPermission(Permissions.getWarpPermission(warp))))).collect(Collectors.toList());
		warps.addAll(plugin.getPlayersData().getPlayersWarps().stream().filter(warp -> (!warp.isPrivate() || isStaff)).map(sawfowl.commandpack.api.data.player.Warp::getPlainName).filter(warp -> (currentInput.isEmpty() || warp.startsWith(currentInput) || (isStaff && args.size() > 1 && warp.startsWith(currentInput.replace(" " + args.get(args.size() - 1), ""))))).collect(Collectors.toList()));
		if(args.size() == 0) return warps.stream().map(warp -> (CommandCompletion.of(warp, text("&3Warp")))).collect(Collectors.toList());
		if(isStaff) {
			List<String> players = Sponge.server().onlinePlayers().stream().map(ServerPlayer::name).filter(name -> (name.startsWith(args.get(args.size() - 1)))).collect(Collectors.toList());
			if(args.size() > 1 && !players.isEmpty()) {
				return players.stream().map(player -> (CommandCompletion.of(player, text("&ePlayer")))).collect(Collectors.toList());
			} else {
				return warps.stream().filter(warp -> (warp.startsWith(currentInput))).map(warp -> (CommandCompletion.of(warp, text("&3Warp")))).collect(Collectors.toList());
			}
		} else {
			return warps.stream().filter(warp -> (warp.startsWith(currentInput))).map(warp -> (CommandCompletion.of(warp, text("&3Warp")))).collect(Collectors.toList());
		}
	}
*/
	@Override
	public Component shortDescription(Locale locale) {
		return text("Warp command.");
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return text("Teleport to a warp point.");
	}

	@Override
	public String permission() {
		return Permissions.WARP;
	}

	@Override
	public Component usage(CommandCause cause) {
		return cause.hasPermission(Permissions.WARP_STAFF) ? text("&c/warp <Warp> [Player]") : text("&c/warp <Warp>");
	}

	@Override
	public String command() {
		return "warp";
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			RawArguments.createWarpArgument(false, false, 0, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
			RawArguments.createPlayerArgument(true, false, 1, Permissions.WARP_STAFF, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT)
		);
	}

	private void teleport(sawfowl.commandpack.api.data.player.Warp warp, ServerPlayer player) {
		plugin.getPlayersData().getTempData().setPreviousLocation(player);
		warp.moveHere(player);
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
