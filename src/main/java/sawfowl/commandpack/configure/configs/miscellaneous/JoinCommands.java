package sawfowl.commandpack.configure.configs.miscellaneous;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class JoinCommands {

	public JoinCommands(){}

	@Setting("Enable")
	private Map<String, Boolean> enable = arraysToMap(new String[] {"FirstJoin", "Regularly"}, new Boolean[] {false, false});
	@Setting("FirstJoin")
	@Comment("These commands are executed only when the player first login to the server.")
	private List<String> firstJoin = Arrays.asList("console:tell %player% Hello", "player:home");
	@Setting("Regularly")
	@Comment("These commands are executed every time a player login to the server, except for the first login.")
	private List<String> regularly = Arrays.asList("console:tell %player% Hello", "player:home");

	public boolean isEnableFirstJoin() {
		return enable.getOrDefault("FirstJoin", false);
	}

	public boolean isEnableRegularly() {
		return enable.getOrDefault("Regularly", false);
	}

	public List<String> getFirstJoin() {
		return firstJoin;
	}

	public List<String> getRegularly() {
		return regularly;
	}

	private Map<String, Boolean> arraysToMap(String[] keys, Boolean[] values) {
		return IntStream.range(0, keys.length).boxed().collect(Collectors.toMap(i -> keys[i], i -> values[i]));
	}

}
