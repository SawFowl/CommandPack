package sawfowl.commandpack.configure.locale.locales.abstractclasses.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import net.kyori.adventure.text.Component;

@ConfigSerializable
public interface TpToggle {

	Component getEnable();

	Component getDisable();

}
