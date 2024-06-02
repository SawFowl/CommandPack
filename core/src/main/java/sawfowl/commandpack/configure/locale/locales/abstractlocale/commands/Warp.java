package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

public interface Warp {

	Component getNotFound();

	Component getSuccess(Component warp);

	Component getSuccessOther(Component warp);

	Component getSuccessStaff(ServerPlayer player, Component warp);

}
