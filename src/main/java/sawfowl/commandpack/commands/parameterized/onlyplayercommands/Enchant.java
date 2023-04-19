package sawfowl.commandpack.commands.parameterized.onlyplayercommands;

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
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentType;

import sawfowl.commandpack.CommandPackPlugin;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Enchant extends AbstractPlayerCommand {

	public Enchant(CommandPackPlugin plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		delay(src, locale, consumer -> {
			if(src.itemInHand(HandTypes.MAIN_HAND).quantity() == 0) exception(locale, LocalesPaths.COMMANDS_ENCHANT_ITEM_IS_NOT_PRESENT);
			EnchantmentType type = getArgument(context, EnchantmentType.class, "Enchant").get();
			int level = getArgument(context, Integer.class, "Level").orElse(1);
			List<Enchantment> enchantments = src.itemInHand(HandTypes.MAIN_HAND).get(Keys.STORED_ENCHANTMENTS).orElse(new ArrayList<>());
			enchantments.add(Enchantment.builder().type(type).level(level).build());
			src.itemInHand(HandTypes.MAIN_HAND).offer(Keys.STORED_ENCHANTMENTS, enchantments);
			src.sendMessage(getText(locale, LocalesPaths.COMMANDS_ENCHANT_SUCCES));
		});
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.ENCHANT_STAFF;
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(
			ParameterSettings.of(CommandParameters.ENCHANT, false, LocalesPaths.COMMANDS_EXCEPTION_TYPE_NOT_PRESENT),
			ParameterSettings.of(CommandParameters.createInteger("Level", true), true, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

	@Override
	public String command() {
		return "Enchant";
	}

}
