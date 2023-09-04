package sawfowl.commandpack.commands.parameterized.onlyplayercommands;

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
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Reply extends AbstractPlayerCommand {

	public Reply(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		Component message = text(getString(context, "Message").get());
		delay(src, locale, consumer -> {
			Optional<Audience> optTarget = plugin.getPlayersData().getTempData().getReply(src);
			if(!optTarget.isPresent()) {
				plugin.getPlayersData().getTempData().removeReply(src);
				exception(locale, LocalesPaths.COMMANDS_REPLY_NOTHING);
			}
			Audience targetAudience = optTarget.get();
			if(targetAudience instanceof ServerPlayer) {
				ServerPlayer target = (ServerPlayer) targetAudience;
				if(!src.hasPermission(Permissions.REPLY_STAFF) && target.get(Keys.VANISH_STATE).map(state -> state.invisible()).orElse(false)) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT);
				src.sendMessage(TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_TELL_SUCCESS), new String[] {Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {target.get(Keys.CUSTOM_NAME).orElse(text(target.name())), message}));
				target.sendMessage(TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_TELL_SUCCESS_TARGET), new String[] {Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {src.get(Keys.CUSTOM_NAME).orElse(text(src.name())), message}));
			} else {
				src.sendMessage(TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_TELL_SUCCESS), new String[] {Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {text("&4Server"), message}));
				targetAudience.sendMessage(TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_TELL_SUCCESS_TARGET), new String[] {Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {text(src.name()), message}));
			}
		});
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.REPLY;
	}

	@Override
	public String command() {
		return "reply";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createStrings("Message", false), false, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT));
	}

}
