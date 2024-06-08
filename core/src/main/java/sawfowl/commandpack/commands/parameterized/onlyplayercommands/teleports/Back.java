package sawfowl.commandpack.commands.parameterized.onlyplayercommands.teleports;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerLocation;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Back extends AbstractPlayerCommand {

	public Back(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		Optional<ServerLocation> location = plugin.getPlayersData().getTempData().getPreviousLocation(src);
		if(!location.isPresent()) exception(getBack(locale).getNotFound());
		if(location.get().world() == null || !Sponge.server().worldManager().worldExists(location.get().worldKey()) || !location.get().world().isLoaded()) exception(getBack(locale).getNotLoadedWorld());
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

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Back getBack(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getBack();
	}

}
