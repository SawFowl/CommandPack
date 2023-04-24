package sawfowl.commandpack.commands.parameterized.onlyplayercommands.items;

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
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class SetName extends AbstractPlayerCommand {

	public SetName(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		if(src.itemInHand(HandTypes.MAIN_HAND.get()).quantity() == 0) exception(locale, LocalesPaths.COMMANDS_ITEM_EMPTY_HAND);
		Component newName = text(getString(context, "Value").get());
		ItemStack item = src.itemInHand(HandTypes.MAIN_HAND.get());
		item.offer(Keys.CUSTOM_NAME, newName);
		src.setItemInHand(HandTypes.MAIN_HAND.get(), item);
		src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_ITEM_SET_NAME), Placeholders.VALUE, newName));
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
		return "set";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createStrings("Value", false), false, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT));
	}

}
