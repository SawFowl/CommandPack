package sawfowl.commandpack.configure.locale.locales.def.debug;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Debug.Economy;

@ConfigSerializable
public class ImplementEconomy implements Economy {

	public ImplementEconomy() {}

	@Setting("ErrorGiveMoney")
	private String errorGiveMoney = "Failed to add game currency to player " + Placeholders.PLAYER + ".";
	@Setting("ErrorTakeMoney")
	private String errorTakeMoney = "Failed to take game currency from player " + Placeholders.PLAYER + ".";
	@Setting("NotFound")
	private String notFound = "There is no economy plugin on the server. Some of the functionality will not be available. You can enable the economy plugin functionality in the main configuration of the CommandPack plugin.";

	@Override
	public String getErrorGiveMoney(String player) {
		return errorGiveMoney.replace(Placeholders.PLAYER, player);
	}

	@Override
	public String getErrorTakeMoney(String player) {
		return errorTakeMoney.replace(Placeholders.PLAYER, player);
	}

	@Override
	public String getNotFound() {
		return notFound;
	}

}
