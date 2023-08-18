package sawfowl.commandpack.apiclasses.economy;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.apiclasses.economy.storage.AbstractEconomyStorage;
import sawfowl.commandpack.configure.configs.economy.SerializedUniqueAccount;

public class CPUniqueAccount extends CPAccount implements UniqueAccount {

	private UUID userId;
	private CPUniqueAccount(){}
	public CPUniqueAccount(UUID userId, Map<Currency, BigDecimal> balances, AbstractEconomyStorage storage) {
		this.userId = userId;
		super.balances = balances.entrySet().stream().filter(entry -> entry.getKey() instanceof CPCurrency).collect(Collectors.toMap(entry -> (CPCurrency) entry.getKey(), entry -> entry.getValue()));
		super.storage = storage;
		Optional<ServerPlayer> optPlayer = Sponge.server().player(userId);
		if(optPlayer.isPresent()) {
			setName(optPlayer.get().name());
			return;
		}
		Optional<GameProfile> optProfile = Sponge.server().gameProfileManager().cache().findById(userId);
		if(optProfile.isPresent()) {
			setName(optProfile.get().name().orElse(optProfile.get().examinableName()));
			return;
		}
		Sponge.server().userManager().load(userId).thenAccept(optUser -> {
			if(optUser.isPresent()) {
				setName(optUser.get().name());
				return;
			}
		});
		save();
	}

	public static CPUniqueAccount create(UUID userId, String identifier, Map<Currency, BigDecimal> balances, AbstractEconomyStorage storage) {
		CPUniqueAccount uniqueAccount = new CPUniqueAccount();
		uniqueAccount.userId = userId;
		uniqueAccount.identifier = identifier;
		uniqueAccount.balances = balances.entrySet().stream().filter(entry -> entry.getKey() instanceof CPCurrency).collect(Collectors.toMap(entry -> (CPCurrency) entry.getKey(), entry -> entry.getValue()));;
		uniqueAccount.storage = storage;
		return uniqueAccount;
	}

	public static CPUniqueAccount deserealize(SerializedUniqueAccount account, AbstractEconomyStorage storage) {
		CPUniqueAccount uniqueAccount = new CPUniqueAccount();
		uniqueAccount.storage = storage;
		uniqueAccount.userId = account.getUserId();
		uniqueAccount.identifier = account.getName();
		uniqueAccount.balances = account.getBalances(storage.getCurrenciesMap());
		return uniqueAccount;
	}

	public void setName(String name) {
		super.identifier = name;
		save();
	}

	@Override
	public UUID uniqueId() {
		return userId;
	}

	@Override
	public String identifier() {
		return identifier;
	}

	@Override
	public Component displayName() {
		return text(identifier);
	}

	@Override
	public void save() {
		if(storage != null) storage.saveUniqueAccount(this);
	}

	@Override
	public String toString() {
		return "CPUniqueAccount [userId=" + userId + ", displayName=" + identifier + ", balances=" + balances + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		return Objects.equals(userId, ((CPUniqueAccount) obj).userId);
	}

}
