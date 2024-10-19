package sawfowl.commandpack.commands.raw.onlyplayercommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.inventory.ItemStack;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.commands.raw.arguments.RawBasicArgumentData;
import sawfowl.commandpack.api.commands.raw.arguments.RawOptional;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.utils.CommandsUtil;

@Register
public class Enchant extends AbstractPlayerCommand {

	List<String> enchants;
	public Enchant(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, ServerPlayer src, Locale locale, Mutable arguments, RawArgumentsMap args) throws CommandException {
		if(src.itemInHand(HandTypes.MAIN_HAND).quantity() == 0) exception(getCommands(locale).getEnchant().getItemNotPresent());
		EnchantmentType enchant = args.getEnchantmentType(0).get();
		if(args.getInput().length == 1) exception(getExceptions(locale).getValueNotPresent());
		int level = args.getInteger(1).get();
		ItemStack stack = src.itemInHand(HandTypes.MAIN_HAND);
		List<Enchantment> enchantments = stack.get(Keys.APPLIED_ENCHANTMENTS).orElse(new ArrayList<>());
		enchantments.add(Enchantment.builder().type(enchant).level(level).build());
		delay(src, locale, consumer -> {
			stack.offer(Keys.APPLIED_ENCHANTMENTS, enchantments);
			src.setItemInHand(HandTypes.MAIN_HAND, stack);
			src.sendMessage(getCommands(locale).getEnchant().getSuccess());
		});
	}

	@Override
	public String permission() {
		return Permissions.ENCHANT_STAFF;
	}

	@Override
	public String command() {
		return "enchant";
	}

	@Override
	public Component shortDescription(Locale locale) {
		return text("&bEnchant item in your hand");
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return text("&bEnchant item in your hand");
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/enchant <Enchantment> <Level>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			RawArguments.createEnchantmentArgument(RawBasicArgumentData.createEnchantmentType(0, null, null), RawOptional.notOptional(), locale -> getExceptions(locale).getTypeNotPresent()),
			RawArguments.createIntegerArgument(CommandsUtil.getEmptyList(), new RawBasicArgumentData<Integer>(null, "Level", 1, null, null), RawOptional.optional(), locale -> getExceptions(locale).getValueNotPresent())
		);
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
