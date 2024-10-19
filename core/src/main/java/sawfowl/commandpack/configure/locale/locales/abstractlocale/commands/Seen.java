package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import net.kyori.adventure.text.Component;

import sawfowl.localeapi.api.Text;

public interface Seen {

	Text getTitle();

	Text getOnline();

	Text getOffline();

	Text getOnlineTime();

	Text getLastOnline();

	Text getDisplayName();

	Text getUUID();

	Text getIP();

	Text getFirstPlayed();

	Text getWalkingSpeed();

	Text getFlyingSpeed();

	Text getCurrentLocation();

	Text getCanFly();

	Text getIsFlying();

	Text getGameMode();

	Text getVanished();

	Text getInvulnerable();

	Text getAFK();

	Text getBan();

	Text getMute();

	Text getWarns();

	Component getYes();

	Component getNo();

	Component getPadding();

}
