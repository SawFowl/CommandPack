package sawfowl.commandpack.configure.locales.def.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.locales.abstractlocale.commands.Disposal;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementDisposal implements Disposal {

	public ImplementDisposal() {}

	@Setting("Title")
	private Component title = TextUtils.deserializeLegacy("&a&lTrash");

	@Override
	public Component getTitle() {
		return title;
	}

}
