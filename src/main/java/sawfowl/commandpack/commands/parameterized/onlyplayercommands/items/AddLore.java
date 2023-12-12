package sawfowl.commandpack.commands.parameterized.onlyplayercommands.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class AddLore extends AbstractPlayerCommand {

	public AddLore(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		if(src.itemInHand(HandTypes.MAIN_HAND.get()).quantity() == 0) exception(locale, LocalesPaths.COMMANDS_ITEM_EMPTY_HAND);
		ItemStack item = src.itemInHand(HandTypes.MAIN_HAND.get());
		String input = getString(context, "Value").get();
		List<Component> newLore = item.get(Keys.LORE).orElse(new ArrayList<>());
		if(input.contains("\\n")) {
			input = input.replace("\\n", "%NEWLINE%");
			for(String split : input.split("%NEWLINE%")) {
				newLore.add(text(split));
			}
		} else newLore.add(text(input));
		item.offer(Keys.LORE, newLore);
		src.setItemInHand(HandTypes.MAIN_HAND, item);
		src.sendMessage(getText(locale, LocalesPaths.COMMANDS_ITEM_SET_LORE));
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
		return "addlore";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createStrings("Value", false), false, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT));
	}

}
