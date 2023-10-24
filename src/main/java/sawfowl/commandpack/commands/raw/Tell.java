package sawfowl.commandpack.commands.raw;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawCompleterSupplier;
import sawfowl.commandpack.api.commands.raw.arguments.RawResultSupplier;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Tell extends AbstractRawCommand {

	public Tell(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		ServerPlayer target = getPlayer(args, 0).get();
		Component message = text(getString(args, 1).get());
		if(isPlayer) {
			if(!cause.hasPermission(Permissions.TELL_STAFF) && target.get(Keys.VANISH_STATE).map(state -> state.invisible()).orElse(false)) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT);
			ServerPlayer player = (ServerPlayer) audience;
			if(target.uniqueId().equals(player.uniqueId())) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_TARGET_SELF);
			delay(player, locale, consumer -> {
				player.sendMessage(getText(locale, LocalesPaths.COMMANDS_TELL_SUCCESS).replace(new String[] {Placeholders.PLAYER, Placeholders.VALUE}, target.get(Keys.CUSTOM_NAME).orElse(text(target.name())), message).get());
				target.sendMessage(getText(locale, LocalesPaths.COMMANDS_TELL_SUCCESS_TARGET).replace(new String[] {Placeholders.PLAYER, Placeholders.VALUE}, player.get(Keys.CUSTOM_NAME).orElse(text(player.name())), message).get());
				plugin.getPlayersData().getTempData().addReply(target, player);
			});
		} else {
			audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_TELL_SUCCESS).replace(new String[] {Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {target.get(Keys.CUSTOM_NAME).orElse(text(target.name())), message}).get());
			target.sendMessage(getText(locale, LocalesPaths.COMMANDS_TELL_SUCCESS_TARGET).replace(new String[] {Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {text("&4Server"), message}).get());
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
		return Arrays.asList(RawArgument.of(ServerPlayer.class, 
			new RawCompleterSupplier<Stream<String>>() {

				@Override
				public Stream<String> get(CommandCause cause, String[] args) {
					return Sponge.server().onlinePlayers().stream().filter(player -> !player.get(Keys.VANISH_STATE).map(state -> state.invisible()).orElse(false)).map(ServerPlayer::name);
				}

			},
			new RawResultSupplier<ServerPlayer>() {

				@Override
				public Optional<ServerPlayer> get(CommandCause cause, String[] args) {
					return args.length >= 1 ? Sponge.server().onlinePlayers().stream().filter(player -> player.name().equals(args[0])).findFirst() : Optional.empty();
				}

			},
			false, false, 0, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT),
			RawArguments.createRemainingJoinedStringsArgument(false, false, 1, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

}
