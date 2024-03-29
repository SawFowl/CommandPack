package sawfowl.commandpack.commands.parameterized.onlyplayercommands.teleports;

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
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

@Register
public class TpaHere extends AbstractPlayerCommand {

	public TpaHere(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		ServerPlayer target = getPlayer(context).get();
		if(target.uniqueId().equals(src.uniqueId())) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_TARGET_SELF);
		if(plugin.getPlayersData().getTempData().isDisableTpRequests(target)) exception(locale, LocalesPaths.COMMANDS_TPA_DISABLE_TP_REQUESTS);
		delay(src, locale, consumer -> {
			UUID source = src.uniqueId();
			TpaAccess access = new TpaAccess();
			target.sendMessage(getText(target, LocalesPaths.COMMANDS_TPA_REQUEST_HERE_MESSAGE).replace(Placeholders.PLAYER, src.get(Keys.CUSTOM_NAME).orElse(text(src.name()))).get().clickEvent(SpongeComponents.executeCallback(cause -> {
				if(!access.access) return;
				if(Sponge.server().player(source).isPresent()) {
					plugin.getPlayersData().getTempData().setPreviousLocation(target);
					target.setLocation(src.serverLocation());
					src.sendMessage(getComponent(target, LocalesPaths.COMMANDS_TPA_ACCEPTED));
				} else target.sendMessage(getComponent(target, LocalesPaths.COMMANDS_TPA_SOURCE_OFFLINE));
				access.access = false;
			})));
			src.sendMessage(getComponent(locale, LocalesPaths.COMMANDS_TPA_SUCCESS));
		});
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(false), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT));
	}

	@Override
	public String permission() {
		return Permissions.TPA_HERE;
	}

	@Override
	public String command() {
		return "tpahere";
	}

	private class TpaAccess {
		public boolean access = true;
	}

}
