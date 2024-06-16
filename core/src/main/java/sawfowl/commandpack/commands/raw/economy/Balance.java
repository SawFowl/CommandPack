package sawfowl.commandpack.commands.raw.economy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.commands.raw.arguments.RawBasicArgumentData;
import sawfowl.commandpack.api.commands.raw.arguments.RawOptional;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Balance extends AbstractRawCommand {

	public Balance(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		if(isPlayer) {
			ServerPlayer source = (ServerPlayer) audience;
			Optional<UniqueAccount> optTarget = args.get(0);
			delay(source, locale, consumer -> {
				List<Component> messages = new ArrayList<Component>();
				if(optTarget.isPresent()) {
					if(plugin.getEconomy().getEconomyServiceImpl().isHiden(optTarget.get()) && !source.hasPermission(Permissions.BALANCE_HIDEN_VIEW) && !source.hasPermission(Permissions.ECONOMY_STAFF)) exception(getBalance(locale).getHiden(optTarget.get().displayName()));
					Component title = getBalance(locale).getTitleOther(optTarget.get().displayName());
					for(Currency currency : plugin.getEconomy().getEconomyServiceImpl().getCurrencies()) {
						if(plugin.getEconomy().getEconomyServiceImpl().defaultCurrency().equals(currency) || source.hasPermission(Permissions.getCurrencyAccess(currency))) {
							messages.add(getBalance(locale).getElementOther(currency, optTarget.get().balance(currency)));
						}
					}
					sendPaginationList(source, title, text("=").color(title.color()), 10, messages);
				} else {
					Component title = getBalance(locale).getTitle();
					for(Currency currency : plugin.getEconomy().getEconomyServiceImpl().getCurrencies()) {
						messages.add(getBalance(locale).getElement(currency, plugin.getEconomy().getPlayerBalance(source.uniqueId(), currency)));
					}
					sendPaginationList(source, title, text("=").color(title.color()), 10, messages);
				};
			});
		} else {
			UniqueAccount account = args.<UniqueAccount>get(0).get();
			audience.sendMessage(getBalance(locale).getTitleOther(account.displayName()).append(text(":")));
			for(Currency currency : plugin.getEconomy().getEconomyServiceImpl().getCurrencies()) {
				audience.sendMessage(getBalance(locale).getElementOther(currency, account.balance(currency)));
			}
		}
	}

	@Override
	public Component shortDescription(Locale locale) {
		return null;
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return null;
	}

	@Override
	public String permission() {
		return Permissions.BALANCE;
	}

	@Override
	public String command() {
		return "balance";
	}

	@Override
	public Component usage(CommandCause cause) {
		return cause.subject() instanceof ServerPlayer ? text("&c/balance [User]") : text("&c/balance <User>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(RawArguments.createUniqueAccountArgument(RawBasicArgumentData.createUniqueAccount(0, null, null), new RawOptional(true, false), locale -> getExceptions(locale).getUserNotPresent()));
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

	@Override
	public boolean isEnable() {
		return plugin.getMainConfig().getEconomy().isEnable();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Balance getBalance(Locale locale) {
		return getCommands(locale).getBalance();
	}

}
