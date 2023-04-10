package sawfowl.commandpack.commands.raw;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Warp extends AbstractRawCommand {

	public Warp(CommandPack plugin) {
		super(plugin);
	}

	@Override
	protected void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args) throws CommandException {
		if(args.length == 0) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_NAME_NOT_PRESENT);
		Optional<ServerPlayer> optTarget = cause.hasPermission(Permissions.WARP_STAFF) && args.length >= 2 ? getPlayer(args[args.length - 1]) : Optional.empty();
		if(optTarget.isPresent()) args = Arrays.copyOf(args, args.length - 1);
		String warpName = "";
		for(String string : args) warpName = warpName.isEmpty() ? string : warpName + " " + string;
		String find = warpName;
		Optional<sawfowl.commandpack.api.data.player.Warp> optWarp = plugin.getPlayersData().getAdminWarp(warpName, predicate -> cause.hasPermission(Permissions.getWarpPermission(find)));
		Optional<sawfowl.commandpack.api.data.player.Warp> optPlayerWarp = plugin.getPlayersData().getWarp(warpName, warp -> (!warp.isPrivate() || cause.hasPermission(Permissions.WARP_STAFF) || (isPlayer && plugin.getPlayersData().getOrCreatePlayerData((ServerPlayer) cause.audience()).containsWarp(warp.getName()))));
		if(!optWarp.isPresent() && !optPlayerWarp.isPresent()) exception(locale, LocalesPaths.COMMANDS_WARP_NOT_FOUND);
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) audience;
			if(optTarget.isPresent()) {
				boolean equal = optTarget.get().uniqueId().equals(player.uniqueId());
				if(optWarp.isPresent()) {
					if(!equal) {
						optWarp.get().moveToThis(optTarget.get());
						player.sendMessage(TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_WARP_SUCCESS_STAFF), new String[] {Placeholders.PLAYER, Placeholders.WARP}, new Component[] {text(optTarget.get().name()), optWarp.get().asComponent()}));
						optTarget.get().sendMessage(TextUtils.replace(getText(optTarget.get().locale(), LocalesPaths.COMMANDS_WARP_SUCCESS_OTHER), Placeholders.WARP, optWarp.get().asComponent()));
					} else {
						delay(player, locale, consumer -> {
							optWarp.get().moveToThis(player);
							player.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WARP_SUCCESS), Placeholders.WARP, optWarp.get().asComponent()));
						});
					}
				} else {
					if(!equal) {
						optPlayerWarp.get().moveToThis(optTarget.get());
						player.sendMessage(TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_WARP_SUCCESS_STAFF), new String[] {Placeholders.PLAYER, Placeholders.WARP}, new Component[] {text(optTarget.get().name()), optPlayerWarp.get().asComponent()}));
						optTarget.get().sendMessage(TextUtils.replace(getText(optTarget.get().locale(), LocalesPaths.COMMANDS_WARP_SUCCESS_OTHER), Placeholders.WARP, optPlayerWarp.get().asComponent()));
					} else {
						delay(player, locale, consumer -> {
							optPlayerWarp.get().moveToThis(player);
							player.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WARP_SUCCESS), Placeholders.WARP, optPlayerWarp.get().asComponent()));
						});
					}
				}
			} else {
				delay(player, locale, consumer -> {
					if(optWarp.isPresent()) {
						optWarp.get().moveToThis(player);
						player.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WARP_SUCCESS), Placeholders.WARP, optWarp.get().asComponent()));
					} else {
						optPlayerWarp.get().moveToThis(player);
						player.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WARP_SUCCESS), Placeholders.WARP, optPlayerWarp.get().asComponent()));
					}
				});
			}
		} else {
			if(args.length == 1) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT);
			if(!optTarget.isPresent()) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT);
			if(optWarp.isPresent()) {
				optWarp.get().moveToThis(optTarget.get());
				audience.sendMessage(TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_WARP_SUCCESS_STAFF), new String[] {Placeholders.PLAYER, Placeholders.WARP}, new Component[] {text(optTarget.get().name()), optWarp.get().asComponent()}));
				optTarget.get().sendMessage(TextUtils.replace(getText(optTarget.get().locale(), LocalesPaths.COMMANDS_WARP_SUCCESS_OTHER), Placeholders.WARP, optWarp.get().asComponent()));
			} else {
				optPlayerWarp.get().moveToThis(optTarget.get());
				audience.sendMessage(TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_WARP_SUCCESS_STAFF), new String[] {Placeholders.PLAYER, Placeholders.WARP}, new Component[] {text(optTarget.get().name()), optPlayerWarp.get().asComponent()}));
				optTarget.get().sendMessage(TextUtils.replace(getText(optTarget.get().locale(), LocalesPaths.COMMANDS_WARP_SUCCESS_OTHER), Placeholders.WARP, optPlayerWarp.get().asComponent()));
			}
		}
	}

	@Override
	protected List<String> complete(CommandCause cause, List<String> args, String plainArg) throws CommandException {
		if(!plugin.getMainConfig().isAutoCompleteRawCommands() || (plugin.getPlayersData().getAdminWarps().isEmpty() && plugin.getPlayersData().getPlayersWarps().isEmpty())) return null;
		boolean isStaff = cause.hasPermission(Permissions.WARP_STAFF);
		List<String> warps = plugin.getPlayersData().getAdminWarps().keySet().stream().filter(warp -> ((plainArg.isEmpty() || warp.startsWith(plainArg) || (isStaff && args.size() > 1 && warp.startsWith(plainArg.replace(" " + args.get(args.size() - 1), "")))) && (isStaff || cause.hasPermission(Permissions.getWarpPermission(warp))))).collect(Collectors.toList());
		warps.addAll(plugin.getPlayersData().getPlayersWarps().stream().filter(warp -> (!warp.isPrivate() || isStaff)).map(sawfowl.commandpack.api.data.player.Warp::getPlainName).filter(warp -> (plainArg.isEmpty() || warp.startsWith(plainArg) || (isStaff && args.size() > 1 && warp.startsWith(plainArg.replace(" " + args.get(args.size() - 1), ""))))).collect(Collectors.toList()));
		if(args.size() == 0) return warps;
		if(isStaff) {
			List<String> players = Sponge.server().onlinePlayers().stream().map(ServerPlayer::name).filter(name -> (name.startsWith(args.get(args.size() - 1)))).collect(Collectors.toList());
			if(args.size() > 1 && !players.isEmpty()) {
				return players;
			} else {
				return warps.stream().filter(warp -> (warp.startsWith(plainArg))).collect(Collectors.toList());
			}
		} else {
			return warps.stream().filter(warp -> (warp.startsWith(plainArg))).collect(Collectors.toList());
		}
	}

	@Override
	protected Predicate<AbstractRawCommand> canExecuteRaw(CommandCause cause) {
		return predicate -> true;
	}

	@Override
	protected Component shortDescription(Locale locale) {
		return text("Warp command.");
	}

	@Override
	protected Component extendedDescription(Locale locale) {
		return text("Teleport to a warp point.");
	}

	@Override
	protected String permission() {
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

}
