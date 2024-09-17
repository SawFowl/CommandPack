package sawfowl.commandpack.configure.configs.miscellaneous;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class RestrictEntityWorldSpawn {

	public RestrictEntityWorldSpawn(){}

	@Setting("BlackList")
	private boolean blackList = true;
	@Setting("Entities")
	private List<String> entities = new ArrayList<>();

	public boolean isAllowSpawn(String entity) {
		return blackList ? !contains(entity) : contains(entity);
	}

	public boolean contains(String entity) {
		return entities.contains(entity);
	}

}
