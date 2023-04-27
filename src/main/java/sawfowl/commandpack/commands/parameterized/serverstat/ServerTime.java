package sawfowl.commandpack.commands.parameterized.serverstat;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractInfoCommand;

public class ServerTime extends AbstractInfoCommand {

	protected Parameterized command;
	boolean register;
	public ServerTime(CommandPack plugin) {
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
	public void register(RegisterCommandEvent<Parameterized> event) {
		if(!register || Sponge.server().commandManager().commandMapping(command()).isPresent()) return;
		if(getCommandSettings() == null) {
			event.register(getContainer(), command, command());
		} else {
			if(!getCommandSettings().isEnable()) return;
			if(getCommandSettings().getAliases() != null && getCommandSettings().getAliases().length > 0) {
				event.register(getContainer(), command, command(), getCommandSettings().getAliases());
			} else event.register(getContainer(), command, command());
		}
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
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

	public void enableRegister() {
		register = true;
	}

}
