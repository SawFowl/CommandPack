package sawfowl.commandpack.commands.parameterized.player;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.parameterized.settings.CommandParameters;
import sawfowl.commandpack.commands.parameterized.settings.ParameterSettings;
import sawfowl.commandpack.configure.configs.commands.CommandSettings;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class InventorySee extends AbstractPlayerCommand {

	public InventorySee(CommandPack plugin, String command, CommandSettings commandSettings) {
		super(plugin, command, commandSettings);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		getUser(context).ifPresent(name -> {
			try {
				Sponge.server().userManager().load(name).get().ifPresent(user -> {
					src.openInventory(user.inventory());
				});
			} catch (InterruptedException | ExecutionException e) {
				plugin.getLogger().error(e.getLocalizedMessage());
			}
		});
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(new ParameterSettings(CommandParameters.createUser(false), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT));
	}

	@Override
	protected String permission() {
		return Permissions.INVENTORYSEE_STAFF;
	}

}
