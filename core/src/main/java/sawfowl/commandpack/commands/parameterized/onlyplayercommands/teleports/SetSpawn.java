package sawfowl.commandpack.commands.parameterized.onlyplayercommands.teleports;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.configure.configs.miscellaneous.SpawnData;

@Register
public class SetSpawn extends AbstractPlayerCommand {

	public SetSpawn(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		plugin.getMainConfig().setSpawnData(new SpawnData(src.serverLocation(), src.rotation(), getBoolean(context, "ForceSpawn", false), getBoolean(context, "ForceRespawn", false)));
		plugin.getConfigManager().updateMainConfig();
		src.sendMessage(plugin.getLocales().getLocale(locale).getCommands().getSpawn().getSet());
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.SET_SPAWN_STAFF;
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(
			ParameterSettings.of(CommandParameters.createBoolean("ForceSpawn", true), false, locale -> plugin.getLocales().getLocale(locale).getCommandExceptions().getBooleanNotPresent()),
			ParameterSettings.of(CommandParameters.createBoolean("ForceRespawn", true), false, locale -> plugin.getLocales().getLocale(locale).getCommandExceptions().getBooleanNotPresent())
		);
	}

	@Override
	public String command() {
		return "setspawn";
	}

}
