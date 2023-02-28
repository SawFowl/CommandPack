package sawfowl.commandpack.configure.configs;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class MainConfig {

	public MainConfig() {}

	@Setting("JsonLocales")
	private boolean jsonLocales = false;
	@Setting("DebugEconomy")
	private boolean debugEconomy = true;

	public boolean isJsonLocales() {
		return jsonLocales;
	}

	public boolean isDebugEconomy() {
		return debugEconomy;
	}

}
