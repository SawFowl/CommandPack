package sawfowl.commandpack.commands.parameterized.onlyplayercommands.teleports.home;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.player.PlayerData;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Home extends AbstractPlayerCommand {

	public Home(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		PlayerData playerData = plugin.getPlayersData().getOrCreatePlayerData(src);
		if(playerData.getHomes().size() == 0) exception(getHome(locale).getNotSet());
		Optional<String> optName = getString(context, "Home");
		if(optName.isPresent()) {
			Optional<sawfowl.commandpack.api.data.player.Home> home = playerData.getHome(optName.get());
			if(home.isPresent()) {
				teleport(home.get(), src);
			} else exception(getHome(locale).getNotFound(optName.get()));
		} else {
			Optional<sawfowl.commandpack.api.data.player.Home> home = playerData.getDefaultHome();
			if(home.isPresent()) {
				teleport(home.get(), src);
			} else exception(getHome(locale).getNotFound("Home"));
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
		return Arrays.asList(ParameterSettings.of(CommandParameters.createStringHome(), true, locale -> getExceptions(locale).getNameNotPresent()));
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
		} else exception(getHome(player).getError());
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Home getHome(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getHome();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Home getHome(ServerPlayer player) {
		return getHome(player.locale());
	}

}
