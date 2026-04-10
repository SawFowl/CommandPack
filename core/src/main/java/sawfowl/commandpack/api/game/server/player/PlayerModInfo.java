package sawfowl.commandpack.api.game.server.player;

import net.kyori.adventure.text.Component;

public interface PlayerModInfo {

	String getId();

	String getName();

	String getVersion();

	String getFullInfo();

	Component asComponent();

}
