package sawfowl.commandpack.commands.parameterized.player.teleports;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.parameterized.settings.ParameterSettings;
import sawfowl.commandpack.configure.configs.commands.CommandSettings;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class TpToggle extends AbstractPlayerCommand {

	public TpToggle(CommandPack plugin, String command, CommandSettings commandSettings) {
		super(plugin, command, commandSettings);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		delay(src, locale, consumer -> {
			plugin.getTempPlayerData().tpToggle(src);
			if(plugin.getTempPlayerData().isDisableTpRequests(src)) {
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_TPTOGGLE_DISABLE));
			} else src.sendMessage(getText(locale, LocalesPaths.COMMANDS_TPTOGGLE_ENABLE));
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
	protected String permission() {
		return Permissions.TPTOGGLE;
	}

}
