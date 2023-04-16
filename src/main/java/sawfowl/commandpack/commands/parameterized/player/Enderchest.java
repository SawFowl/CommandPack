package sawfowl.commandpack.commands.parameterized.player;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Enderchest extends AbstractPlayerCommand {

	public Enderchest(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		Optional<User> optUser = Optional.empty();
		try {
			optUser = getUser(context).isPresent() ? Sponge.server().userManager().load(getUser(context).get()).get() : Optional.empty();
		} catch (InterruptedException | ExecutionException e) {
			// ignore
		}
		if(optUser.isPresent()) {
			src.openInventory(optUser.get().enderChestInventory());
		} else src.openInventory(src.enderChestInventory());
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createUser(Permissions.ENDERCHEST_STAFF, true), true, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT));
	}

	@Override
	public String permission() {
		return Permissions.ENDERCHEST;
	}

	@Override
	public String command() {
		return "enderchest";
	}

}
