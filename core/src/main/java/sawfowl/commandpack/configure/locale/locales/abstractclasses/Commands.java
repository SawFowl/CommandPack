package sawfowl.commandpack.configure.locale.locales.abstractclasses;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import sawfowl.commandpack.configure.locale.locales.abstractclasses.commands.Hat;

@ConfigSerializable
public interface Commands {

	Hat getHat();

}
