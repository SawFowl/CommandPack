package sawfowl.commandpack.commands.containerinfo;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractInfoCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;

public class PluginInfo extends AbstractInfoCommand {

	public PluginInfo(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			delay((ServerPlayer) src, locale, consumer -> {
				sendPluginInfo(src, locale, getArgument(context, CommandParameters.PLUGIN).get().metadata());
			});
		} else {
			sendPluginInfo(src, locale, getArgument(context, CommandParameters.PLUGIN).get().metadata());
		}}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.SERVER_STAT_STAFF_PLUGINS_INFO;
	}

	@Override
	public String command() {
		return "plugininfo";
	}

	@Override
	public String trackingName() {
		return "serverstat";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.PLUGIN, false, locale -> plugin.getLocales().getLocale(locale).getCommandExceptions().getPluginNotPresent()));
	}

	@Override
	public void register(RegisterCommandEvent<Parameterized> event) {}

	@Override
	public Settings getCommandSettings() {
		return Settings.builder().setEnable(false).build();
	}

}
