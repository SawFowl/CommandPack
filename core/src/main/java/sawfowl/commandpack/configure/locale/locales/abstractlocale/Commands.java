package sawfowl.commandpack.configure.locale.locales.abstractlocale;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Hat;

@ConfigSerializable
public interface Commands {

	Hat getHat();

}
