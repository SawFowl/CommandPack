package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Economy;
import sawfowl.commandpack.configure.locale.locales.ru.commands.economy.ImplementAdd;
import sawfowl.commandpack.configure.locale.locales.ru.commands.economy.ImplementRemove;
import sawfowl.commandpack.configure.locale.locales.ru.commands.economy.ImplementSet;

@ConfigSerializable
public class ImplementEconomy implements Economy {

	public ImplementEconomy() {}

	@Setting("Add")
	private ImplementAdd add = new ImplementAdd();
	@Setting("Remove")
	private ImplementRemove remove = new ImplementRemove();
	@Setting("Set")
	private ImplementSet set = new ImplementSet();

	@Override
	public Economy.SubCommand getAdd() {
		return add;
	}

	@Override
	public Economy.SubCommand getRemove() {
		return remove;
	}

	@Override
	public Economy.SubCommand getSet() {
		return set;
	}

}
