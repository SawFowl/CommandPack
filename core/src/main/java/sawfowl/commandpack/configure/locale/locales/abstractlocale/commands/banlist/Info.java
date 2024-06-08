package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.banlist;

import net.kyori.adventure.text.Component;

public interface Info {

	Component getPlayer(String player, Component source, String created, Component expire, Component reason);

	Component getIP(String ip, Component source, String created, Component expire, Component reason);

}
