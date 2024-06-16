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

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Heal extends AbstractParameterizedCommand {

	public Heal(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		Optional<ServerPlayer> target = getPlayer(context);
		if(isPlayer) {
			if(target.isPresent() && !target.get().uniqueId().equals(((ServerPlayer) src).uniqueId())) {
				target.get().offer(Keys.HEALTH, target.get().getOrElse(Keys.MAX_HEALTH, 20d));
				src.sendMessage(getHeal(locale).getSuccessStaff(target.get()));
				target.get().sendMessage(getHeal(target.get()).getSuccess());
			} else delay((ServerPlayer) src, locale, consumer -> {
				((ServerPlayer) src).offer(Keys.HEALTH, ((ServerPlayer) src).getOrElse(Keys.MAX_HEALTH, 20d));
				src.sendMessage(getHeal(locale).getSuccess());
			});
		} else {
			target.get().offer(Keys.HEALTH, target.get().getOrElse(Keys.MAX_HEALTH, 20d));
			src.sendMessage(getHeal(locale).getSuccessStaff(target.get()));
			target.get().sendMessage(getHeal(target.get()).getSuccess());
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.HEAL;
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(Permissions.HEAL_STAFF, true), false, locale -> getExceptions(locale).getPlayerNotPresent()));
	}

	@Override
	public String command() {
		return "heal";
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Heal getHeal(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getHeal();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Heal getHeal(ServerPlayer player) {
		return getHeal(player.locale());
	}

}
