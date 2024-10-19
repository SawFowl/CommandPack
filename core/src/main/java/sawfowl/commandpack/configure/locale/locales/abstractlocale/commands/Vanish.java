package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

public interface Vanish {

	Component getEnable();

	Component getEnableStaff(ServerPlayer player);

	Component getDisable();

	Component getDisableStaff(ServerPlayer player);

}
