package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.UnbanIP;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementUnbanIP implements UnbanIP {

	public ImplementUnbanIP() {}

	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aВы разбанили IP &e" + Placeholders.VALUE + "&a.");

	@Override
	public Component getSuccess(String ip) {
		return Text.of(success).replace(Placeholders.VALUE, ip).get();
	}

}
