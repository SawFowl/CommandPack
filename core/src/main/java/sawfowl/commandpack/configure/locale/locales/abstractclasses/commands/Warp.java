package sawfowl.commandpack.configure.locale.locales.abstractclasses.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import net.kyori.adventure.text.Component;

@ConfigSerializable
public interface Warp {

	Component getNotFound();

	Component getSuccess(Component warp);

	Component getSuccessOther(Component warp);

	Component getSuccessStaff(ServerPlayer player, Component warp);

}
