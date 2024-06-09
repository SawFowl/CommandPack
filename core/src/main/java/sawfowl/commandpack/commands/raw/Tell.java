package sawfowl.commandpack.commands.raw;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Tell extends AbstractRawCommand {

	public Tell(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		ServerPlayer target = args.<ServerPlayer>get(0).get();
		Component message = text(args.<String>get(1).get());
		if(isPlayer) {
			if(!cause.hasPermission(Permissions.TELL_STAFF) && target.get(Keys.VANISH_STATE).map(state -> state.invisible()).orElse(false)) exception(getExceptions(locale).getPlayerNotPresent());
			ServerPlayer player = (ServerPlayer) audience;
			if(target.uniqueId().equals(player.uniqueId())) exception(getExceptions(locale).getTargetSelf());
			delay(player, locale, consumer -> {
				player.sendMessage(getTell(locale).getSuccess(target.get(Keys.DISPLAY_NAME).orElse(text(target.name())), message));
				target.sendMessage(getTell(target).getSuccessTarget(player.get(Keys.DISPLAY_NAME).orElse(text(player.name())), message));
				plugin.getPlayersData().getTempData().addReply(target, player);
			});
		} else {
			audience.sendMessage(getTell(locale).getSuccess(target.get(Keys.DISPLAY_NAME).orElse(text(target.name())), message));
			target.sendMessage(getTell(target).getSuccessTarget(text("&4Server"), message));
			plugin.getPlayersData().getTempData().addReply(target, audience);
		}
	}

	@Override
	public Component shortDescription(Locale locale) {
		return null;
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return null;
	}

	@Override
	public String permission() {
		return Permissions.TELL;
	}

	@Override
	public String command() {
		return "tell";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/tell <Player> <Message>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			RawArguments.createPlayerArgument(false, false, 0, null, null, null, locale -> getExceptions(locale).getPlayerNotPresent()),
			RawArguments.createRemainingJoinedStringsArgument("Message", false, false, 1, null, null, null, null, locale -> getExceptions(locale).getMessageNotPresent())
		);
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Tell getTell(Locale locale) {
		return getCommands(locale).getTell();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Tell getTell(ServerPlayer player) {
		return getTell(player.locale());
	}

}
