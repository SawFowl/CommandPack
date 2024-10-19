package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Back;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementBack implements Back {

	public ImplementBack() {}

	@Setting("NotFound")
	private Component notFound = TextUtils.deserializeLegacy("&cПредыдущая локация не найдена.");
	@Setting("NotLoadedWorld")
	private Component notLoadedWorld = TextUtils.deserializeLegacy("&cМир с предыдущей локацией не загружен.");

	@Override
	public Component getNotFound() {
		return notFound;
	}

	@Override
	public Component getNotLoadedWorld() {
		return notLoadedWorld;
	}

}
