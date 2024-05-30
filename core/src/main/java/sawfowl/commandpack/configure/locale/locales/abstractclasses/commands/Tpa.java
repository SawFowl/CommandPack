package sawfowl.commandpack.configure.locale.locales.abstractclasses.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import net.kyori.adventure.text.Component;

@ConfigSerializable
public interface Tpa {

	Component getDisabledRequest();

	Component getOffline();

	Component getSuccess();

	Component getAccepted();

	Component getRequest(ServerPlayer player);

	Component getRequestHere(ServerPlayer player);

}
