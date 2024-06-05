package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

public interface Afk {

	Component getEnableBroadcast(ServerPlayer player);

	Component getDisableBroadcast(ServerPlayer player);

	Component getTitle();

	Component getSubtitle(Component kickAfter);

	Component getEnableInVanish();

	Component getDisableInVanish();

	Component getKick();

}
