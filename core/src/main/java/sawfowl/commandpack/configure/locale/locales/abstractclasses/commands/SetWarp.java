package sawfowl.commandpack.configure.locale.locales.abstractclasses.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import net.kyori.adventure.text.Component;

@ConfigSerializable
public interface SetWarp {

	Component getAllreadyExist();

	Component getLimit();

	Component getSuccess();

	Component getSuccessAdmin();

}
