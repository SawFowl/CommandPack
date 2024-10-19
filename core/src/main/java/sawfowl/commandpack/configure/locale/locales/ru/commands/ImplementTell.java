package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Tell;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementTell implements Tell {

	public ImplementTell() {}

	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&5Вы шепчете &e" + Placeholders.PLAYER + "&f: &d" + Placeholders.VALUE);
	@Setting("SuccessTarget")
	private Component successTarget = TextUtils.deserializeLegacy("&e" + Placeholders.PLAYER + " &5шепчет вам&f: &d" + Placeholders.VALUE);

	@Override
	public Component getSuccess(Component target, Component message) {
		return Text.of(success).replace(Placeholders.PLAYER, target).replace(Placeholders.VALUE, message).get();
	}

	@Override
	public Component getSuccessTarget(Component source, Component message) {
		return Text.of(successTarget).replace(Placeholders.PLAYER, source).replace(Placeholders.VALUE, message).get();
	}

}
