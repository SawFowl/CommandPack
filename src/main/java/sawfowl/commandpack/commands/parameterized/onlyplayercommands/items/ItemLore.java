package sawfowl.commandpack.commands.parameterized.onlyplayercommands.items;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;

public class ItemLore extends AbstractPlayerCommand {

	public ItemLore(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
	}

	@Override
	public Parameterized build() {
		return Command.builder()
				.permission(permission())
				.addChild(new SetLore(plugin).build(), "set", "setlore")
				.addChild(new AddLore(plugin).build(), "add", "addlore")
				.addChild(new ClearLore(plugin).build(), "clear", "clearlore")
				.build();
	}

	@Override
	public String permission() {
		return Permissions.ITEMLORE_STAFF;
	}

	@Override
	public String command() {
		return "itemlore";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

}
