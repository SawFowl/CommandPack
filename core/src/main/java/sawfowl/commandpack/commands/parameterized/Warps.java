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

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.player.Warp;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Warps extends AbstractParameterizedCommand {

	public Warps(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(plugin.getPlayersData().getAdminWarps().isEmpty() && plugin.getPlayersData().getPlayersWarps().isEmpty()) exception(getWarps(locale).getEmpty());
		if(!isPlayer) {
			src.sendMessage(getWarps(locale).getWait());
			Sponge.asyncScheduler().executor(plugin.getPluginContainer()).execute(() -> {
				String warps = "";
				if(!plugin.getPlayersData().getAdminWarps().isEmpty()) for(String warp : plugin.getPlayersData().getAdminWarps().keySet()) warps = warps.length() == 0 ? "&e" + warp : warps + "&f, &e" + warp;
				if(!plugin.getPlayersData().getPlayersWarps().isEmpty()) for(Warp warp : plugin.getPlayersData().getPlayersWarps()) warps = warps.length() == 0 ? "&e" + warp.getPlainName() : warps + "&f, &e" + warp.getPlainName();
				src.sendMessage(getWarps(locale).getList(plugin.getPlayersData().getAdminWarps().size() + plugin.getPlayersData().getPlayersWarps().size(), text(warps)));
			});
			return;
		}
		ServerPlayer player = (ServerPlayer) src;
		delay(player, locale, consumer -> {
			player.sendMessage(getWarps(locale).getWait());
			List<Component> serverWarps = getServerWarps(player);
			List<Component> playersWarps = getPlayersWarps(player);
			Sponge.asyncScheduler().executor(plugin.getPluginContainer()).execute(() -> {
				if(serverWarps.isEmpty()) {
					if(playersWarps.isEmpty()) {
						player.sendMessage(getWarps(locale).getEmpty());
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
	public String permission() {
		return Permissions.WARPS;
	}

	@Override
	public String command() {
		return "warps";
	}

	private void sendServerWarps(ServerPlayer player, List<Component> warps) {
		Component header = getWarps(player).getHeader(getWarps(player).getServer(), getWarps(player).getPlayer().clickEvent(SpongeComponents.executeCallback(cause -> {
			Sponge.asyncScheduler().executor(plugin.getPluginContainer()).execute(() -> {
				player.sendMessage(getWarps(player).getWait());
				List<Component> playersWarps = getPlayersWarps(player);
				if(!playersWarps.isEmpty()) {
					sendPlayersWarps(player, playersWarps);
				} else player.sendMessage(getWarps(player).getEmpty());
			});
		})));
		sendList(player, header, warps);
	}

	private void sendPlayersWarps(ServerPlayer player, List<Component> warps) {
		Component header = getWarps(player).getHeader(getWarps(player).getServer().clickEvent(SpongeComponents.executeCallback(cause -> {
			Sponge.asyncScheduler().executor(plugin.getPluginContainer()).execute(() -> {
				player.sendMessage(getWarps(player).getWait());
				List<Component> serverWarps = getServerWarps(player);
				if(!serverWarps.isEmpty()) {
					sendServerWarps(player, serverWarps);
				} else player.sendMessage(getWarps(player).getEmpty());
			});
		})), getWarps(player).getPlayer());
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
			Component delete = !player.hasPermission(Permissions.WARP_STAFF) ? Component.empty() : plugin.getLocales().getLocale(player).getButtons().getRemove().clickEvent(SpongeComponents.executeCallback(cause -> {
				plugin.getPlayersData().removeWarp(name, null);
				sendServerWarps(player, getServerWarps(player));
			}));
			Component teleport = player.hasPermission(Permissions.getWarpPermission(name)) || player.hasPermission(Permissions.WARP_STAFF) ? plugin.getLocales().getLocale(player).getButtons().getTeleportClickable().clickEvent(SpongeComponents.executeCallback( cause -> {
				plugin.getPlayersData().getTempData().setPreviousLocation(player);
				warp.moveHere(player);
				player.sendMessage(getWarp(player).getSuccess(warp.asComponent()));
			})) : plugin.getLocales().getLocale(player).getButtons().getTeleport();
			list.add(Component.empty().append(delete).append(teleport).append(warp.asComponent()));
		});
		return list;
	}

	private List<Component> getPlayersWarps(ServerPlayer player) {
		List<Component> list = new ArrayList<>();
		plugin.getPlayersData().getPlayersData().forEach(data -> {
			data.getWarps().forEach(warp -> {
				if(!warp.isPrivate() || player.hasPermission(Permissions.WARP_STAFF) || plugin.getPlayersData().getOrCreatePlayerData(player).containsWarp(warp.getPlainName())) {
					Component delete = !player.hasPermission(Permissions.WARP_STAFF) && !plugin.getPlayersData().getOrCreatePlayerData(player).containsWarp(warp.getPlainName()) ? Component.empty() : plugin.getLocales().getLocale(player).getButtons().getRemove().clickEvent(SpongeComponents.executeCallback(cause -> {
					plugin.getPlayersData().removeWarp(warp.getPlainName(), data);
					sendPlayersWarps(player, getPlayersWarps(player));
				}));
				Component teleport = plugin.getLocales().getLocale(player).getButtons().getTeleportClickable().clickEvent(SpongeComponents.executeCallback( cause -> {
					if(data.containsWarp(warp.getName())) {
						plugin.getPlayersData().getTempData().setPreviousLocation(player);
						warp.moveHere(player);
						player.sendMessage(getWarp(player).getSuccess(warp.asComponent()));
					} else player.sendMessage(getWarp(player).getNotFound());
				}));
				list.add(Component.empty().append(delete).append(teleport).append(warp.asComponent()));
				}
			});
		});
		return list;
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Warps getWarps(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getWarps();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Warps getWarps(ServerPlayer player) {
		return getWarps(player.locale());
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Warp getWarp(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getWarp();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Warp getWarp(ServerPlayer player) {
		return getWarp(player.locale());
	}

}
