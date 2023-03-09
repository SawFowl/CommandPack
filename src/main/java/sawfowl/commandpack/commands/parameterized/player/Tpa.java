package sawfowl.commandpack.commands.parameterized.player;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.adventure.SpongeComponents;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.commands.CommandParameters;
import sawfowl.commandpack.commands.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.configs.commands.CommandSettings;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Tpa extends AbstractPlayerCommand {

	public Tpa(CommandPack plugin, String command, CommandSettings commandSettings) {
		super(plugin, command, commandSettings);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		ServerPlayer target = getPlayer(context).get();
		if(target.uniqueId().equals(src.uniqueId())) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_TARGET_SELF);
		if(plugin.getTempPlayerData().isDisableTpRequests(target)) exception(locale, LocalesPaths.COMMANDS_TPA_DISABLE_TP_REQUESTS);
		delay(src, locale, consumer -> {
			UUID source = src.uniqueId();
			target.sendMessage(TextUtils.replace(getText(target, LocalesPaths.COMMANDS_TPA_REQUEST_MESSAGE), Placeholders.PLAYER, src.get(Keys.CUSTOM_NAME).orElse(text(src.name()))).clickEvent(SpongeComponents.executeCallback(cause -> {
				if(Sponge.server().player(source).isPresent()) {
					src.setLocation(target.serverLocation());
					src.sendMessage(getText(target, LocalesPaths.COMMANDS_TPA_ACCEPTED));
				} else target.sendMessage(getText(target, LocalesPaths.COMMANDS_TPA_SOURCE_OFFLINE));
			})));
			src.sendMessage(getText(locale, LocalesPaths.COMMANDS_TPA_SUCCESS));
		});
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(new ParameterSettings(CommandParameters.createPlayer(false), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT));
	}

	@Override
	protected String permission() {
		return Permissions.TPA;
	}

}
