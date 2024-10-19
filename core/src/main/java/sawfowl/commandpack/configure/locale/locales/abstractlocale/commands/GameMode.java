package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

public interface GameMode {

	Component getSuccess(Component type);

	Component getSuccessStaff(ServerPlayer player, Component type);

	Component getCreative();

	Component getSpectator();

	Component getSurvival();

	Component getAdventure();

}
