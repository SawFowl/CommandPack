package sawfowl.commandpack.configure.locale.locales.abstractclasses.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import net.kyori.adventure.text.Component;

@ConfigSerializable
public interface Home {

	Component getNotFound(Component home);

	Component getNotSet();

	Component getError();

	Component getSuccess(Component home);

	Component getList();

}
