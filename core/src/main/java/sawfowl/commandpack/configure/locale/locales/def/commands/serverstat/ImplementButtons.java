package sawfowl.commandpack.configure.locale.locales.def.commands.serverstat;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.serverstat.Buttons;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementButtons implements Buttons {

	public ImplementButtons() {}

	@Setting("System")
	private Component system = TextUtils.deserializeLegacy("&7[&4System&7]");
	@Setting("Worlds")
	private Component worlds = TextUtils.deserializeLegacy("&7[&eWorlds&7]");
	@Setting("Plugins")
	private Component plugins = TextUtils.deserializeLegacy("&7[&aPlugins&7]");
	@Setting("Mods")
	private Component mods = TextUtils.deserializeLegacy("&7[&2Mods&7]");
	@Setting("RefreshPlugin")
	private Component refreshPlugin = TextUtils.deserializeLegacy("&7[&6Refresh&7]");

	@Override
	public Component getSystem() {
		return system;
	}

	@Override
	public Component getWorlds() {
		return worlds;
	}

	@Override
	public Component getPlugins() {
		return plugins;
	}

	@Override
	public Component getMods() {
		return mods;
	}

	@Override
	public Text getRefreshPlugin() {
		return Text.of(refreshPlugin);
	}

}
