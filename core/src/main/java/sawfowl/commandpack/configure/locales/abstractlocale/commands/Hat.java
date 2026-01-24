package sawfowl.commandpack.configure.locales.abstractlocale.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

public interface Hat {

	Component getNotPresent();

	Component getBlackListItem();

	Component getFullInventory(ServerPlayer player);

	Component getSuccessStaff(ServerPlayer player);

	Component getSuccessSelf();

}
