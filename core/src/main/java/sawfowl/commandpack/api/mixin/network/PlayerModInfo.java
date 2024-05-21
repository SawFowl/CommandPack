package sawfowl.commandpack.api.mixin.network;

import net.kyori.adventure.text.Component;

public interface PlayerModInfo {

	String getId();

	String getName();

	String getVersion();

	Component asComponent();

}
