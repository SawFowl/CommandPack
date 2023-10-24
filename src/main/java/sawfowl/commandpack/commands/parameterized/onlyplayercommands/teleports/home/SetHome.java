package sawfowl.commandpack.commands.parameterized.onlyplayercommands.teleports.home;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.player.Home;
import sawfowl.commandpack.api.data.player.PlayerData;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.configs.miscellaneous.LocationData;
import sawfowl.commandpack.configure.configs.player.HomeData;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class SetHome extends AbstractPlayerCommand {

	public SetHome(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		delay(src, locale, consumer -> {
			PlayerData playerData = plugin.getPlayersData().getOrCreatePlayerData(src);
			String name = getString(context, "Home", "Home");
			if(name.equalsIgnoreCase("list")) name = "Home";
			Home home = new HomeData(name, new LocationData(src.serverLocation(), src.rotation()), !playerData.getDefaultHome().isPresent());
			if(playerData.addHome(home, Permissions.getHomeLimit(src))) {
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_SETHOME_SUCCESS).replace(Placeholders.HOME, home.asComponent()).get());
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
		return Permissions.HOME;
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createString("Home", true), true, LocalesPaths.COMMANDS_EXCEPTION_NAME_NOT_PRESENT));
	}

	@Override
	public String command() {
		return "sethome";
	}

}
