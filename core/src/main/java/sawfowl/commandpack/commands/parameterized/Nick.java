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
import sawfowl.commandpack.commands.settings.Register;

@Register
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
				} else setName(src, locale, optTarget.get(), text(nick), optTarget.get().uniqueId().equals(((ServerPlayer) src).uniqueId()));
			} else {
				delay((ServerPlayer) src, locale, consumer -> {
					if(nick.equals("clear")) {
						clearName(src, locale, (ServerPlayer) src, true);
					} else setName(src, locale, (ServerPlayer) src, text(nick), true);
				});
			}
		} else {
			ServerPlayer target = getPlayer(context).get();
			if(nick.equals("clear")) {
				clearName(src, locale, target, false);
			} else setName(src, locale, target, text(nick), false);
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
			ParameterSettings.of(CommandParameters.createString("Nick", false), false, locale -> getExceptions(locale).getValueNotPresent()),
			ParameterSettings.of(CommandParameters.createPlayer(Permissions.NICK_STAFF, true), false, locale -> getExceptions(locale).getPlayerNotPresent())
		);
	}

	private void setName(Audience src, Locale locale, ServerPlayer target, Component nick, boolean equals) {
		target.offer(Keys.CUSTOM_NAME, nick);
		if(!equals) {
			target.sendMessage(getNick(target).getSet(nick));
			src.sendMessage(getNick(locale).getSetStaff(target, nick));
		} else target.sendMessage(getNick(locale).getSet(nick));
	}

	private void clearName(Audience src, Locale locale, ServerPlayer target, boolean equals) {
		target.remove(Keys.CUSTOM_NAME);
		if(!equals) {
			target.sendMessage(getNick(target).getClear());
			src.sendMessage(getNick(locale).getClearStaff(target));
		} else src.sendMessage(getNick(locale).getClear());
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Nick getNick(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getNick();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Nick getNick(ServerPlayer player) {
		return getNick(player.locale());
	}

}
