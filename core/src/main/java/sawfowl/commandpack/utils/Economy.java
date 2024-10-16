package sawfowl.commandpack.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.lifecycle.ProvideServiceEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.apiclasses.economy.EconomyServiceImpl;
import sawfowl.localeapi.api.TextUtils;

public class Economy {

	private final CommandPackInstance plugin;
	private EconomyService economyService;
	private EconomyServiceImpl economyServiceImpl;

	public Economy(CommandPackInstance plugin) {
		this.plugin = plugin;
		economyService = economyServiceImpl = new EconomyServiceImpl(plugin);
	}

	public Economy createEconomy(ProvideServiceEvent<EconomyService> event) {
		event.suggest(() -> economyService);
		return this;
	}

	public EconomyServiceImpl getEconomyServiceImpl() {
		return economyServiceImpl;
	}

	public EconomyService getEconomyService() {
		return economyService;
	}

	public BigDecimal getPlayerBalance(UUID uuid, Currency currency) {
		return economyServiceImpl.findOrCreateAccount(uuid).map(a -> a.balance(currency)).orElse(BigDecimal.ZERO);
	}

	public boolean isPresent() {
		return economyService != null;
	}

	public boolean checkPlayerBalance(UUID uuid, Currency currency, BigDecimal money) {
		return isPresent() && getPlayerBalance(uuid, currency).doubleValue() >= money.doubleValue();
	}

	public boolean addToPlayerBalance(ServerPlayer player, Currency currency, BigDecimal money) {
		if(economyService == null) return false;
		try {
			Optional<UniqueAccount> uOpt = economyService.findOrCreateAccount(player.uniqueId());
			if (uOpt.isPresent()) {
				TransactionResult result = uOpt.get().deposit(currency, money);
				if (result.result() == ResultType.SUCCESS) {
					return true;
				} else if ((result.result() == ResultType.FAILED || result.result() == ResultType.ACCOUNT_NO_FUNDS) && plugin.getMainConfig().isDebugEconomy()) {
					plugin.getLogger().error(plugin.getLocales().getLocale(player).getDebug().getEconomy().getErrorGiveMoney(player.name()));
				} else {
				}
				}
			} catch (Exception ignored) {
		}
		return false;
	}

	public boolean removeFromPlayerBalance(ServerPlayer player, Currency currency, BigDecimal money) {
		if(economyService == null) return false;
		try {
			Optional<UniqueAccount> uOpt = economyService.findOrCreateAccount(player.uniqueId());
			if (uOpt.isPresent()) {
				TransactionResult result = uOpt.get().withdraw(currency, money);
				if (result.result() == ResultType.SUCCESS) {
					return true;
				} else if ((result.result() == ResultType.FAILED || result.result() == ResultType.ACCOUNT_NO_FUNDS) && plugin.getMainConfig().isDebugEconomy()) {
					plugin.getLogger().error(plugin.getLocales().getLocale(player).getDebug().getEconomy().getErrorTakeMoney(player.name()));
				} else {
				}
				}
			} catch (Exception ignored) {
		}
		return false;
	}

	public Currency checkCurrency(String check) {
		if(economyService == null) return null;
		if(check == null) return economyService.defaultCurrency();
		Optional<Currency> optCurrency = getCurrencies().stream().filter(currency -> (TextUtils.clearDecorations(currency.displayName()).equalsIgnoreCase(check) || TextUtils.clearDecorations(currency.symbol()).equalsIgnoreCase(check))).findFirst();
		return optCurrency.isPresent() ? optCurrency.get() : economyService.defaultCurrency();
	}

	public List<Currency> getCurrencies() {
		if(economyServiceImpl != null) return Arrays.asList(economyServiceImpl.getCurrencies());
		List<Currency> currencies = new ArrayList<Currency>();
		if(economyService == null) return currencies;
		Sponge.game().findRegistry(RegistryTypes.CURRENCY).ifPresent(registry -> {
			if(registry.stream().count() > 0) currencies.addAll(registry.stream().collect(Collectors.toList()));
		});
		return !currencies.isEmpty() ? currencies : Arrays.asList(economyService.defaultCurrency());
	}

	@Listener(order = Order.LAST)
	public void onProvideEconomyService(StartedEngineEvent<Server> event) {
		if(economyService == null) economyService = Sponge.server().serviceProvider().economyService().orElse(null);
		Sponge.eventManager().unregisterListeners(this);
	}

}
