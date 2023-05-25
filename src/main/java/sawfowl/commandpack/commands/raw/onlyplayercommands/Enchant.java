package sawfowl.commandpack.commands.raw.onlyplayercommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.RawArgument;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractPlayerCommand;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Enchant extends AbstractPlayerCommand {

	List<String> enchants;
	public Enchant(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, ServerPlayer src, Locale locale, String[] args, Mutable arguments) throws CommandException {
		delay(src, locale, consumer -> {
			if(src.itemInHand(HandTypes.MAIN_HAND).quantity() == 0) exception(locale, LocalesPaths.COMMANDS_ENCHANT_ITEM_IS_NOT_PRESENT);
			if(args.length == 0) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_TYPE_NOT_PRESENT);
			String type = args[0];
			Optional<EnchantmentType> optEnchant = EnchantmentTypes.registry().findValue(ResourceKey.resolve(type));
			if(!optEnchant.isPresent()) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_TYPE_NOT_PRESENT);
			if(args.length == 1) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT);
			int level = NumberUtils.isParsable(args[1]) ? NumberUtils.createInteger(args[1]) : 1;
			ItemStack stack = src.itemInHand(HandTypes.MAIN_HAND);
			List<Enchantment> enchantments = stack.get(Keys.APPLIED_ENCHANTMENTS).orElse(new ArrayList<>());
			enchantments.add(Enchantment.builder().type(optEnchant.get()).level(level).build());
			stack.offer(Keys.APPLIED_ENCHANTMENTS, enchantments);
			src.setItemInHand(HandTypes.MAIN_HAND, stack);
			src.sendMessage(getText(locale, LocalesPaths.COMMANDS_ENCHANT_SUCCES));
		});
	}

	@Override
	public List<CommandCompletion> complete(CommandCause cause, List<String> args, Mutable arguments, String currentInput) throws CommandException {
		if(!plugin.getMainConfig().isAutoCompleteRawCommands() || (plugin.getPlayersData().getAdminWarps().isEmpty() && plugin.getPlayersData().getPlayersWarps().isEmpty())) return getEmptyCompletion();
		if(enchants == null) enchants = EnchantmentTypes.registry().streamEntries().map(e -> (e.key().asString())).collect(Collectors.toList());
		if((args.isEmpty() || args.size() == 1) && !currentInput.endsWith(" ")) return enchants.stream().filter(e -> (currentInput.length() == 0 || e.startsWith(currentInput) || (e.contains(":") && !e.endsWith(":") && e.split(":")[1].startsWith(currentInput)) || (currentInput.contains(e) && !currentInput.contains(e + " ")))).map(e -> CommandCompletion.of(e, text("&3Enchant"))).collect(Collectors.toList());
		return getEmptyCompletion();
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
	public List<RawArgument<?>> getArguments() {
		return null;
	}

}
