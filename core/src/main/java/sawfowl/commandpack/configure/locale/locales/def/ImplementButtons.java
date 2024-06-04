package sawfowl.commandpack.configure.locale.locales.def;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.Buttons;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementButtons implements Buttons {

	public ImplementButtons() {}

	@Setting("Remove")
	private Component remove = TextUtils.deserializeLegacy("&7[&cRemove&7] ");
	@Setting("Teleport")
	private Component teleport = TextUtils.deserializeLegacy("&7[&aTeleport&7] ");
	@Setting("TeleportClickable")
	private Component teleportClickable = TextUtils.deserializeLegacy("&7[&eTeleport&7] ");

	@Override
	public Component getRemove() {
		return remove;
	}

	@Override
	public Component getTeleport() {
		return teleport;
	}

	@Override
	public Component getTeleportClickable() {
		return teleportClickable;
	}

}
