package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Jump;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementJump implements Jump {

	public ImplementJump() {}

	@Setting("Exception")
	private Component exception = TextUtils.deserializeLegacy("&cНе удалось определить позицию для прыжка.");

	@Override
	public Component getException() {
		return exception;
	}

}
