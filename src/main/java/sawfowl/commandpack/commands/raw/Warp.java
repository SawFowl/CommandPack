package sawfowl.commandpack.commands.raw;

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
import sawfowl.commandpack.configure.configs.commands.CommandSettings;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Warp extends AbstractRawCommand {

	public Warp(CommandPack plugin, String command, CommandSettings commandSettings) {
		super(plugin, command, commandSettings);
	}

	@Override
	protected void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args) throws CommandException {
		if(args.length == 0) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_NAME_NOT_PRESENT);
		Optional<sawfowl.commandpack.api.data.player.Warp> optWarp = plugin.getPlayersData().getAdminWarp(args[0], predicate -> cause.hasPermission(Permissions.getWarpPermission(args[0])));
		Optional<sawfowl.commandpack.api.data.player.Warp> optPlayerWarp = plugin.getPlayersData().getWarp(args[0], warp -> (!warp.isPrivate() || cause.hasPermission(Permissions.WARP_STAFF) || (isPlayer && plugin.getPlayersData().getOrCreatePlayerData((ServerPlayer) cause.audience()).containsWarp(warp.getName()))));
		if(!optWarp.isPresent() && !optPlayerWarp.isPresent()) exception(locale, LocalesPaths.COMMANDS_WARP_NOT_FOUND);
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) audience;
			if(player.hasPermission(Permissions.WARP_STAFF) && args.length >= 2) {
				Optional<ServerPlayer> optTarget = getPlayer(args[1]);
				if(!optTarget.isPresent()) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT);
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
			Optional<ServerPlayer> optTarget = getPlayer(args[1]);
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
		List<String> warps = plugin.getPlayersData().getAdminWarps().keySet().stream().filter(warp -> (cause.hasPermission(Permissions.getWarpPermission(warp)))).collect(Collectors.toList());
		warps.addAll(plugin.getPlayersData().getPlayersWarps().stream().filter(warp -> (!warp.isPrivate() || cause.hasPermission(Permissions.WARP_STAFF))).map(sawfowl.commandpack.api.data.player.Warp::getPlainName).collect(Collectors.toList()));
		if(args.size() == 0) return warps;
		if(args.size() == 1 && !plainArg.contains(args.get(0) + " ")) return warps.stream().filter(warp -> (warp.startsWith(args.get(0)))).collect(Collectors.toList());
		if(args.size() == 1 && !warps.stream().filter(warp -> (warp.equals(args.get(0)))).findFirst().isPresent()) return null;
		if(args.size() == 1 && plainArg.contains(args.get(0) + " ") && cause.hasPermission(Permissions.WARP_STAFF)) return Sponge.server().onlinePlayers().stream().map(ServerPlayer::name).collect(Collectors.toList());
		if(args.size() == 2 && plainArg.contains(args.get(1) + " ")) return null;
		if(args.size() == 2 && cause.hasPermission(Permissions.WARP_STAFF) && !plainArg.contains(args.get(0) + " ")) return Sponge.server().onlinePlayers().stream().map(ServerPlayer::name).filter(player -> (player.startsWith(args.get(1)))).collect(Collectors.toList());
		if(args.size() == 2 && cause.hasPermission(Permissions.WARP_STAFF) && plainArg.contains(args.get(0) + " ") && Sponge.server().onlinePlayers().stream().map(ServerPlayer::name).filter(player -> (player.startsWith(args.get(1)))).findFirst().isPresent()) return Sponge.server().onlinePlayers().stream().map(ServerPlayer::name).filter(player -> (player.startsWith(args.get(1)))).collect(Collectors.toList());
		return null;
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

}
