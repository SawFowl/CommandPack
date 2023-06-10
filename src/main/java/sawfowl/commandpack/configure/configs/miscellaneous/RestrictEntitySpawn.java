package sawfowl.commandpack.configure.configs.miscellaneous;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class RestrictEntitySpawn {

	public RestrictEntitySpawn(){}

	@Setting("Enable")
	private boolean enable = false;
	@Setting("BlackList")
	private boolean blackList = true;
	@Setting("Entities")
	private List<String> entities = new ArrayList<>();
	@Setting("Worlds")
	private Map<String, RestrictEntityWorldSpawn> worlds = arraysToMap(new String[] {"minecraft:overworld", "minecraft:the_nether", "minecraft:the_end"}, new RestrictEntityWorldSpawn[] {new RestrictEntityWorldSpawn(), new RestrictEntityWorldSpawn(), new RestrictEntityWorldSpawn()});;

	public boolean isAllowSpawn(String entity, String string) {
		return !enable || (worlds.containsKey(string) && worlds.get(string).contains(entity) ? worlds.get(string).isAllowSpawn(entity) : (blackList ? !entities.contains(entity) : entities.contains(entity)));
	}

	private Map<String, RestrictEntityWorldSpawn> arraysToMap(String[] keys, RestrictEntityWorldSpawn[] values) {
		return IntStream.range(0, keys.length).boxed().collect(Collectors.toMap(i -> keys[i], i -> values[i]));
	}

}
