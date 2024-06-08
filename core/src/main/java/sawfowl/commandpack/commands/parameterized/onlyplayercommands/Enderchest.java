package sawfowl.commandpack.commands.parameterized.onlyplayercommands;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Enderchest extends AbstractPlayerCommand {

	public Enderchest(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		if(!getUser(context).isPresent()) {
			delay(src, locale, consumer -> {
				src.openInventory(src.enderChestInventory());
			});
			return;
		}
		if(Sponge.server().player(getUser(context).get()).isPresent()) {
			src.openInventory(Sponge.server().player(getUser(context).get()).get().enderChestInventory());
			return;
		}
		if(getUser(context).isPresent()) {
			Sponge.server().userManager().load(getUser(context).get()).thenAccept(optUser -> {
				try {
					delay(src, locale, consumer -> {
						if(optUser.isPresent()) {
							src.openInventory(optUser.get().enderChestInventory());
						} else src.openInventory(src.enderChestInventory());
					});
				} catch (CommandException e) {
					src.sendMessage(e.componentMessage());
				}
			});
		} else delay(src, locale, consumer -> src.openInventory(src.enderChestInventory()));
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createUser(Permissions.ENDERCHEST_STAFF, true), true, locale -> getExceptions(locale).getUserNotPresent()));
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
