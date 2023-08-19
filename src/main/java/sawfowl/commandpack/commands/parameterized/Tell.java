package sawfowl.commandpack.commands.parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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

public class Tell extends AbstractParameterizedCommand {

	public Tell(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		ServerPlayer target = getPlayer(context).get();
		Component message = text(getString(context, "Message").get());
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) src;
			if(target.uniqueId().equals(player.uniqueId())) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_TARGET_SELF);
			delay(player, locale, consumer -> {
				player.sendMessage(TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_TELL_SUCCESS), new String[] {Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {target.get(Keys.CUSTOM_NAME).orElse(text(target.name())), message}));
				target.sendMessage(TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_TELL_SUCCESS_TARGET), new String[] {Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {player.get(Keys.CUSTOM_NAME).orElse(text(player.name())), message}));
				plugin.getPlayersData().getTempData().addReply(target, player);
			});
		} else {
			src.sendMessage(TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_TELL_SUCCESS), new String[] {Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {target.get(Keys.CUSTOM_NAME).orElse(text(target.name())), message}));
			target.sendMessage(TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_TELL_SUCCESS_TARGET), new String[] {Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {text("&4Server"), message}));
			plugin.getPlayersData().getTempData().addReply(target, src);
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
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
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(
			ParameterSettings.of(CommandParameters.createPlayer(false), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT),
			ParameterSettings.of(CommandParameters.createStrings("Message", false), false, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

}
