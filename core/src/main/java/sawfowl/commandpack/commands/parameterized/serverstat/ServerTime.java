package sawfowl.commandpack.commands.parameterized.serverstat;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractInfoCommand;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class ServerTime extends AbstractInfoCommand {

	protected Parameterized command;
	boolean register;
	public ServerTime(CommandPackInstance plugin) {
		super(plugin);
		register = !plugin.getConfigManager().getCommandsConfig().get().getCommandConfig("serverstat").isEnable();
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			delay((ServerPlayer) src, locale, consumer -> {
				src.sendMessage(getServerTime(locale).append(Component.newline()).append(getUptime(locale)));
			});
		} else {
			src.sendMessage(getServerTime(locale).append(Component.newline()).append(getUptime(locale)));
		}
	}

	@Override
	public Parameterized build() {
		return command == 
				null ?
				command = fastBuild() :
				command;
	}

	@Override
	public String permission() {
		return Permissions.SERVER_STAT_INFO_TIME;
	}

	@Override
	public String command() {
		return "servertime";
	}

	@Override
	public String trackingName() {
		return "serverstat";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

	public void enableRegister() {
		register = true;
	}

}
