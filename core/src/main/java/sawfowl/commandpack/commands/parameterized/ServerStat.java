package sawfowl.commandpack.commands.parameterized;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractInfoCommand;
import sawfowl.commandpack.commands.parameterized.serverstat.Mods;
import sawfowl.commandpack.commands.parameterized.serverstat.Plugins;
import sawfowl.commandpack.commands.parameterized.serverstat.ServerTime;
import sawfowl.commandpack.commands.parameterized.serverstat.Tps;
import sawfowl.commandpack.commands.parameterized.serverstat.Worlds;
import sawfowl.commandpack.commands.settings.Register;

import sawfowl.localeapi.api.TextUtils;

@Register
public class ServerStat extends AbstractInfoCommand {

	private final Plugins plugins;
	private final Parameterized pluginsCommand;
	private Mods modsClass;
	private Parameterized modsCommand;
	private final Tps tps;
	private final Parameterized tpsCommand;
	private final ServerTime time;
	private final Parameterized timeCommand;
	public ServerStat(CommandPackInstance plugin) {
		super(plugin);
		plugins = new Plugins(plugin);
		pluginsCommand = plugins.build();
		modsClass = new Mods(plugin);
		modsCommand = modsClass.build();
		this.tps = new Tps(plugin);
		this.tpsCommand = tps.build();
		this.time = new ServerTime(plugin);
		this.timeCommand = time.build();
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			delay((ServerPlayer) src, locale, consumer -> {
				sendStat(src, locale, isPlayer);
			});
		} else {
			sendStat(src, locale, isPlayer);
		}
	}

	@Override
	public Parameterized build() {
		return builder().addChild(modsCommand, "mods")
				.addChild(new Worlds(plugin).build(), "worlds")
				.addChild(pluginsCommand, "plugins")
				.addChild(tpsCommand, "tps")
				.addChild(timeCommand, "servertime", "time")
				.build();
	}

	@Override
	public void register(RegisterCommandEvent<Parameterized> event) {
		if(getCommandSettings() == null) {
			event.register(getContainer(), build(), command());
		} else {
			if(!getCommandSettings().isEnable()) return;
			if(getCommandSettings().getAliases() != null && getCommandSettings().getAliases().length > 0) {
				event.register(getContainer(), build(), command(), getCommandSettings().getAliases());
			} else event.register(getContainer(), build(), command());
		}
		modsClass.register(event);
		plugins.register(event);
		tps.enableRegister();
		tps.register(event);
		time.enableRegister();
		time.register(event);
	}

	@Override
	public String permission() {
		return Permissions.SERVER_STAT_BASIC;
	}

	@Override
	public String command() {
		return "serverstat";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

	private void sendStat(Audience src, Locale locale, boolean isPlayer) {
		Component title = getServerStat(locale).getTitle();
		List<Component> statList = new ArrayList<>();
		Component buttons = isPlayer ? getButtons((ServerPlayer) src, locale) : null;
		if(buttons != null) statList.add(buttons.append(Component.newline()));
		statList.add(getTPS(locale));
		statList.add(getUptime(locale));
		statList.add(getServerTime(locale));
		statList.add(Component.empty());
		long max = Runtime.getRuntime().maxMemory() / 1024 / 1024;
		long total = Runtime.getRuntime().totalMemory() / 1024 / 1024;
		statList.add(getServerStat(locale).getMemory().getMax(max));
		statList.add(getServerStat(locale).getMemory().getAllocated(total));
		long free = Runtime.getRuntime().freeMemory() / 1024 / 1024;
		long utilised = total - free;
		statList.add(getServerStat(locale).getMemory().getUtilised(utilised, (utilised * 100)/total, (utilised * 100)/max));
		statList.add(getServerStat(locale).getMemory().getFree(Runtime.getRuntime().freeMemory() / 1024 / 1024));
		sendPaginationList(src, title, Component.text("=").color(title.color()), linesPerPage, statList);
	}

	private Component getButtons(ServerPlayer src, Locale locale) {
		Component buttons = Component.empty();
		if(src.hasPermission(Permissions.SERVER_STAT_STAFF_INFO_SYSTEM)) {
			Component system = TextUtils.createCallBack(getServerStat(locale).getButtons().getSystem(), cause -> {
				sendSystemInfo(src, locale, true);
			});
			buttons = buttons.append(system).append(text("&r "));
		}
		if(src.hasPermission(Permissions.SERVER_STAT_STAFF_INFO_WORLDS)) {
			Component worlds = TextUtils.createCallBack(getServerStat(locale).getButtons().getWorlds(), cause -> {
				sendWorldsInfo(src, locale);
			});
			buttons = buttons.append(worlds).append(text("&r "));
		}
		if(src.hasPermission(Permissions.SERVER_STAT_STAFF_PLUGINS_LIST)) {
			Component plugins = TextUtils.createCallBack(getServerStat(locale).getButtons().getPlugins(), cause -> {
				sendPluginsInfo(src, locale, true);
			});
			buttons = buttons.append(plugins);
		}
		if(plugin.isModifiedServer() && ((ServerPlayer) src).hasPermission(Permissions.SERVER_STAT_STAFF_MODS_LIST)) {
			Component mods = TextUtils.createCallBack(getServerStat(locale).getButtons().getMods(), cause -> {
				sendModsInfo(src, locale, true);
			});
			buttons = buttons.append(text("&r ")).append(mods);
		}
		return TextUtils.clearDecorations(buttons).equals("") ? null : buttons;
	}

}
