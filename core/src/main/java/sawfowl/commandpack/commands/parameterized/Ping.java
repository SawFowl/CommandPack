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
import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

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
				src.sendMessage(getPing(locale).getSuccessStaff(player.get(), MixinServerPlayer.cast(player.get()).getPing()));
			} else src.sendMessage(getPing(locale).getSuccessStaff((ServerPlayer) src, MixinServerPlayer.cast((ServerPlayer) src).getPing()));
		} else src.sendMessage(getPing(locale).getSuccessStaff(player.get(), MixinServerPlayer.cast(player.get()).getPing()));
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
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(Permissions.PING_STAFF, true), false, locale -> getExceptions(locale).getPlayerNotPresent()));
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Ping getPing(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getPing();
	}

}
