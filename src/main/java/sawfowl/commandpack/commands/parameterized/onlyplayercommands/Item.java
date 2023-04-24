package sawfowl.commandpack.commands.parameterized.onlyplayercommands;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.parameterized.onlyplayercommands.items.ItemLore;
import sawfowl.commandpack.commands.parameterized.onlyplayercommands.items.ItemName;

public class Item extends AbstractPlayerCommand {

	public Item(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
	}

	@Override
	public Parameterized build() {
		return Command.builder()
				.executionRequirements(cause -> cause.root() instanceof ServerPlayer && (cause.hasPermission(Permissions.ITEMNAME_STAFF) || cause.hasPermission(Permissions.ITEMLORE_STAFF)))
				.addChild(new ItemName(plugin).build(), "itemname", "name")
				.addChild(new ItemLore(plugin).build(), "itemlore", "lore")
				.build();
	}

	@Override
	public String permission() {
		return null;
	}

	@Override
	public String command() {
		return "item";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

}
