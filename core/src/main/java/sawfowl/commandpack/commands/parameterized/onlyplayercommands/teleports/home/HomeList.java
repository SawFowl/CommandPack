package sawfowl.commandpack.commands.parameterized.onlyplayercommands.teleports.home;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.service.pagination.PaginationList;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.player.PlayerData;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Home;

public class HomeList extends AbstractPlayerCommand {

	public HomeList(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		PlayerData playerData = plugin.getPlayersData().getOrCreatePlayerData(src);
		if(playerData.getHomes().size() == 0) exception(getHome(locale).getNotSet());
		delay(src, locale, consumer -> {
			PaginationList.builder()
				.contents(playerData.homesListChatMenu(locale, true))
				.linesPerPage(10)
				.title(getHome(locale).getListTitle())
				.padding(text("=").color(getHome(locale).getListTitle().color()))
				.sendTo(src);
		});
	}

	@Override
	public void register(RegisterCommandEvent<Parameterized> event) {}

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
		return null;
	}

	@Override
	public String command() {
		return "list";
	}

	private Home getHome(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getHome();
	}

}
