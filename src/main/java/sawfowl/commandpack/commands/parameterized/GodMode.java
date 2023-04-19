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

public class GodMode extends AbstractParameterizedCommand {

	public GodMode(CommandPackPlugin plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		Optional<ServerPlayer> optTarget = getPlayer(context);
		if(isPlayer) {
			if(optTarget.isPresent() && !optTarget.get().uniqueId().equals(((ServerPlayer) src).uniqueId())) {
				if(setGodMode(optTarget.get())) {
					sendStaffMessage(src, locale, optTarget.get(), LocalesPaths.COMMANDS_GODMODE_ENABLE_STAFF, LocalesPaths.COMMANDS_GODMODE_ENABLE);
				} else sendStaffMessage(src, locale, optTarget.get(), LocalesPaths.COMMANDS_GODMODE_DISABLE_STAFF, LocalesPaths.COMMANDS_GODMODE_DISABLE);
			} else {
				delay((ServerPlayer) src, locale, consumer -> {
					if(setGodMode((ServerPlayer) src)) {
						src.sendMessage(getText(locale, LocalesPaths.COMMANDS_GODMODE_ENABLE));
					} else src.sendMessage(getText(locale, LocalesPaths.COMMANDS_GODMODE_DISABLE));
				});
			}
		} else {
			if(setGodMode(optTarget.get())) {
				sendStaffMessage(src, locale, optTarget.get(), LocalesPaths.COMMANDS_GODMODE_ENABLE_STAFF, LocalesPaths.COMMANDS_GODMODE_ENABLE);
			} else sendStaffMessage(src, locale, optTarget.get(), LocalesPaths.COMMANDS_GODMODE_DISABLE_STAFF, LocalesPaths.COMMANDS_GODMODE_DISABLE);
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(Permissions.GODMODE_STAFF, true), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT));
	}

	@Override
	public String permission() {
		return Permissions.GODMODE;
	}

	private boolean setGodMode(ServerPlayer player) {
		player.offer(Keys.INVULNERABLE, !isGodMode(player));
		return isGodMode(player);
	}

	private boolean isGodMode(ServerPlayer player) {
		return player.get(Keys.INVULNERABLE).orElse(false);
	}

	private void sendStaffMessage(Audience src, Locale staffLocale, ServerPlayer target, Object[] pathStaff, Object[] pathPlayer) {
		src.sendMessage(getText(staffLocale, pathStaff));
		target.sendMessage(getText(staffLocale, pathPlayer));
	}

	@Override
	public String command() {
		return "godmode";
	}

}
