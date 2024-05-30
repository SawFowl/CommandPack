package sawfowl.commandpack.configure.locale.locales.abstractclasses.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import net.kyori.adventure.text.Component;

@ConfigSerializable
public interface Hat {

	Component getNotPresent();

	Component getBlackListItem();

	Component getFullInventory(ServerPlayer player);

	Component getSuccessOther(ServerPlayer player);

	Component getSuccessSelf();

}
