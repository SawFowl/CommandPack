package sawfowl.commandpack.commands.parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPackPlugin;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Fly extends AbstractParameterizedCommand {

	public Fly(CommandPackPlugin plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		Optional<ServerPlayer> optTarget = getPlayer(context);
		if(optTarget.isPresent()) {
			if(setFly(optTarget.get())) {
				sendStaffMessage(src, locale, optTarget.get(), LocalesPaths.COMMANDS_FLY_ENABLE_STAFF, LocalesPaths.COMMANDS_FLY_ENABLE);
			} else sendStaffMessage(src, locale, optTarget.get(), LocalesPaths.COMMANDS_FLY_DISABLE_STAFF, LocalesPaths.COMMANDS_FLY_DISABLE);
		} else {
			if(!isPlayer) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT);
			delay((ServerPlayer) src, locale, consumer -> {
				if(setFly((ServerPlayer) src)) {
					src.sendMessage(getText(locale, LocalesPaths.COMMANDS_FLY_ENABLE));
				} else src.sendMessage(getText(locale, LocalesPaths.COMMANDS_FLY_DISABLE));
			});
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(Permissions.FLY_STAFF, true), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT));
	}

	@Override
	public String permission() {
		return Permissions.FLY;
	}

	@Override
	public String command() {
		return "fly";
	}

	private boolean setFly(ServerPlayer player) {
		boolean newValue = !isFlying(player);
		player.offer(Keys.CAN_FLY, !newValue);
		if(!newValue) player.offer(Keys.IS_FLYING, !newValue);
		return isFlying(player);
	}

	private boolean isFlying(ServerPlayer player) {
		return player.get(Keys.CAN_FLY).orElse(false);
	}

	private void sendStaffMessage(Audience src, Locale staffLocale, ServerPlayer target, Object[] pathStaff, Object[] pathPlayer) {
		src.sendMessage(getText(staffLocale, pathStaff));
		target.sendMessage(getText(staffLocale, pathPlayer));
	}

}
