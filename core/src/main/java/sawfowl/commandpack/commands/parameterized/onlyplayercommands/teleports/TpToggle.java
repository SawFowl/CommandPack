package sawfowl.commandpack.commands.parameterized.onlyplayercommands.teleports;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class TpToggle extends AbstractPlayerCommand {

	public TpToggle(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		delay(src, locale, consumer -> {
			plugin.getPlayersData().getTempData().tpToggle(src);
			if(plugin.getPlayersData().getTempData().isDisableTpRequests(src)) {
				src.sendMessage(plugin.getLocales().getLocale(locale).getCommands().getTpToggle().getDisable());
			} else src.sendMessage(plugin.getLocales().getLocale(locale).getCommands().getTpToggle().getEnable());
		});
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

	@Override
	public String permission() {
		return Permissions.TPTOGGLE;
	}

	@Override
	public String command() {
		return "tptoggle";
	}

}
