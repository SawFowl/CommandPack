package sawfowl.commandpack.commands.parameterized.onlyplayercommands.teleports;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.adventure.SpongeComponents;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class TpaHereAll extends AbstractPlayerCommand {

	public TpaHereAll(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		delay(src, locale, consumer -> {
			UUID source = src.uniqueId();
			Sponge.server().onlinePlayers().forEach(target -> {
				TpaAccess access = new TpaAccess();
				if(!target.uniqueId().equals(src.uniqueId())) target.sendMessage(getTpa(target).getRequestHere(src).clickEvent(SpongeComponents.executeCallback(cause -> {
					if(!access.access) return;
					if(Sponge.server().player(source).isPresent()) {
						plugin.getPlayersData().getTempData().setPreviousLocation(target);
						target.setLocation(src.serverLocation());
					} else target.sendMessage(getTpa(target).getOffline());
					access.access = false;
				})));
			});
			src.sendMessage(getTpa(locale).getSuccess());
		});
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

	@Override
	public String permission() {
		return Permissions.TPA_HERE_ALL;
	}

	@Override
	public String command() {
		return "tpahereall";
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
