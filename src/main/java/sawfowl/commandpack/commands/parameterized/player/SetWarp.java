package sawfowl.commandpack.commands.parameterized.player;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.miscellaneous.Location;
import sawfowl.commandpack.api.data.player.PlayerData;
import sawfowl.commandpack.api.data.player.Warp;
import sawfowl.commandpack.commands.CommandParameters;
import sawfowl.commandpack.commands.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.configs.commands.CommandSettings;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class SetWarp extends AbstractPlayerCommand {

	public SetWarp(CommandPack plugin, String command, CommandSettings commandSettings) {
		super(plugin, command, commandSettings);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		delay(src, locale, consumer -> {
			PlayerData playerData = plugin.getPlayersData().getOrCreatePlayerData(src);
			String name = getString(context, "Warp", src.name());
			if(name.equalsIgnoreCase("list")) name = src.name();
			Warp warp = getBoolean(context, "Admin", false) ? Warp.create(name, Location.create(src)) : Warp.create(name, Location.create(src)).setPrivate(getBoolean(context, "Private", false));
			if(getBoolean(context, "Admin", false)) {
				plugin.getPlayersData().addAdminWarp(warp);
			} else if(playerData.addWarp(warp, Permissions.getWarpsLimit(src))) {
				src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SETHOME_SUCCESS), Placeholders.HOME, warp.asComponent()));
				playerData.save();
			} else exception(locale, LocalesPaths.COMMANDS_SETHOME_LIMIT);
		});
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.WARP;
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(
					new ParameterSettings(CommandParameters.createString("Warp", false), false, LocalesPaths.COMMANDS_EXCEPTION_NAME_NOT_PRESENT),
					new ParameterSettings(CommandParameters.createBoolean("Admin", Permissions.WARP_STAFF, true), true, LocalesPaths.COMMANDS_EXCEPTION_BOOLEAN_NOT_PRESENT),
					new ParameterSettings(CommandParameters.createBoolean("Private", true), true, LocalesPaths.COMMANDS_EXCEPTION_BOOLEAN_NOT_PRESENT)
				);
	}

}
