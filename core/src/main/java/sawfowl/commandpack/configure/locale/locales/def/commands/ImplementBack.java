package sawfowl.commandpack.configure.locale.locales.def.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Back;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementBack implements Back {

	public ImplementBack() {}

	@Setting("NotFound")
	private Component notFound = TextUtils.deserializeLegacy("&cThe previous location was not found.");
	@Setting("NotLoadedWorld")
	private Component notLoadedWorld = TextUtils.deserializeLegacy("&cThe world in which the previous location is located is not loaded.");

	@Override
	public Component getNotFound() {
		return null;
	}

	@Override
	public Component getNotLoadedWorld() {
		return null;
	}

}
