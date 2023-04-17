package sawfowl.commandpack.commands.parameterized.player.teleports;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerLocation;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Back extends AbstractPlayerCommand {

	public Back(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		Optional<ServerLocation> location = plugin.getPlayersData().getTempData().getPreviousLocation(src);
		if(!location.isPresent()) exception(locale, LocalesPaths.COMMANDS_BACK_EMPTY);
		if(location.get().world() == null || !location.get().world().isLoaded()) exception(locale, LocalesPaths.COMMANDS_BACK_NOT_LOADED_WORLD);
		delay(src, locale, consumer -> {
			plugin.getPlayersData().getTempData().setPreviousLocation(src);
			src.setLocation(location.get());
		});
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.BACK;
	}

	@Override
	public String command() {
		return "back";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

}
