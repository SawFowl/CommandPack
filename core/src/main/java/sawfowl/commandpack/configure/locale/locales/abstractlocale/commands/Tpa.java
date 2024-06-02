package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

public interface Tpa {

	Component getDisabledRequest();

	Component getOffline();

	Component getSuccess();

	Component getAccepted();

	Component getRequest(ServerPlayer player);

	Component getRequestHere(ServerPlayer player);

}
