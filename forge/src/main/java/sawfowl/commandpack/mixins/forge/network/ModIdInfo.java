package sawfowl.commandpack.mixins.forge.network;

import sawfowl.commandpack.api.mixin.network.PlayerModInfo;

public abstract class ModIdInfo implements PlayerModInfo {

	private String id;

	PlayerModInfo setId(String string) {
		id = string;
		return this;
	}

	public String getId() {
		return id;
	}

}
