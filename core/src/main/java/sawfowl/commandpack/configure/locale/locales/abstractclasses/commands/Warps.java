package sawfowl.commandpack.configure.locale.locales.abstractclasses.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import net.kyori.adventure.text.Component;

@ConfigSerializable
public interface Warps {

	Component getEmpty();

	Component getList();

	Component getWait();

	Component getHeader(Component server, Component player);

	Component getServer();

	Component getPlayer();

}
