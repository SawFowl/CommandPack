package sawfowl.commandpack.commands.parameterized.serverstat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.plugin.PluginContainer;

import net.kyori.adventure.audience.Audience;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractInfoCommand;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class PluginInfo extends AbstractInfoCommand {

	public PluginInfo(CommandPack plugin) {
		super(plugin);
		this.CHOICES = Parameter.choices(Sponge.pluginManager().plugins().stream().map(container -> container.metadata().id()).toArray(String[]::new)).key("Plugin").build();;
	}

	public final Value<String> CHOICES;
	public PluginInfo(CommandPack plugin, Collection<PluginContainer> plugins) {
		super(plugin);
		containers = plugins;
		CHOICES = Parameter.choices(plugins.stream().map(container -> container.metadata().id()).toArray(String[]::new)).key("Plugin").build();
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			delay((ServerPlayer) src, locale, consumer -> {
				sendPluginInfo(src, locale, Sponge.pluginManager().plugin(getString(context, "Plugin").get()).get().metadata());
			});
		} else {
			sendPluginInfo(src, locale, Sponge.pluginManager().plugin(getString(context, "Plugin").get()).get().metadata());
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
		return "info";
	}

	@Override
	public String trackingName() {
		return "serverstat";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CHOICES, false, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT));
	}

}
