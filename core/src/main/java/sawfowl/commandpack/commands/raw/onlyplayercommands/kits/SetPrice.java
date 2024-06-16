package sawfowl.commandpack.commands.raw.onlyplayercommands.kits;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.commands.raw.arguments.RawBasicArgumentData;
import sawfowl.commandpack.api.commands.raw.arguments.RawOptional;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.api.data.kits.KitPrice;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractKitsEditCommand;
import sawfowl.commandpack.configure.configs.kits.KitData;
import sawfowl.commandpack.utils.CommandsUtil;

public class SetPrice extends AbstractKitsEditCommand {

	public SetPrice(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, ServerPlayer src, Locale locale, Mutable arguments, RawArgumentsMap args) throws CommandException {
		Kit kit = args.<Kit>get(0).get();
		KitData kitData = (KitData) (kit instanceof KitData ? kit : Kit.builder().copyFrom(kit));
		if(args.getInput().length < 3) exception(getExceptions(locale).getValueNotPresent());
		kitData.setPrice(KitPrice.of(args.getCurrency(1).get(), args.getBigDecimal(2).get()));
		kitData.save();
		src.sendMessage(getCommands(locale).getKits().getSetPrice());
	}

	@Override
	public Component shortDescription(Locale locale) {
		return text("&3Specifying the price of the kit.");
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return text("&3Specifying the price of the kit.");
	}

	@Override
	public String command() {
		return "setprice";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/kits setprice <Kit> <Currency> <Price>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			kitArgument(0, false, false),
			RawArguments.createCurrencyArgument(RawBasicArgumentData.createCurrency(1, null, null), RawOptional.notOptional(), locale -> getExceptions(locale).getCurrencyNotPresent()),
			RawArguments.createBigDecimalArgument(CommandsUtil.getEmptyList(), new RawBasicArgumentData<BigDecimal>(null, "Price", 2, null, null), RawOptional.notOptional(), locale -> getExceptions(locale).getValueNotPresent())
		);
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
