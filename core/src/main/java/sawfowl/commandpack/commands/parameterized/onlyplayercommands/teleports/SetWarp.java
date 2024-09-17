package sawfowl.commandpack.commands.parameterized.onlyplayercommands.teleports;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.miscellaneous.Location;
import sawfowl.commandpack.api.data.player.PlayerData;
import sawfowl.commandpack.api.data.player.Warp;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

@Register
public class SetWarp extends AbstractPlayerCommand {

	public SetWarp(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		delay(src, locale, consumer -> {
			PlayerData playerData = plugin.getPlayersData().getOrCreatePlayerData(src);
			String name = getString(context, "Warp", src.name());
			if(plugin.getPlayersData().streamAllWarps().filter(warp -> warp.getPlainName().equalsIgnoreCase(TextUtils.clearDecorations(name))).findFirst().isPresent()) exception(locale, LocalesPaths.COMMANDS_SETWARP_EXIST);
			Warp warp = Warp.of(name, Location.of(src), getBoolean(context, "Private", false));
			if(getBoolean(context, "Admin", false)) {
				plugin.getPlayersData().addAndSaveAdminWarp(warp);
				src.sendMessage(getComponent(locale, LocalesPaths.COMMANDS_SETWARP_SUCCESS_ADMIN));
			} else if(playerData.addWarp(warp, Permissions.getWarpsLimit(src))) {
				playerData.save();
				src.sendMessage(getComponent(locale, LocalesPaths.COMMANDS_SETWARP_SUCCESS));
			} else exception(locale, LocalesPaths.COMMANDS_SETWARP_LIMIT);
		});
	}

	@Override
	public Parameterized build() {
		return Command.builder()
				.executionRequirements(cause -> (
					cause.audience() instanceof ServerPlayer && cause.hasPermission(permission()))
				)
				.executor(this)
				.addParameter(CommandParameters.createString("Warp", false))
				.addParameter(CommandParameters.createBoolean("Private", false))
				.addParameter(CommandParameters.createBoolean("Admin", Permissions.WARP_STAFF, true))
				.build();
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
