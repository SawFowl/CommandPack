package sawfowl.commandpack.configure.locale.locales.ru;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Debug;
import sawfowl.commandpack.configure.locale.locales.ru.debug.ImplementEconomy;
import sawfowl.commandpack.configure.locale.locales.ru.debug.ImplementCommands;

@ConfigSerializable
public class ImplementDebug implements Debug {

	public ImplementDebug() {}

	@Setting("Economy")
	private ImplementEconomy economy = new ImplementEconomy();
	@Setting("Commands")
	private ImplementCommands commands = new ImplementCommands();
	@Setting("ModsList")
	@Comment("Эта функция работает только при наличии Forge на стороне сервера.")
	private String modsList = "Игрок " + Placeholders.PLAYER + " входит на сервер с модами: " + Placeholders.VALUE + ".";

	@Override
	public Economy getEconomy() {
		return economy;
	}

	@Override
	public Commands getCommands() {
		return commands;
	}

	@Override
	public String getModsList(String player, String mods) {
		return modsList.replace(Placeholders.PLAYER, player).replace(Placeholders.VALUE, mods);
	}

}
