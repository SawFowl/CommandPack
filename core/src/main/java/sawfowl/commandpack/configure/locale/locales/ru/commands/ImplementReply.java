package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Reply;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementReply implements Reply {

	public ImplementReply() {}

	@Setting("Nothing")
	private Component nothing = TextUtils.deserializeLegacy("&cНекому отвечать.");

	@Override
	public Component getNothing() {
		return nothing;
	}

}
