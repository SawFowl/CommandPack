package sawfowl.commandpack.configure.locale.locales.def.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.locale.locales.abstractclasses.commands.Jump;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementJump implements Jump {

	public ImplementJump() {}

	@Setting("Exception")
	private Component exception = TextUtils.deserializeLegacy("&cFailed to determine the position for the jump.");

	@Override
	public Component getException() {
		return exception;
	}

}
