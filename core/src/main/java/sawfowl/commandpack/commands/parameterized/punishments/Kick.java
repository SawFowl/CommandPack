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

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Kick extends AbstractParameterizedCommand {

	public Kick(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		ServerPlayer target = getPlayer(context).get();
		if(isPlayer && target.name().equals(((ServerPlayer) src).name())) exception(getExceptions(locale).getTargetSelf());
		if(target.hasPermission(Permissions.IGNORE_KICK) && isPlayer) exception(getKick(locale).getIgnore(target.name()));
		Component source = isPlayer ? ((ServerPlayer) src).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) src).name())) : text("&4Server");
		Component reason = text(getString(context, "Reason").orElse("-"));
		target.kick(getKick(target).getDisconnect(source, reason));
		Sponge.systemSubject().sendMessage(getKick().getAnnouncement(source, target.name(), reason));
		if(plugin.getMainConfig().getPunishment().getAnnounce().isKick()) {
			Sponge.server().onlinePlayers().forEach(player -> {
				player.sendMessage(getKick(player).getAnnouncement(source, target.name(), reason));
			});
		} else src.sendMessage(getKick(locale).getSuccess(target.name()));
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
			ParameterSettings.of(CommandParameters.createPlayer(false), false, locale -> getExceptions(locale).getPlayerNotPresent()),
			ParameterSettings.of(CommandParameters.createStrings("Reason", true), true, locale -> getExceptions(locale).getReasonNotPresent())
		);
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Kick getKick(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getKick();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Kick getKick(ServerPlayer player) {
		return getKick(player.locale());
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Kick getKick() {
		return plugin.getLocales().getSystemLocale().getCommands().getKick();
	}

}
