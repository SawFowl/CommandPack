package sawfowl.commandpack.configure.configs;

import java.util.HashMap;
import java.util.Map;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.reference.ValueReference;
import org.spongepowered.configurate.serialize.SerializationException;

@ConfigSerializable
public class CommandsConfig {

	public CommandsConfig() {}

	private Map<String, Command> map = new HashMap<>();

	@Setting("Suicide")
	private Command suicide = new Command();

	public Command getCommandConfig(String command) {
		return map.getOrDefault(command.toLowerCase(), map.values().stream().filter(config -> (config.getAliasesList().contains(command))).findFirst().orElse(Command.EMPTY));
		/*
		switch (command.toLowerCase()) {
		case "suicide":
			return suicide;
		default:
			return Command.EMPTY;
		}*/
	}

	public void updateCommandMap(ValueReference<CommandsConfig, CommentedConfigurationNode> reference) {
		map.clear();
		reference.node().childrenMap().entrySet().forEach(entry -> {
			map.put(entry.getKey().toString().toLowerCase(), getCommandFromNode(entry.getValue()));
		});
	}

	private Command getCommandFromNode(CommentedConfigurationNode node) {
		try {
			return node.get(Command.class, Command.EMPTY);
		} catch (SerializationException e) {
			return Command.EMPTY;
		}
	}

}
