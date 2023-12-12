package sawfowl.commandpack.configure.configs.miscellaneous;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class PreventDamage {

	public PreventDamage(){}

	@Setting("Vanish")
	private final boolean vanish = false;
	@Setting("GodMode")
	private final boolean godMode = false;

	public boolean isVanish() {
		return vanish;
	}

	public boolean isGodMode() {
		return godMode;
	}

}
