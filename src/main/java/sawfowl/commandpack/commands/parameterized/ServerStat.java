package sawfowl.commandpack.commands.parameterized;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractInfoCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class ServerStat extends AbstractInfoCommand {

	public ServerStat(CommandPack plugin) {
		super(plugin);
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
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.SERVER_STAT;
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
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getString(locale, LocalesPaths.COMMANDS_SERVERSTAT_TIMEFORMAT));
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(System.currentTimeMillis());
		statList.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_TPS), Placeholders.VALUE, tPStoText(currentTPS())
				.append(text("&f, "))
				.append(tPStoText(plugin.getAverageTPS1m()))
				.append(text("&f, "))
				.append(tPStoText(plugin.getAverageTPS5m()))
				.append(text("&f, "))
				.append(tPStoText(plugin.getAverageTPS10m()))
				));
		statList.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_UPTIME), Placeholders.VALUE, getUptime(locale)));
		statList.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_TIME), Placeholders.VALUE, text(format.format(calendar.getTime()))));
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
		if(isPlayer && ((ServerPlayer) src).hasPermission(Permissions.SERVER_STAT_STAFF_INFO_PLUGINS)) {
			Component plugins = TextUtils.createCallBack(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_BUTTON_PLUGINS), () -> {
				sendPluginsInfo(src, locale, isPlayer);
			});
			buttons = buttons.append(plugins);
		}
		return buttons;
	}

	private double currentTPS() {
		return BigDecimal.valueOf(Sponge.server().ticksPerSecond()).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	private Component getUptime(Locale locale) {
		//Component uptime = timeFormat(plugin.getServerUptime(), locale).append(Component.text(" ")).append(timeFormat(ManagementFactory.getRuntimeMXBean().getUptime(), locale));
		//Duration uptimeJVM = Duration.ofMillis(ManagementFactory.getRuntimeMXBean().getUptime());
		return timeFormat(plugin.getServerUptime(), locale).append(Component.text(" / ")).append(timeFormat(ManagementFactory.getRuntimeMXBean().getUptime() / 1000, locale));
	}

}
