package sawfowl.commandpack.commands.parameterized;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.ArrayList;
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
import sawfowl.commandpack.api.data.commands.parameterized.AbstractInfoCommand;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
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
		if(TextUtils.serializeLegacy(buttons).length() > 0) statList.add(buttons);
		statList.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_TPS), Placeholders.VALUE, tPStoText(currentTPS())
				.append(text("&f, "))
				.append(tPStoText(plugin.getAverageTPS1m()))
				.append(text("&f, "))
				.append(tPStoText(plugin.getAverageTPS5m()))
				.append(text("&f, "))
				.append(tPStoText(plugin.getAverageTPS10m()))
				));
		statList.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_UPTIME), Placeholders.VALUE, getUptime(locale)));
		sendPaginationList(src, buttons, Component.text("=").color(header.color()), linesPerPage, statList);
	}

	private Component getButtons(Audience src, Locale locale, boolean isPlayer) {
		Component buttons = Component.empty();
		if(!isPlayer || ((ServerPlayer) src).hasPermission(Permissions.SERVER_STAT_STAFF_INFO_SYSTEM)) {
			Component system = TextUtils.createCallBack(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_BUTTON_SYSTEM), () -> {
				sendSystemInfo(src, locale, isPlayer);
			});
			buttons.append(system);
		}
		if(!isPlayer || ((ServerPlayer) src).hasPermission(Permissions.SERVER_STAT_STAFF_INFO_WORLDS)) {
			Component worlds = TextUtils.createCallBack(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_BUTTON_WORLDS), () -> {
				sendWorldsInfo(src, locale);
			});
			buttons.append(worlds);
		}
		if(!isPlayer || ((ServerPlayer) src).hasPermission(Permissions.SERVER_STAT_STAFF_INFO_PLUGINS)) {
			Component plugins = TextUtils.createCallBack(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_BUTTON_PLUGINS), () -> {
				sendPluginsInfo(src, locale, isPlayer);
			});
			buttons.append(plugins);
		}
		if(plugin.isForgeServer() && (!isPlayer || ((ServerPlayer) src).hasPermission(Permissions.SERVER_STAT_STAFF_INFO_MODS))) {
			Component mods = TextUtils.createCallBack(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_BUTTON_MODS), () -> {
				sendModsInfo(src, locale, isPlayer);
			});
			buttons.append(mods);
		}
		return buttons;
	}

	private double currentTPS() {
		return Sponge.server().ticksPerSecond();
	}

	private Component tPStoText(double tps) {
		if(tps < 10) return text("&4" + tps);
		if(tps < 15) return text("&c" + tps);
		if(tps < 17) return text("&e" + tps);
		if(tps < 20) return text("&a" + tps);
		return text("&2" + tps);
	}

	private Component getUptime(Locale locale) {
		final Duration uptime = Duration.ofMillis(ManagementFactory.getRuntimeMXBean().getUptime());
		
		return Component.empty();
	}

}
