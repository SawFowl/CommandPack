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
public class Fly extends AbstractParameterizedCommand {

	public Fly(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		Optional<ServerPlayer> optTarget = getPlayer(context);
		if(optTarget.isPresent() && (!isPlayer || !optTarget.get().uniqueId().equals(((ServerPlayer) src).uniqueId()))) {
			if(setFly(optTarget.get())) {
				sendStaffMessage(src, optTarget.get(), getFly(locale).getEnableStaff(optTarget.get()), getFly(optTarget.get()).getEnable());
			} else sendStaffMessage(src, optTarget.get(), getFly(locale).getDisabeStaff(optTarget.get()), getFly(optTarget.get()).getDisabe());
		} else {
			if(!isPlayer) exception(getExceptions(locale).getPlayerNotPresent());
			delay((ServerPlayer) src, locale, consumer -> {
				if(setFly((ServerPlayer) src)) {
					src.sendMessage(getFly(locale).getEnable());
				} else src.sendMessage(getFly(locale).getDisabe());
			});
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(Permissions.FLY_STAFF, true), false, locale -> getExceptions(locale).getPlayerNotPresent()));
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
		player.offer(Keys.CAN_FLY, newValue);
		if(!newValue) player.offer(Keys.IS_FLYING, newValue);
		return isFlying(player);
	}

	private boolean isFlying(ServerPlayer player) {
		return player.get(Keys.CAN_FLY).orElse(false);
	}

	private void sendStaffMessage(Audience src, ServerPlayer target, Component staff, Component targetMessage) {
		src.sendMessage(staff);
		target.sendMessage(targetMessage);
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Fly getFly(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getFly();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Fly getFly(ServerPlayer player) {
		return getFly(player.locale());
	}

}
