package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import net.kyori.adventure.text.Component;

public interface BanInfo {

	Component getPermanent();

	Component getNotPresent();

	Component getSuccess(Component source, String created, Component expire, Component reason);

	Component getTitle(String player);

}
