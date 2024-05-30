package sawfowl.commandpack.configure.locale.locales.def;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.AbstractLocale;

@ConfigSerializable
public class ImplementPluginLocale extends AbstractLocale {

	public ImplementPluginLocale(){}

	@Setting("Commands")
	private ImplementCommands commands = new ImplementCommands();

	@Override
	public ImplementCommands getCommands() {
		return commands;
	}

}
