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

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Item;

public class ClearLore extends AbstractPlayerCommand {

	public ClearLore(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		if(src.itemInHand(HandTypes.MAIN_HAND.get()).quantity() == 0) exception(getItem(locale).getEmptyHand());
		ItemStack item = src.itemInHand(HandTypes.MAIN_HAND.get());
		item.remove(Keys.LORE);
		src.setItemInHand(HandTypes.MAIN_HAND.get(), item);
		src.sendMessage(getItem(locale).getClearLore());
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

	private Item getItem(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getItem();
	}

}
