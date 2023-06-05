package sawfowl.commandpack.commands.parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.User;
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

public class Seen extends AbstractParameterizedCommand {

	public Seen(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) src;
			delay(player, locale, consumer -> {
				Sponge.server().userManager().load(getUser(context).get()).thenAccept(user -> {
					sendInfo(src, locale, user.orElse(player.user()), player.hasPermission(Permissions.SEEN_STAFF) || (user.isPresent() && user.get().uniqueId().equals(player.uniqueId())), 10);
				});
			});
		} else {
			Sponge.server().userManager().load(getUser(context).get()).thenAccept(user -> {
				if(user.isPresent()) {
					sendInfo(src, locale, user.get(), true, 30);
				} else src.sendMessage(getText(locale, LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_PRESENT));
			});
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.SEEN;
	}

	@Override
	public String command() {
		return "seen";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createUser(true), false, LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_PRESENT));
	}

	private void sendInfo(Audience audience, Locale locale, User target, boolean full, int size) {
		Optional<ServerPlayer> player = target.player();
		Component displayName = target.get(Keys.DISPLAY_NAME).orElse(text(target.name()));
		Component title = TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_TITLE), Placeholders.PLAYER, displayName);
		List<Component> messages = new ArrayList<>();
		boolean online = target.isOnline() && player.isPresent();
		if(online) {
			if(full) {
				messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_ONLINE), Placeholders.PLAYER, displayName));
				messages.add(TextUtils.replace(title, Placeholders.VALUE, timeFormat(TimeUnit.MILLISECONDS.toSeconds(plugin.getPlayersData().getTempData().getVanishEnabledTime(player.get()).get()), locale)));
			}
		}
	}

}
