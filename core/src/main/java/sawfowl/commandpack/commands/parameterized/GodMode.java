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
import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class GodMode extends AbstractParameterizedCommand {

	public GodMode(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		Optional<ServerPlayer> optTarget = getPlayer(context);
		if(isPlayer) {
			if(optTarget.isPresent() && !optTarget.get().uniqueId().equals(((ServerPlayer) src).uniqueId())) {
				if(setGodMode(optTarget.get())) {
					sendStaffMessage(src, optTarget.get(), getGodMode(locale).getEnableStaff(optTarget.get()), getGodMode(optTarget.get()).getEnable());
				} else sendStaffMessage(src, optTarget.get(), getGodMode(locale).getDisabeStaff(optTarget.get()), getGodMode(optTarget.get()).getDisabe());
			} else {
				delay((ServerPlayer) src, locale, consumer -> {
					if(setGodMode((ServerPlayer) src)) {
						src.sendMessage(getGodMode(locale).getEnable());
					} else src.sendMessage(getGodMode(locale).getDisabe());
				});
			}
		} else {
			if(setGodMode(optTarget.get())) {
				sendStaffMessage(src, optTarget.get(), getGodMode(locale).getEnableStaff(optTarget.get()), getGodMode(optTarget.get()).getEnable());
			} else sendStaffMessage(src, optTarget.get(), getGodMode(locale).getDisabeStaff(optTarget.get()), getGodMode(optTarget.get()).getDisabe());
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(Permissions.GODMODE_STAFF, true), false, locale -> getExceptions(locale).getPlayerNotPresent()));
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

	private void sendStaffMessage(Audience src, ServerPlayer target, Component staff, Component player) {
		src.sendMessage(staff);
		target.sendMessage(player);
	}

	@Override
	public String command() {
		return "godmode";
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.GodMode getGodMode(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getGodMode();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.GodMode getGodMode(ServerPlayer player) {
		return getGodMode(player.locale());
	}

}
