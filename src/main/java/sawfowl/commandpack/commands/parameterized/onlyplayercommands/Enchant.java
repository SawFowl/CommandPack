package sawfowl.commandpack.commands.parameterized.onlyplayercommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Enchant extends AbstractPlayerCommand {

	public Enchant(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		delay(src, locale, consumer -> {
			if(src.itemInHand(HandTypes.MAIN_HAND).quantity() == 0) exception(locale, LocalesPaths.COMMANDS_ENCHANT_ITEM_IS_NOT_PRESENT);
			String type = getString(context, "Enchant", null);
			if(type == null) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_TYPE_NOT_PRESENT);
			Optional<EnchantmentType> optEnchant = EnchantmentTypes.registry().findValue(ResourceKey.resolve(type));
			if(!optEnchant.isPresent()) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_TYPE_NOT_PRESENT);
			int level = getArgument(context, Integer.class, "Level").orElse(1);
			ItemStack stack = src.itemInHand(HandTypes.MAIN_HAND);
			List<Enchantment> enchantments = stack.get(Keys.APPLIED_ENCHANTMENTS).orElse(new ArrayList<>());
			enchantments.add(Enchantment.builder().type(optEnchant.get()).level(level).build());
			stack.offer(Keys.APPLIED_ENCHANTMENTS, enchantments);
			src.setItemInHand(HandTypes.MAIN_HAND, stack);
			src.sendMessage(getText(locale, LocalesPaths.COMMANDS_ENCHANT_SUCCES));
		});
	}

	@Override
	public Parameterized build() {
		return builder().reset()
				.permission(permission())
				.addParameters(getParameterSettings().stream().map(ParameterSettings::getParameterUnknownType).collect(Collectors.toList()))
				.executor(this)
				.build();
	}

	@Override
	public String permission() {
		return Permissions.ENCHANT_STAFF;
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(
			ParameterSettings.of(CommandParameters.ENCHANT, false, 0, LocalesPaths.COMMANDS_EXCEPTION_TYPE_NOT_PRESENT),
			ParameterSettings.of(CommandParameters.createInteger("Level", true), true, 1, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

	@Override
	public String command() {
		return "enchant";
	}

}
