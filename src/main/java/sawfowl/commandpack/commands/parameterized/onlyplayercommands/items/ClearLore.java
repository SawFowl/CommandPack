package sawfowl.commandpack.commands.parameterized.onlyplayercommands.items;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.ItemStack;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class ClearLore extends AbstractPlayerCommand {

	public ClearLore(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		if(src.itemInHand(HandTypes.MAIN_HAND.get()).quantity() == 0) exception(locale, LocalesPaths.COMMANDS_ITEM_EMPTY_HAND);
		ItemStack item = src.itemInHand(HandTypes.MAIN_HAND.get());
		item.remove(Keys.LORE);
		src.setItemInHand(HandTypes.MAIN_HAND.get(), item);
		src.sendMessage(getComponent(locale, LocalesPaths.COMMANDS_ITEM_CLEAR_LORE));
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.ITEMLORE_STAFF;
	}

	@Override
	public String command() {
		return "clearlore";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

}
