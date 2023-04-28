package sawfowl.commandpack.configure.configs.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.Queries;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class CommandSettings implements sawfowl.commandpack.api.data.command.Settings {

	public static final CommandSettings EMPTY = new CommandSettings();

	public CommandSettings() {}

	public CommandSettings(String[] aliases) {
		this.aliases = aliases;
	}

	public CommandSettings(Delay delay) {
		this.delay = delay;
	}

	public CommandSettings(long cooldown) {
		this.cooldown = cooldown;
	}

	public CommandSettings(long cooldown, Delay delay) {
		this.cooldown = cooldown;
		this.delay = delay;
	}

	public CommandSettings(long cooldown, String[] aliases) {
		this.cooldown = cooldown;
		this.aliases = aliases;
	}

	public CommandSettings(Delay delay, String[] aliases) {
		this.delay = delay;
		this.aliases = aliases;
	}

	public CommandSettings(CommandPrice price, String[] aliases) {
		this.price = price;
		this.aliases = aliases;
	}

	public CommandSettings(Delay delay, CommandPrice price) {
		this.delay = delay;
		this.price = price;
	}

	public CommandSettings(long cooldown, Delay delay, CommandPrice price) {
		this.cooldown = cooldown;
		this.delay = delay;
		this.price = price;
	}

	public CommandSettings(long cooldown, Delay delay, String[] aliases) {
		this.cooldown = cooldown;
		this.delay = delay;
		this.aliases = aliases;
	}

	public CommandSettings(long cooldown, Delay delay, CommandPrice price, String[] aliases) {
		this.cooldown = cooldown;
		this.delay = delay;
		this.price = price;
		this.aliases = aliases;
	}

	public Builder builder() {
		return new Builder();
	}

	@Setting("Aliases")
	private String[] aliases = {};
	@Setting("Cooldown")
	private long cooldown = 0;
	@Setting("Delay")
	private Delay delay = new Delay();
	@Setting("Enable")
	private boolean enable = true;
	@Setting("Price")
	private CommandPrice price = new CommandPrice();

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
	public Delay getDelay() {
		return delay;
	}

	@Override
	public CommandPrice getPrice() {
		return price;
	}

	@Override
	public boolean isEnable() {
		return enable;
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
	public String toString() {
		return "Settings [Aliases=" + aliases + ", Cooldown=" + cooldown + ", Delay=" + delay + ", Enable=" + enable + ", Price=" + price + "]";
	}

	@Override
	public int contentVersion() {
		return 1;
	}

	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew()
				.set(DataQuery.of("Aliases"), aliases)
				.set(DataQuery.of("Cooldown"), cooldown)
				.set(DataQuery.of("Delay"), delay)
				.set(DataQuery.of("Enable"), enable)
				.set(DataQuery.of("Price"), price)
				.set(Queries.CONTENT_VERSION, contentVersion());
	}

	public class Builder implements sawfowl.commandpack.api.data.command.Settings.Builder {

		@Override
		public sawfowl.commandpack.api.data.command.Settings.Builder reset() {
			CommandSettings.this.aliases = new String[] {};
			CommandSettings.this.cooldown = 0;
			CommandSettings.this.delay = new sawfowl.commandpack.configure.configs.commands.Delay();
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
			CommandSettings.this.aliases = Stream.of(aliases).toArray(String[]::new);
			return this;
		}

		@Override
		public Builder setAliases(String[] aliases) {
			CommandSettings.this.aliases = aliases;
			return this;
		}

		@Override
		public Builder setCooldown(long cooldown) {
			CommandSettings.this.cooldown = cooldown;
			return this;
		}

		@Override
		public Builder setDelay(sawfowl.commandpack.api.data.command.Delay delay) {
			CommandSettings.this.delay = delay instanceof sawfowl.commandpack.configure.configs.commands.Delay ? (sawfowl.commandpack.configure.configs.commands.Delay) delay : new sawfowl.commandpack.configure.configs.commands.Delay(delay.getSeconds(), new CancelRules(delay.getCancelRules().isAllowMoving(), delay.getCancelRules().isAllowOtherCommand()));
			return this;
		}

		@Override
		public Builder setEnable(boolean enable) {
			CommandSettings.this.enable = enable;
			return this;
		}

		@Override
		public Builder setPrice(sawfowl.commandpack.api.data.command.Price price) {
			CommandSettings.this.price = price instanceof CommandPrice ? (CommandPrice) price : new CommandPrice(price.getCurrency(), price.getMoney());
			return this;
		}
		
	}

}
