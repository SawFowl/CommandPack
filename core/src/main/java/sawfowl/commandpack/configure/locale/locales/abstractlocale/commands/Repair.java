package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

public interface Repair {

	Component getSuccess();

	Component getSuccessOther();

	Component getSuccessStaff(ServerPlayer player);

}
