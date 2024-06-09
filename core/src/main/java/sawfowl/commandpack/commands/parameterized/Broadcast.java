package sawfowl.commandpack.commands.parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Broadcast extends AbstractParameterizedCommand {

	public Broadcast(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			delay((ServerPlayer) src, locale, consumer -> {
				Sponge.systemSubject().sendMessage(plugin.getLocales().getSystemLocale().getCommands().getBroadcast().getTitle(text(getString(context, "Message").get())));
				Sponge.server().onlinePlayers().forEach(player -> {
					player.sendMessage(plugin.getLocales().getLocale(player.locale()).getCommands().getBroadcast().getTitle(text(getString(context, "Message").get())));
				});
			});
		} else {
			Sponge.systemSubject().sendMessage(plugin.getLocales().getSystemLocale().getCommands().getBroadcast().getTitle(text(getString(context, "Message").get())));
			Sponge.server().onlinePlayers().forEach(player -> {
				player.sendMessage(plugin.getLocales().getLocale(player.locale()).getCommands().getBroadcast().getTitle(text(getString(context, "Message").get())));
			});
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.BROADCAST_STAFF;
	}

	@Override
	public String command() {
		return "broadcast";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createStrings("Message", false), false, locale -> getExceptions(locale).getMessageNotPresent()));
	}

}
