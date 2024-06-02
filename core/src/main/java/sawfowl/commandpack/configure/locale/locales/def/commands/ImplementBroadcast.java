package sawfowl.commandpack.configure.locale.locales.def.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Broadcast;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementBroadcast implements Broadcast {

	public ImplementBroadcast() {}

	@Setting("Title")
	private Component title = TextUtils.deserializeLegacy("&9[&2Broadcast&9]&r ");

	@Override
	public Component getTitle() {
		return title;
	}

}
