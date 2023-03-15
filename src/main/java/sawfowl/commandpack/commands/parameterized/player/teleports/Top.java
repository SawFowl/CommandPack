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

public class Top extends AbstractPlayerCommand {

	public Top(CommandPack plugin, String command, CommandSettings commandSettings) {
		super(plugin, command, commandSettings);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		src.setPosition(src.world().highestPositionAt(src.blockPosition()).toDouble());
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
		return Permissions.TOP;
	}

}
