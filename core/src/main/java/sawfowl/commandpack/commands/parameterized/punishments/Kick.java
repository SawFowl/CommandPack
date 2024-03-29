package sawfowl.commandpack.commands.parameterized.punishments;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.Sponge;
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
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

@Register
public class Kick extends AbstractParameterizedCommand {

	public Kick(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		ServerPlayer target = getPlayer(context).get();
		if(isPlayer && target.name().equals(((ServerPlayer) src).name())) exception(getText(locale, LocalesPaths.COMMANDS_EXCEPTION_TARGET_SELF));
		if(target.hasPermission(Permissions.IGNORE_KICK) && isPlayer) exception(getText(locale, LocalesPaths.COMMANDS_KICK_IGNORE).replace(Placeholders.PLAYER, target.name()).get());
		Component source = isPlayer ? ((ServerPlayer) src).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) src).name())) : text("&4Server");
		Component reason = text(getString(context, "Reason").orElse("-"));
		target.kick(getText(target, LocalesPaths.COMMANDS_KICK_DISCONNECT).replace(new String[] {Placeholders.SOURCE, Placeholders.VALUE}, source, reason).get());
		Sponge.systemSubject().sendMessage(getText(plugin.getLocales().getLocaleService().getDefaultLocale(), LocalesPaths.COMMANDS_KICK_ANNOUNCEMENT).replace(new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, source, text(target.name()), reason).get());
		if(plugin.getMainConfig().getPunishment().getAnnounce().isKick()) {
			Component targetName = target.get(Keys.DISPLAY_NAME).orElse(text(target.name()));
			Sponge.server().onlinePlayers().forEach(player -> {
				player.sendMessage(getText(player, LocalesPaths.COMMANDS_KICK_ANNOUNCEMENT).replace(new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, source, targetName, reason).get());
			});
		} else src.sendMessage(getText(locale, LocalesPaths.COMMANDS_KICK_SUCCESS).replace(Placeholders.PLAYER, target.name()).get());
	}

	@Override
	public Parameterized build() {
		if(!plugin.getMainConfig().getPunishment().isEnable()) return null;
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.KICK_STAFF;
	}

	@Override
	public String command() {
		return "kick";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(
				ParameterSettings.of(CommandParameters.createPlayer(false), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT),
				ParameterSettings.of(CommandParameters.createStrings("Reason", true), true, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
			);
		}

}
