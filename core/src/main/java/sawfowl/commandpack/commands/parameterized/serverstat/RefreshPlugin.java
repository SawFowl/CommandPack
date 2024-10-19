package sawfowl.commandpack.commands.parameterized.serverstat;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.plugin.PluginContainer;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractInfoCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;

public class RefreshPlugin extends AbstractInfoCommand {

	public RefreshPlugin(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		PluginContainer container = context.one(CommandParameters.PLUGIN).get();
		if(isPlayer) {
			delay((ServerPlayer) src, locale, consumer -> {
				sendRefreshEvent(container);
				src.sendMessage(plugin.getLocales().getLocale(locale).getCommands().getServerStat().getRefreshPlugin());
			});
		} else {
			sendRefreshEvent(container);
			src.sendMessage(plugin.getLocales().getLocale(locale).getCommands().getServerStat().getRefreshPlugin());
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.SERVER_STAT_STAFF_INFO_PLUGINS_REFRESH;
	}

	@Override
	public String command() {
		return "refresh";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.PLUGIN, false, locale -> getExceptions(locale).getPluginNotPresent()));
	}

}
