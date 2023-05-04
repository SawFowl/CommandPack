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

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractInfoCommand;
import sawfowl.commandpack.commands.parameterized.serverstat.Mods;
import sawfowl.commandpack.commands.parameterized.serverstat.Plugins;
import sawfowl.commandpack.commands.parameterized.serverstat.ServerTime;
import sawfowl.commandpack.commands.parameterized.serverstat.Tps;
import sawfowl.commandpack.commands.parameterized.serverstat.Worlds;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

import sawfowl.localeapi.api.TextUtils;

public class ServerStat extends AbstractInfoCommand {

	private final Plugins plugins;
	private final Parameterized pluginsCommand;
	private  Mods mods;
	private Parameterized modsCommand;
	private final Tps tps;
	private final Parameterized tpsCommand;
	private final ServerTime time;
	private final Parameterized timeCommand;
	public ServerStat(CommandPack plugin) {
		super(plugin);
		plugins = new Plugins(plugin);
		pluginsCommand = plugins.build();
		mods = new Mods(plugin);
		modsCommand = mods.build();
		this.tps = new Tps(plugin);
		this.tpsCommand = tps.build();
		this.time = new ServerTime(plugin);
		this.timeCommand = time.build();
		fillLists();
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
		return (plugin.isForgeServer() ? builder().addChild(modsCommand, "mods") : builder())
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
		if(plugin.isForgeServer()) mods.register(event);
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
		Component header = getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_HEADER);
		List<Component> statList = new ArrayList<>();
		Component buttons = getButtons(src, locale, isPlayer);
		if(TextUtils.serializeLegacy(buttons).length() > 0) statList.add(buttons.append(Component.newline()));
		statList.add(getTPS(locale));
		statList.add(getUptime(locale));
		statList.add(getServerTime(locale));
		statList.add(Component.empty());
		long max = Runtime.getRuntime().maxMemory() / 1024 / 1024;
		long total = Runtime.getRuntime().totalMemory() / 1024 / 1024;
		statList.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MEMORY_MAX), Placeholders.VALUE, text(max)));
		statList.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MEMORY_ALLOCATED), Placeholders.VALUE, text(total)));
		long free = Runtime.getRuntime().freeMemory() / 1024 / 1024;
		long utilised = total - free;
		statList.add(TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MEMORY_UTILISED), 
				new  String[] {Placeholders.VALUE, Placeholders.FROM_ALLOCATED, Placeholders.FROM_MAX}, 
				new Component[] {text(utilised), text((utilised * 100)/total), text((utilised * 100)/max)}
			));
		statList.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MEMORY_FREE), Placeholders.VALUE, text(Runtime.getRuntime().freeMemory() / 1024 / 1024)));
		sendPaginationList(src, header, Component.text("=").color(header.color()), linesPerPage, statList);
	}

	private Component getButtons(Audience src, Locale locale, boolean isPlayer) {
		Component buttons = Component.empty();
		if(isPlayer && ((ServerPlayer) src).hasPermission(Permissions.SERVER_STAT_STAFF_INFO_SYSTEM)) {
			Component system = TextUtils.createCallBack(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_BUTTON_SYSTEM), () -> {
				sendSystemInfo(src, locale, isPlayer);
			});
			buttons = buttons.append(system).append(text("&r "));
		}
		if(isPlayer && ((ServerPlayer) src).hasPermission(Permissions.SERVER_STAT_STAFF_INFO_WORLDS)) {
			Component worlds = TextUtils.createCallBack(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_BUTTON_WORLDS), () -> {
				sendWorldsInfo(src, locale);
			});
			buttons = buttons.append(worlds).append(text("&r "));
		}
		if(isPlayer && ((ServerPlayer) src).hasPermission(Permissions.SERVER_STAT_STAFF_PLUGINS_LIST)) {
			Component plugins = TextUtils.createCallBack(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_BUTTON_PLUGINS), () -> {
				sendPluginsInfo(src, locale, isPlayer);
			});
			buttons = buttons.append(plugins);
		}
		if(plugin.isForgeServer() && isPlayer && ((ServerPlayer) src).hasPermission(Permissions.SERVER_STAT_STAFF_MODS_LIST)) {
			Component mods = TextUtils.createCallBack(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_BUTTON_MODS), () -> {
				sendModsInfo(src, locale, isPlayer);
			});
			buttons = buttons.append(text("&r ")).append(mods);
		}
		return buttons;
	}

}
