package sawfowl.commandpack.commands.parameterized.player.teleports;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.miscellaneous.Location;
import sawfowl.commandpack.api.data.player.PlayerData;
import sawfowl.commandpack.api.data.player.Warp;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class SetWarp extends AbstractPlayerCommand {

	public SetWarp(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		delay(src, locale, consumer -> {
			PlayerData playerData = plugin.getPlayersData().getOrCreatePlayerData(src);
			String name = getString(context, "Warp", src.name());
			Optional<sawfowl.commandpack.api.data.player.Warp> optWarp = plugin.getPlayersData().getAdminWarp(name);
			Optional<sawfowl.commandpack.api.data.player.Warp> optPlayerWarp = plugin.getPlayersData().getWarp(name, warp -> (!warp.isPrivate() || src.hasPermission(Permissions.WARP_STAFF) || (plugin.getPlayersData().getOrCreatePlayerData(src).containsWarp(warp.getName()))));
			if((optWarp.isPresent() && !src.hasPermission(Permissions.WARP_STAFF)) || (optPlayerWarp.isPresent() && !playerData.getWarp(name).isPresent())) exception(locale, LocalesPaths.COMMANDS_SETWARP_EXIST);
			Warp warp = Warp.of(name, Location.of(src), getBoolean(context, "Private", false));
			if(getBoolean(context, "Admin", false)) {
				plugin.getPlayersData().addAndSaveAdminWarp(warp);
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_SETWARP_SUCCESS_ADMIN));
			} else if(playerData.addWarp(warp, Permissions.getWarpsLimit(src))) {
				playerData.save();
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_SETWARP_SUCCESS));
			} else exception(locale, LocalesPaths.COMMANDS_SETWARP_LIMIT);
		});
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.SET_WARP;
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(
			ParameterSettings.of(CommandParameters.createString("Warp", false), false, LocalesPaths.COMMANDS_EXCEPTION_NAME_NOT_PRESENT),
			ParameterSettings.of(CommandParameters.createBoolean("Private", false), false, LocalesPaths.COMMANDS_EXCEPTION_BOOLEAN_NOT_PRESENT),
			ParameterSettings.of(CommandParameters.createBoolean("Admin", Permissions.WARP_STAFF, true), true, LocalesPaths.COMMANDS_EXCEPTION_BOOLEAN_NOT_PRESENT)
		);
	}

	@Override
	public String command() {
		return "setwarp";
	}

}
