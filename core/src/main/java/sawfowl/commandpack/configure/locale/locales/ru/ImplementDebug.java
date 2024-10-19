package sawfowl.commandpack.configure.locale.locales.ru;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.Debug;
import sawfowl.commandpack.configure.locale.locales.ru.debug.ImplementEconomy;
import sawfowl.commandpack.configure.locale.locales.ru.debug.ImplementCommands;
import sawfowl.commandpack.configure.locale.locales.ru.debug.ImplementDebugPlayerData;

@ConfigSerializable
public class ImplementDebug implements Debug {

	public ImplementDebug() {}

	@Setting("Economy")
	private ImplementEconomy economy = new ImplementEconomy();
	@Setting("Commands")
	private ImplementCommands commands = new ImplementCommands();
	@Setting("DebugPlayerData")
	private ImplementDebugPlayerData debugPlayerData = new ImplementDebugPlayerData();

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

}
