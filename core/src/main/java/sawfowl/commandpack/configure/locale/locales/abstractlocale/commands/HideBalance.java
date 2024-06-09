package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import net.kyori.adventure.text.Component;

public interface HideBalance {

	Component getHide();

	Component getOpen();

	Component getHideStaff(Component player);

	Component getOpenStaff(Component player);

	default Component getResult(boolean hide) {
		 return hide ? getHide() : getOpen();
	}

	default Component getResultStaff(boolean hide, Component player) {
		 return hide ? getHideStaff(player) : getOpenStaff(player);
	}

}
