package sawfowl.commandpack.configure.locales.def;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locales.abstractlocale.Debug;
import sawfowl.commandpack.configure.locales.def.debug.ImplementCommands;
import sawfowl.commandpack.configure.locales.def.debug.ImplementDebugPlayerData;
import sawfowl.commandpack.configure.locales.def.debug.ImplementEconomy;

@ConfigSerializable
public class ImplementDebug implements Debug {

	public ImplementDebug() {}

	@Setting("Economy")
	private ImplementEconomy economy = new ImplementEconomy();
	@Setting("Commands")
	private ImplementCommands commands = new ImplementCommands();
	@Setting("DebugPlayerData")
	private ImplementDebugPlayerData debugPlayerData = new ImplementDebugPlayerData();
	@Setting("FinishedRegisterNetworkData")
	private String finishedRegisterNetworkData = "Registration of the '" + Placeholders.VALUE + "' data channel and codec is performed too late. Registration should be performed immediately after gaining access to the CommandPack API.";

	@Override
	public Economy getEconomy() {
		return economy;
	}

	@Override
	public Commands getCommands() {
		return commands;
	}

	@Override
	public DebugPlayerData getDebugPlayerData() {
		return debugPlayerData;
	}

	@Override
	public String getFinishedRegisterNetworkData(ResourceKey channel) {
		return finishedRegisterNetworkData.replace(Placeholders.VALUE, channel.asString());
	}

}
