package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

public interface Kit {

	Component getTitle();

	Component getView();

	Component getEmpty();

	Component getPermissionRequired();

	Component getWait(Component time);

	Component getInventoryFull();

	Component getGiveLimit();

	Component getNotEnoughMoney(Component price);

	Component getSuccess(Component kitName);

	Component getSuccessStaff(ServerPlayer player, Component kitName);

}
