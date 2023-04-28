package sawfowl.commandpack.commands.parameterized.serverstat;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractInfoCommand;

public class Tps extends AbstractInfoCommand {

	protected Parameterized command;
	boolean register;
	public Tps(CommandPack plugin) {
		super(plugin);
		register = !plugin.getConfigManager().getCommandsConfig().get().getCommandConfig("serverstat").isEnable();
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			delay((ServerPlayer) src, locale, consumer -> {
				src.sendMessage(getTPS(locale));
			});
		} else {
			src.sendMessage(getTPS(locale));
		}
	}

	@Override
	public Parameterized build() {
		return command == null ? command = fastBuild() : command;
	}

	@Override
	public String permission() {
		return Permissions.SERVER_STAT_STAFF_INFO_TPS;
	}

	@Override
	public String command() {
		return "tps";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

	public void enableRegister() {
		register = true;
	}

}
