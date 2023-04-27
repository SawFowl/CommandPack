package sawfowl.commandpack.commands.parameterized.serverstat;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.plugin.PluginContainer;

import net.kyori.adventure.audience.Audience;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractInfoCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class RefreshPlugin extends AbstractInfoCommand {

	public RefreshPlugin(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		PluginContainer container = Sponge.pluginManager().plugin(getString(context, "Plugin").get()).get();
		if(isPlayer) {
			delay((ServerPlayer) src, locale, consumer -> {
				sendRefreshEvent(container);
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_REFRESH_MESSAGE));
			});
		} else {
			sendRefreshEvent(container);
			src.sendMessage(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_REFRESH_MESSAGE));
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
		return Arrays.asList(ParameterSettings.of(CommandParameters.PLUGINS, false, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT));
	}

}
