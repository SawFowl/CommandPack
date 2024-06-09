package sawfowl.commandpack.commands.parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.effect.VanishState;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Vanish extends AbstractParameterizedCommand {

	public Vanish(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			Optional<ServerPlayer> optTarget = getPlayer(context);
			if(optTarget.isPresent()) {
				vanish(optTarget.get(), src, locale, optTarget.get().uniqueId().equals(((ServerPlayer) src).uniqueId()), context.hasPermission(Permissions.VANISH_STAFF));
			} else {
				delay((ServerPlayer) src, locale, consumer -> {
					vanish((ServerPlayer) src, src, locale, true, context.hasPermission(Permissions.VANISH_STAFF));
				});
			}
		} else {
			ServerPlayer target = getPlayer(context).get();
			vanish(target, src, locale, false, true);
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.VANISH;
	}

	@Override
	public String command() {
		return "vanish";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(Permissions.VANISH_STAFF, true), false, locale -> getExceptions(locale).getPlayerNotPresent()));
	}

	private void vanish(ServerPlayer target, Audience src, Locale locale, boolean equals, boolean isStaff) {
		if(target.getKeys().contains(Keys.VANISH_STATE) && target.get(Keys.VANISH_STATE).get().invisible()) {
			target.remove(Keys.VANISH_STATE);
			target.offer(Keys.VANISH_STATE, VanishState.unvanished());
			if(!equals) {
				target.sendMessage(getVanish(target).getDisable());
				src.sendMessage(getVanish(locale).getDisableStaff(target));
			} else src.sendMessage(getVanish(locale).getDisable());
			plugin.getPlayersData().getTempData().removeVanishEnabledTime(target);
		} else {
			target.offer(Keys.VANISH_STATE, VanishState.vanished().ignoreCollisions(isStaff).createParticles(false).createSounds(!isStaff));
			if(!equals) {
				target.sendMessage(getVanish(target).getEnable());
				src.sendMessage(getVanish(locale).getEnableStaff(target));
			} else src.sendMessage(getVanish(locale).getEnable());
			plugin.getPlayersData().getTempData().setVanishTime(target);
		}
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Vanish getVanish(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getVanish();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Vanish getVanish(ServerPlayer player) {
		return getVanish(player.locale());
	}

}
