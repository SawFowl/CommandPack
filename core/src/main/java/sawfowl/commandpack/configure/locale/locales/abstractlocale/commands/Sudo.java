package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

public interface Sudo {

	Component getCommandNotFound();

	Component getCommandNotAllowed();

	Component getSuccess(ServerPlayer player);

}
