package sawfowl.commandpack.commands.parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

@Register
public class Ping extends AbstractParameterizedCommand {

	public Ping(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		Optional<ServerPlayer> player = getPlayer(context);
		if(isPlayer) {
			if(player.isPresent() && !player.get().uniqueId().equals(((ServerPlayer) src).uniqueId())) {
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_PING_SUCCESS_STAFF).replace(Placeholders.VALUE, ((ServerPlayer) src).connection().latency()).get());
			} else src.sendMessage(getText(locale, LocalesPaths.COMMANDS_PING_SUCCESS).replace(new String[] {Placeholders.PLAYER, Placeholders.VALUE}, player.get().name(), ((ServerPlayer) src).connection().latency()).get());
		} else {
			src.sendMessage(getText(locale, LocalesPaths.COMMANDS_PING_SUCCESS_STAFF).replace(new String[] {Placeholders.PLAYER, Placeholders.VALUE}, player.get().name(), player.get().connection().latency()).get());
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.PING;
	}

	@Override
	public String command() {
		return "ping";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(Permissions.PING_STAFF, true), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT));
	}

}
