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
			boolean admin = getBoolean(context, "Admin", false);
			if(warpIsPresent(name, admin, src)) {
				if(admin) exception(plugin.getLocales().getLocale(locale).getCommands().getSetWarp().getAllreadyExist());
				plugin.getPlayersData().removeWarp(name, playerData);
			}
			Warp warp = Warp.of(name, Location.of(src), getBoolean(context, "Private", false));
			if(admin) {
				plugin.getPlayersData().addAndSaveWarp(warp, null);
				src.sendMessage(plugin.getLocales().getLocale(locale).getCommands().getSetWarp().getSuccessAdmin());
			} else if(src.hasPermission(Permissions.WARP_STAFF) || playerData.getTotalWarps() < Permissions.getWarpsLimit(src)) {
				plugin.getPlayersData().addAndSaveWarp(warp, playerData);
				src.sendMessage(plugin.getLocales().getLocale(locale).getCommands().getSetWarp().getSuccess());
			} else exception(plugin.getLocales().getLocale(locale).getCommands().getSetWarp().getLimit(Permissions.getWarpsLimit(src)));
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
			ParameterSettings.of(CommandParameters.createString("Warp", false), false, locale -> plugin.getLocales().getLocale(locale).getCommandExceptions().getNameNotPresent()),
			ParameterSettings.of(CommandParameters.createBoolean("Private", false), false, locale -> plugin.getLocales().getLocale(locale).getCommandExceptions().getBooleanNotPresent()),
			ParameterSettings.of(CommandParameters.createBoolean("Admin", Permissions.WARP_STAFF, true), true, locale -> plugin.getLocales().getLocale(locale).getCommandExceptions().getBooleanNotPresent())
		);
	}

	@Override
	public String command() {
		return "setwarp";
	}

	private boolean warpIsPresent(String name, boolean isAdmin, ServerPlayer src) {
		return isAdmin ? plugin.getPlayersData().getAdminWarps().containsKey(TextUtils.clearDecorations(name)) : plugin.getPlayersData().getOrCreatePlayerData(src).containsWarp(TextUtils.clearDecorations(name));
	}

}
