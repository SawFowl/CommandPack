package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

public interface Speed {

	Component getSetSelf(int value);

	Component getSetByStaff(ServerPlayer player, int value);

	Component getSetOther(int value);

	Component getInFly();

}
