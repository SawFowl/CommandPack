package sawfowl.commandpack.configure.configs.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class CommandSettings {

	public static final CommandSettings EMPTY = new CommandSettings();

	public CommandSettings() {}

	public CommandSettings(long cooldown, Delay delay, CommandPrice price) {
		this.cooldown = cooldown;
		this.delay = delay;
		this.price = price;
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
		aliases = newAliases.toArray(new String[newAliases.size()]);
		return this;
	}

	public CommandSettings addAliases(List<String> aliases) {
		List<String> newAliases = getAliasesList();
		newAliases.addAll(aliases);
		this.aliases = newAliases.toArray(new String[newAliases.size()]);
		return this;
	}

	@Override
	public String toString() {
		return "CommandSettings [Aliases=" + aliases + ", Cooldown=" + cooldown + ", Delay=" + delay + ", Enable=" + enable + ", Price=" + price + "]";
	}

}
