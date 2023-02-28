package sawfowl.commandpack.configure.configs;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.configs.commands.Suicide;

@ConfigSerializable
public class Commands {

	public Commands() {}

	@Setting("Suicide")
	private Suicide suicide = new Suicide();

	public Suicide getSuicide() {
		return suicide;
	}

}
