package sawfowl.commandpack.configure.configs.commands;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.Command.Raw;
import org.spongepowered.api.command.manager.CommandFailedRegistrationException;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;

@ConfigSerializable
public class CommandSettings {

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

	public String[] getAliases() {
		return aliases;
	}

	public List<String> getAliasesList() {
		return aliases.length == 0 ? new ArrayList<>() : Stream.of(aliases).collect(Collectors.toList());
	}

	public long getCooldown() {
		return cooldown;
	}

	public Delay getDelay() {
		return delay;
	}

	public CommandPrice getPrice() {
		return price;
	}

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

	public void registerParameterized(Class<? extends AbstractParameterizedCommand> clazz, RegisterCommandEvent<Parameterized> event, CommandPack plugin) {
		try {
			clazz.getDeclaredConstructor().newInstance(plugin).register(event);
		} catch (CommandFailedRegistrationException | InstantiationException | IllegalAccessException| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

	public void registerRaw(Class<? extends AbstractRawCommand> clazz, RegisterCommandEvent<Raw> event, CommandPack plugin) {
		try {
			clazz.getDeclaredConstructor().newInstance(plugin).register(event);
		} catch (CommandFailedRegistrationException | InstantiationException | IllegalAccessException| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

}
