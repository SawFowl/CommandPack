package sawfowl.commandpack.configure.locale.locales.abstractclasses.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

public interface Speed {

	Component getSetSelf(double value);

	Component getSetByStaff(ServerPlayer player, double value);

	Component getSetOther(double value);

	Component getInFly();

	Component getSetDefault();

}
