package sawfowl.commandpack.commands.parameterized.player;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.commands.CommandParameters;
import sawfowl.commandpack.commands.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.configure.configs.miscellaneous.SpawnData;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class SetSpawn extends AbstractPlayerCommand {

	public SetSpawn(CommandPack plugin, String command, String[] aliases) {
		super(plugin, command, aliases);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		plugin.getMainConfig().setSpawnData(new SpawnData(src.serverLocation(), src.rotation(), getBoolean(context, "ForceSpawn", false),getBoolean(context, "ForceRespawn", false)));
		plugin.getConfigManager().updateMainConfig();
		src.sendMessage(getText(src.locale(), LocalesPaths.COMMANDS_SETSPAWN_SUCCESS));
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
					new ParameterSettings(CommandParameters.createBoolean("ForceSpawn", true), true, true, new Object[]{}),
					new ParameterSettings(CommandParameters.createBoolean("ForceRespawn", true), true, true, new Object[]{})
				);
	}

}
