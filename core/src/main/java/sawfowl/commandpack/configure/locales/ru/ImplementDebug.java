package sawfowl.commandpack.configure.locales.ru;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locales.abstractlocale.Debug;
import sawfowl.commandpack.configure.locales.ru.debug.ImplementCommands;
import sawfowl.commandpack.configure.locales.ru.debug.ImplementDebugPlayerData;
import sawfowl.commandpack.configure.locales.ru.debug.ImplementEconomy;

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
	private String finishedRegisterNetworkData = "Слишком поздно выполняется регистрация кодека и канала данных '" + Placeholders.VALUE + "'. Регистрацию стоит выполнять сразу после получения доступа к API CommandPack.";

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
