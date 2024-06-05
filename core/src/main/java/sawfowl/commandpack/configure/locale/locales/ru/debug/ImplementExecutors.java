package sawfowl.commandpack.configure.locale.locales.ru.debug;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Debug;

@ConfigSerializable
public class ImplementExecutors implements Debug.Commands.Executors {

	public ImplementExecutors() {}

	@Setting("Server")
	private String server = "Server";
	@Setting("CommandBlock")
	private String commandBlock = "CommandBlock" + Placeholders.LOCATION;
	@Setting("CommandBlockMinecart")
	private String commandBlockMinecart = "Minecart with command block" + Placeholders.LOCATION;
	@Setting("Unknown")
	private String unknown = "Unknown";

	@Override
	public String getServer() {
		return server;
	}

	@Override
	public String getCommandBlock(String cords) {
		return commandBlock.replace(Placeholders.LOCATION, cords);
	}

	@Override
	public String getCommandBlockMinecart(String cords) {
		return commandBlockMinecart.replace(Placeholders.LOCATION, cords);
	}

	@Override
	public String getUnknown() {
		return unknown;
	}

}
