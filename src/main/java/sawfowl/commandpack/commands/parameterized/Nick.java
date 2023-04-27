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
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Nick extends AbstractParameterizedCommand {

	public Nick(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		String nick = getString(context, "Nick").get();
		if(isPlayer) {
			Optional<ServerPlayer> optTarget = getPlayer(context);
			if(optTarget.isPresent()) {
				if(nick.equals("clear")) {
					clearName(src, locale, optTarget.get(), optTarget.get().uniqueId().equals(((ServerPlayer) src).uniqueId()));
				} else setName(src, locale, optTarget.get(), nick, optTarget.get().uniqueId().equals(((ServerPlayer) src).uniqueId()));
			} else {
				delay((ServerPlayer) src, locale, consumer -> {
					if(nick.equals("clear")) {
						clearName(src, locale, (ServerPlayer) src, true);
					} else setName(src, locale, (ServerPlayer) src, nick, true);
				});
			}
		} else {
			ServerPlayer target = getPlayer(context).get();
			if(nick.equals("clear")) {
				clearName(src, locale, target, false);
			} else setName(src, locale, target, nick, false);
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.NICK;
	}

	@Override
	public String command() {
		return "nick";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(
			ParameterSettings.of(CommandParameters.createString("Nick", false), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT),
			ParameterSettings.of(CommandParameters.createPlayer(Permissions.NICK_STAFF, true), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT)
		);
	}

	private void setName(Audience src, Locale locale, ServerPlayer target, String nick, boolean equals) {
		target.offer(Keys.CUSTOM_NAME, text(nick));
		if(!equals) {
			target.sendMessage(TextUtils.replace(getText(target, LocalesPaths.COMMANDS_NICK_SET_SELF), Placeholders.VALUE, text(nick)));
			src.sendMessage(TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_NICK_SET_STAFF), new String[] {Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {text(target.name()), text(nick)}));
		} else target.sendMessage(TextUtils.replace(getText(target, LocalesPaths.COMMANDS_NICK_SET_SELF), Placeholders.VALUE, text(nick)));
	}

	private void clearName(Audience src, Locale locale, ServerPlayer target, boolean equals) {
		target.remove(Keys.CUSTOM_NAME);
		if(!equals) {
			target.sendMessage(getText(target, LocalesPaths.COMMANDS_NICK_CLEAR_SELF));
			target.sendMessage(TextUtils.replace(getText(target, LocalesPaths.COMMANDS_NICK_CLEAR_STAFF), Placeholders.PLAYER, text(target.name())));
		} else target.sendMessage(getText(target, LocalesPaths.COMMANDS_NICK_CLEAR_SELF));
	}

}
