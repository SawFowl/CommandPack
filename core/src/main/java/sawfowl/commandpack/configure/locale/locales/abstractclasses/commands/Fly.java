package sawfowl.commandpack.configure.locale.locales.abstractclasses.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

public interface Fly {

	Component getEnable();

	Component getEnableStaff(ServerPlayer player);

	Component getDisabe();

	Component getDisabeStaff(ServerPlayer player);

}
