package sawfowl.commandpack.configure.locales.def.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locales.abstractlocale.commands.Broadcast;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementBroadcast implements Broadcast {

	public ImplementBroadcast() {}

	@Setting("Title")
	private Component title = TextUtils.deserializeLegacy("&9[&2Broadcast&9]&r " + Placeholders.VALUE);

	@Override
	public Component getTitle(Component text) {
		return Text.of(title).replace(Placeholders.VALUE, text).get();
	}

}
