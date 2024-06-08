package sawfowl.commandpack.commands.parameterized.gamemode.types;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.GameMode;

@Register
public class Spectator extends AbstractParameterizedCommand {

	public Spectator(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			ServerPlayer player = getPlayer(context).orElse((ServerPlayer) src);
			delay(player, locale, consumer -> {
				player.offer(Keys.GAME_MODE, GameModes.SPECTATOR.get());
				if(!player.uniqueId().equals(((ServerPlayer) src).uniqueId())) {
					player.sendMessage(getGameMode(player).getSuccess(getGameMode(player).getSpectator()));
					src.sendMessage(getGameMode(locale).getSuccessStaff(player, getGameMode(locale).getSpectator()));
				} else src.sendMessage(getGameMode(locale).getSuccess(getGameMode(locale).getSpectator()));
			});
		} else {
			ServerPlayer target = getPlayer(context).get();
			target.offer(Keys.GAME_MODE, GameModes.SPECTATOR.get());
			target.sendMessage(getGameMode(target).getSuccess(getGameMode(target).getSpectator()));
			src.sendMessage(getGameMode(locale).getSuccessStaff(target, getGameMode(locale).getSpectator()));
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.getGameModePermission(command());
	}

	@Override
	public String command() {
		return "spectator";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(Permissions.GAMEMODE_OTHER_STAFF, true), false, locale -> plugin.getLocales().getLocale(locale).getCommandExceptions().getPlayerNotPresent()));
	}

	private GameMode getGameMode(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getGameMode();
	}

	private GameMode getGameMode(ServerPlayer player) {
		return getGameMode(player.locale());
	}

}
