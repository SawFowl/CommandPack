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
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class ClearName extends AbstractPlayerCommand {

	public ClearName(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		if(src.itemInHand(HandTypes.MAIN_HAND.get()).quantity() == 0) exception(locale, LocalesPaths.COMMANDS_ITEM_EMPTY_HAND);
		ItemStack item = src.itemInHand(HandTypes.MAIN_HAND.get());
		item.remove(Keys.CUSTOM_NAME);
		src.setItemInHand(HandTypes.MAIN_HAND.get(), item);
		src.sendMessage(getText(locale, LocalesPaths.COMMANDS_ITEM_CLEAR_NAME));
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.ITEMNAME_STAFF;
	}

	@Override
	public String command() {
		return "clear";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

}
