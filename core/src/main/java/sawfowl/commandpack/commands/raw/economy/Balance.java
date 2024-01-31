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
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

@Register
public class Balance extends AbstractRawCommand {

	public Balance(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		if(isPlayer) {
			ServerPlayer source = (ServerPlayer) audience;
			Optional<UniqueAccount> optTarget = getArgument(UniqueAccount.class, args, 0);
			delay(source, locale, consumer -> {
				List<Component> messages = new ArrayList<Component>();
				if(optTarget.isPresent()) {
					if(plugin.getEconomy().getEconomyServiceImpl().isHiden(optTarget.get()) && !source.hasPermission(Permissions.BALANCE_HIDEN_VIEW) && !source.hasPermission(Permissions.ECONOMY_STAFF)) exception(getText(locale, LocalesPaths.COMMANDS_BALANCE_OTHER_HIDEN).replace(Placeholders.PLAYER, optTarget.get().displayName()).get());
					Component title = getText(locale, LocalesPaths.COMMANDS_BALANCE_OTHER_TITLE).replace(Placeholders.PLAYER, optTarget.get().displayName()).get();
					for(Currency currency : plugin.getEconomy().getEconomyServiceImpl().getCurrencies()) {
						if(plugin.getEconomy().getEconomyServiceImpl().defaultCurrency().equals(currency) || source.hasPermission(Permissions.getCurrencyAccess(currency))) {
							double balance = optTarget.get().balance(currency).doubleValue();
							Component message = getText(locale, LocalesPaths.COMMANDS_BALANCE_OTHER_LIST).replace(new String[] {Placeholders.CURRENCY_SYMBOL, Placeholders.CURRENCY_STYLED_SYMBOL, Placeholders.CURRENCY_NAME, Placeholders.CURRENCY_PLURAL_NAME, Placeholders.VALUE}, currency.symbol(), currency.symbol().style(currency.displayName().style()), currency.displayName(), currency.pluralDisplayName(), text(balance)).get();
							messages.add(message);
						}
					}
					sendPaginationList(source, title, text("=").color(title.color()), 10, messages);
				} else {
					Component title = getComponent(source.locale(), LocalesPaths.COMMANDS_BALANCE_SELF_TITLE);
					for(Currency currency : plugin.getEconomy().getEconomyServiceImpl().getCurrencies()) {
						double balance = plugin.getEconomy().getPlayerBalance(source.uniqueId(), currency).doubleValue();
						Component message = getText(source.locale(), LocalesPaths.COMMANDS_BALANCE_SELF_LIST).replace(new String[] {Placeholders.CURRENCY_SYMBOL, Placeholders.CURRENCY_STYLED_SYMBOL, Placeholders.CURRENCY_NAME, Placeholders.CURRENCY_PLURAL_NAME, Placeholders.VALUE}, currency.symbol(), currency.symbol().style(currency.displayName().style()), currency.displayName(), currency.pluralDisplayName(), text(balance)).get();
						messages.add(message);
					}
					sendPaginationList(source, title, text("=").color(title.color()), 10, messages);
				};
			});
		} else {
			UniqueAccount account = getArgument(UniqueAccount.class, args, 0).get();
			audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_BALANCE_OTHER_TITLE).replace(Placeholders.PLAYER, account.displayName()).get().append(text(":")));
			for(Currency currency : plugin.getEconomy().getEconomyServiceImpl().getCurrencies()) {
				double balance = account.balance(currency).doubleValue();
				Component message = getText(locale, LocalesPaths.COMMANDS_BALANCE_OTHER_LIST).replace(new String[] {Placeholders.CURRENCY_SYMBOL, Placeholders.CURRENCY_STYLED_SYMBOL, Placeholders.CURRENCY_NAME, Placeholders.CURRENCY_PLURAL_NAME, Placeholders.VALUE}, currency.symbol(), currency.symbol().style(currency.displayName().style()), currency.displayName(), currency.pluralDisplayName(), text(balance)).get();
				audience.sendMessage(message);
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
		return Arrays.asList(RawArguments.createUniqueAccountArgument(true, false, 0, null, LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_PRESENT));
	}

	@Override
	public void register(RegisterCommandEvent<Raw> event) {
		if(!plugin.getMainConfig().getEconomy().isEnable()) return;
		if(getCommandSettings() == null) {
			event.register(getContainer(), this, command());
		} else {
			if(!getCommandSettings().isEnable()) return;
			if(getCommandSettings().getAliases() != null && getCommandSettings().getAliases().length > 0) {
				event.register(getContainer(), this, command(), getCommandSettings().getAliases());
			} else event.register(getContainer(), this, command());
		}
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
