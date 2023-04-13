package sawfowl.commandpack.commands.parameterized.player.teleports;

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
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class TpaHereAll extends AbstractPlayerCommand {

	public TpaHereAll(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		delay(src, locale, consumer -> {
			UUID source = src.uniqueId();
			Sponge.server().onlinePlayers().forEach(target -> {
				if(!target.uniqueId().equals(src.uniqueId())) target.sendMessage(TextUtils.replace(getText(target, LocalesPaths.COMMANDS_TPA_REQUEST_HERE_MESSAGE), Placeholders.PLAYER, src.get(Keys.CUSTOM_NAME).orElse(text(src.name()))).clickEvent(SpongeComponents.executeCallback(cause -> {
					if(Sponge.server().player(source).isPresent()) {
						target.setLocation(src.serverLocation());
					} else target.sendMessage(getText(target, LocalesPaths.COMMANDS_TPA_SOURCE_OFFLINE));
				})));
			});
			src.sendMessage(getText(locale, LocalesPaths.COMMANDS_TPA_SUCCESS));
		});
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

	@Override
	public String permission() {
		return Permissions.TPA_HERE_ALL;
	}

	@Override
	public String command() {
		return "tpahereall";
	}

}
