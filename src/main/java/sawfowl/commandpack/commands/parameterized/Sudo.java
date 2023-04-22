package sawfowl.commandpack.commands.parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.manager.CommandMapping;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.math.vector.Vector3d;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Sudo extends AbstractParameterizedCommand {

	public Sudo(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		String command = getString(context, "Command").get();
		if(!Sponge.server().commandManager().commandMapping(command.contains(" ") ? command.split(" ")[0] : command).isPresent()) exception(getText(locale, LocalesPaths.COMMANDS_SUDO_COMMAND_NOT_FOUND));
		ServerPlayer player = getPlayer(context).get();
		CommandMapping mapping = Sponge.server().commandManager().commandMapping(command.contains(" ") ? command.split(" ")[0] : command).get();
		if(isPlayer) {
			if(!mapping.registrar().canExecute(context.cause(), mapping)) exception(getText(locale, LocalesPaths.COMMANDS_SUDO_EXECUTE_NOT_ALLOWED));
			delay((ServerPlayer) src, locale, consumer -> {
				Sponge.server().commandManager().process(context.cause().subject(), player, command);
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_SUDO_SUCCESS));
			});
		} else {
			if(mapping.registrar().canExecute(context.cause(), mapping)) {
				Sponge.server().commandManager().process(context.cause().subject(), player, command);
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_SUDO_SUCCESS));
			} else {
				CommandCause playerCause = new CommandCause() {
					Cause cause = Cause.of(EventContext.builder().add(EventContextKeys.PLUGIN, plugin.getPluginContainer()).add(EventContextKeys.PLAYER, player).add(EventContextKeys.COMMAND, command).add(EventContextKeys.AUDIENCE, player).add(EventContextKeys.SUBJECT, player).build(), plugin.getPluginContainer());
					@Override
					public Optional<BlockSnapshot> targetBlock() {
						return Optional.empty();
					}
					
					@Override
					public Subject subject() {
						return player;
					}
					
					@Override
					public void sendMessage(Identity source, Component message) {
						if(source instanceof Audience) ((Audience) source).sendMessage(message);
					}
					
					@Override
					public void sendMessage(Identified source, Component message) {
						sendMessage(source.identity(), message);
					}
					
					@Override
					public Optional<Vector3d> rotation() {
						return Optional.ofNullable(player.rotation());
					}
					
					@Override
					public Optional<ServerLocation> location() {
						return Optional.ofNullable(player.serverLocation());
					}
					
					@Override
					public Cause cause() {
						return cause;
					}
					
					@Override
					public Audience audience() {
						return src;
					}
				};
				if(!mapping.registrar().canExecute(playerCause, mapping)) exception(getText(locale, LocalesPaths.COMMANDS_SUDO_EXECUTE_NOT_ALLOWED));
				Sponge.server().commandManager().process(player, command);
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_SUDO_SUCCESS));
			}
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.SUDO_STAFF;
	}

	@Override
	public String command() {
		return "sudo";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(
			ParameterSettings.of(CommandParameters.createPlayer(false), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT),
			ParameterSettings.of(CommandParameters.createString("Command", false), false, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

}
