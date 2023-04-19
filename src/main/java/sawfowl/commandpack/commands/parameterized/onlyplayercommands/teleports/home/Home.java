package sawfowl.commandpack.commands.parameterized.onlyplayercommands.teleports.home;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPackPlugin;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.player.PlayerData;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Home extends AbstractPlayerCommand {

	public Home(CommandPackPlugin plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		PlayerData playerData = plugin.getPlayersData().getOrCreatePlayerData(src);
		if(playerData.getHomes().size() == 0) exception(locale, LocalesPaths.COMMANDS_HOME_NOT_SET);
		Optional<String> optName = getString(context, "Home");
		if(optName.isPresent()) {
			Optional<sawfowl.commandpack.api.data.player.Home> home = playerData.getHome(optName.get());
			if(home.isPresent()) {
				teleport(home.get(), src);
			} else exception(TextUtils.replace(getText(src, LocalesPaths.COMMANDS_HOME_NOT_FOUND), Placeholders.HOME, optName.get()));
		} else {
			Optional<sawfowl.commandpack.api.data.player.Home> home = playerData.getDefaultHome();
			if(home.isPresent()) {
				teleport(home.get(), src);
			} else exception(TextUtils.replace(getText(src, LocalesPaths.COMMANDS_HOME_NOT_FOUND), Placeholders.HOME, "Home"));
		}
	}

	@Override
	public Parameterized build() {
		return builder().addChild(new HomeList(plugin).build(), "list").build();
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
		return "home";
	}

	private void teleport(sawfowl.commandpack.api.data.player.Home home, ServerPlayer player) throws CommandException {
		if(home.getLocation().getServerLocation().isPresent()) {
			delay(player, player.locale(), consumer -> {
				plugin.getPlayersData().getTempData().setPreviousLocation(player);
				home.getLocation().moveHere(player);
			});
		} else exception(player, LocalesPaths.COMMANDS_HOME_TELEPORT_ERROR);
	}

}
