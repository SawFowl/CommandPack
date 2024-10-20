package sawfowl.commandpack.commands.parameterized.onlyplayercommands.teleports;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.adventure.SpongeComponents;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Tpa extends AbstractPlayerCommand {

	public Tpa(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		ServerPlayer target = getPlayer(context).get();
		if(target.uniqueId().equals(src.uniqueId())) exception(plugin.getLocales().getLocale(locale).getCommandExceptions().getTargetSelf());
		if(plugin.getPlayersData().getTempData().isDisableTpRequests(target)) exception(getTpa(locale).getDisabledRequest());
		delay(src, locale, consumer -> {
			UUID source = src.uniqueId();
			TpaAccess access = new TpaAccess();
			target.sendMessage(getTpa(target).getRequest(src).clickEvent(SpongeComponents.executeCallback(cause -> {
				if(!access.access) return;
				if(Sponge.server().player(source).isPresent()) {
					plugin.getPlayersData().getTempData().setPreviousLocation(src);
					src.setLocation(target.serverLocation());
					src.sendMessage(getTpa(locale).getAccepted());
				} else target.sendMessage(getTpa(target).getOffline());
				access.access = false;
			})));
			src.sendMessage(getTpa(locale).getSuccess());
		});
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(false), false, locale -> plugin.getLocales().getLocale(locale).getCommandExceptions().getPlayerNotPresent()));
	}

	@Override
	public String permission() {
		return Permissions.TPA;
	}

	@Override
	public String command() {
		return "tpa";
	}

	private class TpaAccess {
		public boolean access = true;
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Tpa getTpa(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getTpa();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Tpa getTpa(ServerPlayer player) {
		return getTpa(player.locale());
	}

}
