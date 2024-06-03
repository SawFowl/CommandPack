package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

public interface CommandSpy {

	Component getEnable();

	Component getDisable();

	Component getSpy(ServerPlayer player, String command);

}
