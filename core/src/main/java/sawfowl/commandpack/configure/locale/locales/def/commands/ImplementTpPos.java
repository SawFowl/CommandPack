package sawfowl.commandpack.configure.locale.locales.def.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.TpPos;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementTpPos implements TpPos {

	public ImplementTpPos() {}

	@Setting("InvalidLocation")
	private Component invalidLocation = TextUtils.deserializeLegacy("&cAn unacceptable location is specified.");

	@Override
	public Component getInvalidLocation() {
		return invalidLocation;
	}

}
