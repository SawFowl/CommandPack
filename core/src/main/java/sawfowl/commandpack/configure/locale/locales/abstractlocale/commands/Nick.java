package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

public interface Nick {

	Component getSet(Component value);

	Component getClear();

	Component getSetStaff(ServerPlayer player, Component value);

	Component getClearStaff(ServerPlayer player);

}
