package sawfowl.commandpack.configure.locale.locales.def;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractclasses.Commands;
import sawfowl.commandpack.configure.locale.locales.abstractclasses.commands.Hat;
import sawfowl.commandpack.configure.locale.locales.abstractclasses.commands.Spawn;
import sawfowl.commandpack.configure.locale.locales.def.commands.ImplementHat;
import sawfowl.commandpack.configure.locale.locales.def.commands.ImplementSpawn;

@ConfigSerializable
public class ImplementCommands implements Commands {

	public ImplementCommands() {}

	@Setting("Hat")
	private ImplementHat hat = new ImplementHat();
	private ImplementSpawn spawn = new ImplementSpawn();

	@Override
	public Hat getHat() {
		return hat;
	}

	public Spawn getSpawn() {
		return spawn;
	}

}
