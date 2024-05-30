package sawfowl.commandpack.configure.locale.locales.abstractclasses.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import net.kyori.adventure.text.Component;

@ConfigSerializable
public interface SetHome {

	Component getLimit(int limit);

	Component getSuccess(Component home);

}
