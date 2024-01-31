package sawfowl.commandpack.configure.configs.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.Queries;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.api.data.command.Delay;
import sawfowl.commandpack.api.data.command.Price;

@ConfigSerializable
public class CommandSettings implements sawfowl.commandpack.api.data.command.Settings {

	public static final CommandSettings EMPTY = new CommandSettings();

	public CommandSettings() {}

	public Builder builder() {
		return new Builder();
	}

	@Setting("Aliases")
	private String[] aliases = {};
	@Setting("Cooldown")
	private long cooldown = 0;
	@Setting("DelayData")
	private DelayData delayData = new DelayData();
	@Setting("Enable")
	private boolean enable = true;
	@Setting("Price")
	private CommandPrice price = new CommandPrice();
	private Boolean autocomplete;
	private Boolean generateRawTree;
	private UUID uuid = UUID.randomUUID();

	@Override
	public String[] getAliases() {
		return aliases;
	}

	@Override
	public List<String> getAliasesList() {
		return aliases.length == 0 ? new ArrayList<>() : Stream.of(aliases).collect(Collectors.toList());
	}

	@Override
	public long getCooldown() {
		return cooldown;
	}

	@Override
	public DelayData getDelay() {
		return delayData;
	}

	@Override
	public CommandPrice getPrice() {
		return price;
	}

	@Override
	public boolean isEnable() {
		return enable;
	}

	@Override
	public Boolean isAutocomplete() {
		return autocomplete;
	}

	@Override
	public Boolean isGenerateRawTree() {
		return generateRawTree;
	}

	public CommandSettings addAlias(String alias) {
		List<String> newAliases = getAliasesList();
		newAliases.add(alias);
		aliases = newAliases.stream().toArray(String[]::new);
		return this;
	}

	public CommandSettings addAliases(List<String> aliases) {
		List<String> newAliases = getAliasesList();
		newAliases.addAll(aliases);
		this.aliases = newAliases.stream().toArray(String[]::new);
		return this;
	}

	@Override
	public int contentVersion() {
		return 1;
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this != obj || !(obj instanceof CommandSettings)) return false;
		return Objects.equals(uuid, ((CommandSettings) obj).uuid);
	}

	@Override
	public String toString() {
		return "CommandSettings [uuid=" + uuid + "]";
	}

	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew()
				.set(DataQuery.of("Aliases"), aliases)
				.set(DataQuery.of("Cooldown"), cooldown)
				.set(DataQuery.of("DelayData"), delayData)
				.set(DataQuery.of("Enable"), enable)
				.set(DataQuery.of("Price"), price)
				.set(DataQuery.of("Autocomplete"), autocomplete)
				.set(DataQuery.of("GenerateRawTree"), generateRawTree)
				.set(Queries.CONTENT_VERSION, contentVersion());
	}

	public class Builder implements sawfowl.commandpack.api.data.command.Settings.Builder {

		@Override
		public sawfowl.commandpack.api.data.command.Settings.Builder reset() {
			CommandSettings.this.aliases = new String[] {};
			CommandSettings.this.cooldown = 0;
			CommandSettings.this.delayData = new sawfowl.commandpack.configure.configs.commands.DelayData();
			CommandSettings.this.enable = true;
			CommandSettings.this.price = new CommandPrice();
			return this;
		}

		@Override
		public sawfowl.commandpack.api.data.command.Settings build() {
			return CommandSettings.this;
		}

		@Override
		public Builder setAliases(List<String> aliases) {
			CommandSettings.this.aliases = aliases.stream().toArray(String[]::new);
			return this;
		}

		@Override
		public Builder setAliases(String... aliases) {
			CommandSettings.this.aliases = aliases;
			return this;
		}

		@Override
		public Builder setCooldown(long cooldown) {
			CommandSettings.this.cooldown = cooldown;
			return this;
		}

		@Override
		public Builder setDelay(Delay delay) {
			CommandSettings.this.delayData = delay instanceof DelayData ? (DelayData) delay : DelayData.of(delay.getSeconds(), CancelRulesData.of(delay.getCancelRules().isAllowMoving(), delay.getCancelRules().isAllowOtherCommand()));
			return this;
		}

		@Override
		public Builder setEnable(boolean enable) {
			CommandSettings.this.enable = enable;
			return this;
		}

		@Override
		public Builder setPrice(Price price) {
			CommandSettings.this.price = price instanceof CommandPrice ? (CommandPrice) price : CommandPrice.of(price.getCurrency(), price.getMoney());
			return this;
		}

		@Override
		public Builder setRawAutoComplete(Boolean enable) {
			return this;
		}

		@Override
		public Builder generateRawCommandTree(Boolean enable) {
			return this;
		}
		
	}

}
