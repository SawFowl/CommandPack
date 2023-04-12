package sawfowl.commandpack.configure.configs.commands;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.Command.Raw;
import org.spongepowered.api.command.manager.CommandFailedRegistrationException;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.value.CollectionValue;
import org.spongepowered.api.data.value.MapValue;
import org.spongepowered.api.data.value.MergeFunction;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.api.data.value.ValueContainer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;

@ConfigSerializable
public class CommandSettings implements sawfowl.commandpack.api.data.miscellaneous.CommandSettings {

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
		return "CommandSettings [Aliases=" + aliases + ", Cooldown=" + cooldown + ", Delay=" + delay + ", Enable=" + enable + ", Price=" + price + "]";
	}

	@Override
	public void registerParameterized(Class<? extends AbstractParameterizedCommand> clazz, RegisterCommandEvent<Parameterized> event) {
		try {
			clazz.getConstructor(CommandSettings.class).newInstance(this).register(event);
		} catch (CommandFailedRegistrationException | InstantiationException | IllegalAccessException| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void registerRaw(Class<? extends AbstractRawCommand> clazz, RegisterCommandEvent<Raw> event) {
		try {
			clazz.getConstructor(CommandSettings.class).newInstance(this).register(event);
		} catch (CommandFailedRegistrationException | InstantiationException | IllegalAccessException| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

	public class Builder implements sawfowl.commandpack.api.data.miscellaneous.CommandSettings.Builder {

		CommandSettings settings;
		public Builder() {
			settings = new CommandSettings();
		}

		@Override
		public <V> sawfowl.commandpack.api.data.miscellaneous.CommandSettings.Builder add(Key<? extends Value<V>> key, V value) {
			return this;
		}

		@Override
		public sawfowl.commandpack.api.data.miscellaneous.CommandSettings.Builder from(sawfowl.commandpack.api.data.miscellaneous.CommandSettings holder) {
			settings.aliases = holder.getAliases();
			settings.cooldown = holder.getCooldown();
			settings.delay = holder.getDelay();
			settings.enable = holder.isEnable();
			settings.price = holder.getPrice();
			return this;
		}

		@Override
		public sawfowl.commandpack.api.data.miscellaneous.CommandSettings.Builder reset() {
			settings.aliases = new String[] {};
			settings.cooldown = 0;
			settings.delay = new Delay();
			settings.enable = true;
			settings.price = new CommandPrice();
			return this;
		}

		@Override
		public sawfowl.commandpack.api.data.miscellaneous.@NotNull CommandSettings build() {
			return settings;
		}

		@Override
		public Builder setAliases(List<String> aliases) {
			settings.aliases = Stream.of(aliases).toArray(String[]::new);
			return this;
		}

		@Override
		public Builder setAliases(String[] aliases) {
			settings.aliases = aliases;
			return this;
		}

		@Override
		public Builder setCooldown(long cooldown) {
			settings.cooldown = cooldown;
			return this;
		}

		@Override
		public Builder setDelay(Delay delay) {
			settings.delay = delay;
			return this;
		}

		@Override
		public Builder setEnable(boolean enable) {
			settings.enable = enable;
			return this;
		}

		@Override
		public Builder setPrice(CommandPrice price) {
			settings.price = price;
			return this;
		}
		
	}

	@Override
	public void setRawData(DataView container) throws InvalidDataException {
	}

	@Override
	public org.spongepowered.api.data.SerializableDataHolder.Mutable copy() {
		CommandSettings copy = new CommandSettings();
		copy.aliases = this.aliases;
		copy.cooldown = this.cooldown;
		copy.delay = this.delay;
		copy.enable = this.enable;
		copy.price = this.price;
		return copy;
	}

	@Override
	public boolean validateRawData(DataView container) {
		return false;
	}

	@Override
	public int contentVersion() {
		return 0;
	}

	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew();
	}

	@Override
	public <E> Optional<E> get(Key<? extends Value<E>> key) {
		return Optional.empty();
	}

	@Override
	public <E, V extends Value<E>> Optional<V> getValue(Key<V> key) {
		return Optional.empty();
	}

	@Override
	public boolean supports(Key<?> key) {
		return false;
	}

	@Override
	public Set<Key<?>> getKeys() {
		return new HashSet<>();
	}

	@Override
	public Set<org.spongepowered.api.data.value.Value.Immutable<?>> getValues() {
		return new HashSet<>();
	}

	@Override
	public <E> DataTransactionResult offer(Key<? extends Value<E>> key, E value) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public DataTransactionResult offer(Value<?> value) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public <E> DataTransactionResult offerSingle(Key<? extends CollectionValue<E, ?>> key, E element) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public <K, V> DataTransactionResult offerSingle(Key<? extends MapValue<K, V>> key, K valueKey, V value) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public <K, V> DataTransactionResult offerAll(Key<? extends MapValue<K, V>> key, Map<? extends K, ? extends V> map) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public DataTransactionResult offerAll(MapValue<?, ?> value) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public DataTransactionResult offerAll(CollectionValue<?, ?> value) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public <E> DataTransactionResult offerAll(Key<? extends CollectionValue<E, ?>> key, Collection<? extends E> elements) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public <E> DataTransactionResult removeSingle(Key<? extends CollectionValue<E, ?>> key, E element) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public <K> DataTransactionResult removeKey(Key<? extends MapValue<K, ?>> key, K mapKey) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public DataTransactionResult removeAll(CollectionValue<?, ?> value) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public <E> DataTransactionResult removeAll(Key<? extends CollectionValue<E, ?>> key, Collection<? extends E> elements) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public DataTransactionResult removeAll(MapValue<?, ?> value) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public <K, V> DataTransactionResult removeAll(Key<? extends MapValue<K, V>> key, Map<? extends K, ? extends V> map) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public <E> DataTransactionResult tryOffer(Key<? extends Value<E>> key, E value) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public DataTransactionResult remove(Key<?> key) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public DataTransactionResult undo(DataTransactionResult result) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public DataTransactionResult copyFrom(ValueContainer that, MergeFunction function) {
		return DataTransactionResult.failNoData();
	}

}
