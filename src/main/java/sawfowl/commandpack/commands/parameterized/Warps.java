package sawfowl.commandpack.commands.parameterized;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.adventure.SpongeComponents;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.pagination.PaginationList;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.player.Warp;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.parameterized.settings.ParameterSettings;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.configs.commands.CommandSettings;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Warps extends AbstractParameterizedCommand {

	public Warps(CommandPack plugin, String command, CommandSettings commandSettings) {
		super(plugin, command, commandSettings);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(plugin.getPlayersData().getAdminWarps().isEmpty() && plugin.getPlayersData().getPlayersWarps().isEmpty()) exception(locale, LocalesPaths.COMMANDS_WARPS_EMPTY);
		if(!isPlayer) {
			src.sendMessage(getText(locale, LocalesPaths.COMMANDS_WARPS_WAIT));
			Sponge.asyncScheduler().executor(plugin.getPluginContainer()).execute(() -> {
				String warps = "";
				if(!plugin.getPlayersData().getAdminWarps().isEmpty()) for(String warp : plugin.getPlayersData().getAdminWarps().keySet()) warps = warps.length() == 0 ? "&e" + warp : warps + "&f, &e" + warp;
				if(!plugin.getPlayersData().getPlayersWarps().isEmpty()) for(Warp warp : plugin.getPlayersData().getPlayersWarps()) warps = warps.length() == 0 ? "&e" + warp.getPlainName() : warps + "&f, &e" + warp.getPlainName();
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_WARPS_LIST).append(text(warps)));
			});
			return;
		}
		ServerPlayer player = (ServerPlayer) src;
		delay(player, locale, consumer -> {
			player.sendMessage(getText(player, LocalesPaths.COMMANDS_WARPS_WAIT));
			List<Component> serverWarps = getServerWarps(player);
			List<Component> playersWarps = getPlayersWarps(player);
			Sponge.asyncScheduler().executor(plugin.getPluginContainer()).execute(() -> {
				if(serverWarps.isEmpty()) {
					if(playersWarps.isEmpty()) {
						player.sendMessage(getText(locale, LocalesPaths.COMMANDS_WARPS_EMPTY));
						return;
					}
					sendPlayersWarps(player, playersWarps);
				} else sendServerWarps(player, serverWarps);
			});
		});
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

	@Override
	protected String permission() {
		return Permissions.WARPS;
	}

	private void sendServerWarps(ServerPlayer player, List<Component> warps) {
		Component header = TextUtils.replaceToComponents(getText(player.locale(), LocalesPaths.COMMANDS_WARPS_HEADER), new String[] {Placeholders.SERVER, Placeholders.PLAYER}, new Component[] {getText(player.locale(), LocalesPaths.COMMANDS_WARPS_SERVER_GROUP), getText(player.locale(), LocalesPaths.COMMANDS_WARPS_PLAYERS_GROUP).clickEvent(SpongeComponents.executeCallback(cause -> {
			Sponge.asyncScheduler().executor(plugin.getPluginContainer()).execute(() -> {
				player.sendMessage(getText(player, LocalesPaths.COMMANDS_WARPS_WAIT));
				List<Component> playersWarps = getPlayersWarps(player);
				if(!playersWarps.isEmpty()) {
					sendPlayersWarps(player, playersWarps);
				} else player.sendMessage(getText(player.locale(), LocalesPaths.COMMANDS_WARPS_EMPTY));
			});
		}))});
		sendList(player, header, warps);
	}

	private void sendPlayersWarps(ServerPlayer player, List<Component> warps) {
		Component header = TextUtils.replaceToComponents(getText(player.locale(), LocalesPaths.COMMANDS_WARPS_HEADER), new String[] {Placeholders.SERVER, Placeholders.PLAYER}, new Component[] {getText(player.locale(), LocalesPaths.COMMANDS_WARPS_SERVER_GROUP).clickEvent(SpongeComponents.executeCallback(cause -> {
			Sponge.asyncScheduler().executor(plugin.getPluginContainer()).execute(() -> {
				player.sendMessage(getText(player, LocalesPaths.COMMANDS_WARPS_WAIT));
				List<Component> serverWarps = getServerWarps(player);
				if(!serverWarps.isEmpty()) {
					sendServerWarps(player, serverWarps);
				} else player.sendMessage(getText(player.locale(), LocalesPaths.COMMANDS_WARPS_EMPTY));
			});
		})), getText(player.locale(), LocalesPaths.COMMANDS_WARPS_PLAYERS_GROUP)});
		sendList(player, header, warps);
	}

	private void sendList(ServerPlayer player, Component header, List<Component> warps) {
		PaginationList.builder()
		.header(header)
		.contents(warps)
		.padding(text("=").color(header.color()))
		.linesPerPage(10)
		.sendTo(player);
	}

	private List<Component> getServerWarps(ServerPlayer player) {
		List<Component> list = new ArrayList<>();
		plugin.getPlayersData().getAdminWarps().forEach((name, warp) -> {
			Component delete = !player.hasPermission(Permissions.WARP_STAFF) ? Component.empty() : getText(player.locale(), LocalesPaths.REMOVE).clickEvent(SpongeComponents.executeCallback(cause -> {
				plugin.getPlayersData().removeAdminWarp(name);
			}));
			Component teleport = player.hasPermission(Permissions.getWarpPermission(name)) || player.hasPermission(Permissions.WARP_STAFF) ? getText(player.locale(), LocalesPaths.TELEPORTCLICKABLE).clickEvent(SpongeComponents.executeCallback( cause -> {
				warp.moveToThis(player);
			})) : getText(player.locale(), LocalesPaths.TELEPORT);
			list.add(delete.append(teleport).append(warp.asComponent()));
		});
		return list;
	}

	private List<Component> getPlayersWarps(ServerPlayer player) {
		List<Component> list = new ArrayList<>();
		plugin.getPlayersData().getPlayersWarps(warp -> !warp.isPrivate() || player.hasPermission(Permissions.WARP_STAFF) || plugin.getPlayersData().getOrCreatePlayerData(player).containsWarp(warp.getPlainName())).forEach(warp -> {
			Component delete = !player.hasPermission(Permissions.WARP_STAFF) || !plugin.getPlayersData().getOrCreatePlayerData(player).containsWarp(warp.getPlainName()) ? Component.empty() : getText(player.locale(), LocalesPaths.REMOVE).clickEvent(SpongeComponents.executeCallback(cause -> {
				plugin.getPlayersData().getOrCreatePlayerData(player).removeWarp(warp.getPlainName());
			}));
			Component teleport = getText(player.locale(), LocalesPaths.TELEPORTCLICKABLE).clickEvent(SpongeComponents.executeCallback( cause -> {
				warp.moveToThis(player);
			}));
			list.add(delete.append(teleport).append(warp.asComponent()));
		});
		return list;
	}

}
