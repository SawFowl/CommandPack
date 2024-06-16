package sawfowl.commandpack.commands.raw.economy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.economy.Currency;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextDecoration;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.commands.raw.arguments.RawBasicArgumentData;
import sawfowl.commandpack.api.commands.raw.arguments.RawOptional;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.localeapi.api.TextUtils;

@Register
public class BalanceTop extends AbstractRawCommand {

	public BalanceTop(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		Currency currency = args.getCurrency(0).orElse(plugin.getEconomy().getEconomyServiceImpl().defaultCurrency());
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) audience;
			delay(player, locale, condumer -> {
				sendTop(audience, locale, currency, player.hasPermission(Permissions.BALANCE_HIDEN_VIEW), player.name());
			});
		} else sendTop(audience, locale, currency, true, null);
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
	public boolean canExecute(CommandCause cause) {
		return cause.hasPermission(permission()) && cause.hasPermission(Permissions.BALANCE_OTHER);
	}

	@Override
	public String permission() {
		return Permissions.BALANCE_TOP;
	}

	@Override
	public String command() {
		return "balancetop";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/balancetop [Currency]");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(RawArguments.createCurrencyArgument(RawBasicArgumentData.createCurrency(0, null, null), new RawOptional(true, true), locale -> getExceptions(locale).getValueNotPresent()));
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

	private void sendTop(Audience audience, Locale locale, Currency currency, boolean viewHide, String name) {
		boolean nullableName = name == null;
		Sponge.asyncScheduler().submit(Task.builder().plugin(getContainer()).execute(() -> {
			Map<Component, Double> balances = new HashMap<Component, Double>();
			plugin.getEconomy().getEconomyServiceImpl().streamUniqueAccounts().forEach(account -> {
				if(viewHide || !plugin.getEconomy().getEconomyServiceImpl().isHiden(account) || (!nullableName && account.identifier().equals(name))) balances.put(account.displayName(), account.balance(currency).doubleValue());
			});
			List<Component> top = new ArrayList<Component>();
			balances.entrySet().stream().sorted(Map.Entry.<Component, Double>comparingByValue().reversed()).forEach(entry -> {
				if(!nullableName && TextUtils.clearDecorations(entry.getKey()).equals(name)) {
					top.add(getBalanceTop(locale).getElement(text(top.size() + 1).decorate(TextDecoration.ITALIC), entry.getKey().decorate(TextDecoration.ITALIC), text(entry.getValue()).decorate(TextDecoration.ITALIC)));
				} else top.add(getBalanceTop(locale).getElement(text(top.size() + 1), display(entry.getKey()), text(entry.getValue())));
			});
			Component title = getBalanceTop(locale).getTitle(currency);
			sendPaginationList(audience, title, getBalanceTop(locale).getPadding(), 10, top);
			balances.clear();
			top.clear();
			title = null;
		}).build());
	}

	private Component display(Component component) {
		String name = TextUtils.clearDecorations(component);
		return Sponge.server().player(name).isPresent() ? component.clickEvent(ClickEvent.suggestCommand("/tell " + name + " ")) : component;
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.BalanceTop getBalanceTop(Locale locale) {
		return getCommands(locale).getBalanceTop();
	}

}
