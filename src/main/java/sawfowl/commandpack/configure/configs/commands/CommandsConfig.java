package sawfowl.commandpack.configure.configs.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.reference.ValueReference;
import org.spongepowered.configurate.serialize.SerializationException;

@ConfigSerializable
public class CommandsConfig {

	public CommandsConfig() {}

	private Map<String, CommandSettings> map = new HashMap<>();

	@Setting("Suicide")
	private CommandSettings suicide = new CommandSettings();
	@Setting("Hat")
	private CommandSettings hat = new CommandSettings();
	@Setting("Home")
	private CommandSettings home = new CommandSettings(new Delay(3));
	@Setting("SetHome")
	private CommandSettings setHome = new CommandSettings();
	@Setting("SetSpawn")
	private CommandSettings setSpawn = new CommandSettings();
	@Setting("Spawn")
	private CommandSettings spawn = new CommandSettings(new Delay(3));
	@Setting("SetWarp")
	private CommandSettings setWarp = new CommandSettings();
	@Setting("Warp")
	private CommandSettings warp = new CommandSettings(15, new Delay(3));
	@Setting("Warps")
	private CommandSettings warps = new CommandSettings(new Delay(3));
	@Setting("Tpa")
	private CommandSettings tpa = new CommandSettings(new Delay(3), new CommandPrice("$", 5));
	@Setting("Tpahere")
	private CommandSettings tpahere = new CommandSettings(new Delay(3), new CommandPrice("$", 10));
	@Setting("Tpahereall")
	private CommandSettings tpahereall = new CommandSettings(new Delay(3));
	@Setting("Teleport")
	private CommandSettings teleport = new CommandSettings(new String[] {"tp"});
	@Setting("TeleportHere")
	private CommandSettings teleporthere = new CommandSettings(new String[] {"tphere"});
	@Setting("TeleportHereAll")
	private CommandSettings teleporthereall = new CommandSettings(new String[] {"tphereall"});
	@Setting("Tppos")
	private CommandSettings tppos = new CommandSettings();
	@Setting("Clearinventory")
	private CommandSettings clearinventory = new CommandSettings(new String[] {"clear"});
	@Setting("Repair")
	private CommandSettings repair = new CommandSettings(new CommandPrice("$", 20), new String[] {"fix"});

	public CommandSettings getCommandConfig(String command) {
		return map.getOrDefault(command.toLowerCase(), map.values().stream().filter(config -> (config.getAliasesList().contains(command))).findFirst().orElse(CommandSettings.EMPTY));
	}

	public Optional<CommandSettings> getOptCommandSettings(String command) {
		return Optional.ofNullable(map.getOrDefault(command, null));
	}

	public void updateCommandMap(ValueReference<CommandsConfig, CommentedConfigurationNode> reference) {
		map.clear();
		reference.node().childrenMap().entrySet().forEach(entry -> {
			CommandSettings settings = getCommandFromNode(entry.getValue());
			if(settings.isEnable()) map.put(entry.getKey().toString().toLowerCase(), getCommandFromNode(entry.getValue()));
		});
	}

	private CommandSettings getCommandFromNode(CommentedConfigurationNode node) {
		try {
			return node.get(CommandSettings.class, CommandSettings.EMPTY);
		} catch (SerializationException e) {
			return CommandSettings.EMPTY;
		}
	}

}
