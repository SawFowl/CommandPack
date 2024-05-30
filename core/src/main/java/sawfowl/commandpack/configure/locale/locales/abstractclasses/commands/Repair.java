package sawfowl.commandpack.configure.locale.locales.abstractclasses.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import net.kyori.adventure.text.Component;

@ConfigSerializable
public interface Repair {

	Component getSuccess();

	Component getSuccessOther();

	Component getSuccessStaff(ServerPlayer player);

}
