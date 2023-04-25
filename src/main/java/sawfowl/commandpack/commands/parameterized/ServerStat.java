package sawfowl.commandpack.commands.parameterized;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
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
				
			});
		} else {
			
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
		List<Component> statList = new ArrayList<>();
		Component buttons = getButtons(src, locale, isPlayer);
		
		
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
				
			});
			buttons.append(plugins);
		}
		if(plugin.isForgeServer() && (!isPlayer || ((ServerPlayer) src).hasPermission(Permissions.SERVER_STAT_STAFF_INFO_MODS))) {
			Component mods = TextUtils.createCallBack(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_BUTTON_MODS), () -> {
				
			});
			buttons.append(mods);
		}
		return buttons;
	}

}
