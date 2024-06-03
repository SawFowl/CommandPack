package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

public interface Ping {

	Component getSuccess(long ping);

	Component getSuccessStaff(ServerPlayer player, long ping);

}
