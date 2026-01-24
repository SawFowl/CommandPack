package sawfowl.commandpack.configure.locales.abstractlocale.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

public interface ClearInventory {

	Component getSuccess();

	Component getSuccessOther();

	Component getSuccessStaff(ServerPlayer player);

}
