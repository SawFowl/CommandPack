package sawfowl.commandpack.commands.parameterized.player.teleports.home;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.pagination.PaginationList;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.player.PlayerData;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.parameterized.settings.ParameterSettings;
import sawfowl.commandpack.configure.configs.commands.CommandSettings;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class HomeList extends AbstractPlayerCommand {

	public HomeList(CommandPack plugin, String command, CommandSettings commandSettings) {
		super(plugin, command, commandSettings);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		PlayerData playerData = plugin.getPlayersData().getOrCreatePlayerData(src);
		if(playerData.getHomes().size() == 0) exception(locale, LocalesPaths.COMMANDS_HOME_NOT_SET);
		delay(src, locale, consumer -> {
			PaginationList.builder()
				.contents(playerData.homesListChatMenu(locale, true))
				.linesPerPage(10)
				.title(getText(locale, LocalesPaths.COMMANDS_HOME_LIST))
				.padding(text("=").color(getText(locale, LocalesPaths.COMMANDS_HOME_LIST).color()))
				.sendTo(src);
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
		return null;
	}

}
