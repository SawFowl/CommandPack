package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.SetHome;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementSetHome implements SetHome {

	public ImplementSetHome() {}

	@Setting("Limit")
	private Component limit = TextUtils.deserializeLegacy("&cВаш лимит&7(&4" + Placeholders.LIMIT + "&7)&c не позволяет установить новую домашнюю точку.");
	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aВы установили домашнюю точку &e" + Placeholders.HOME + "&a.");

	@Override
	public Component getLimit(int limit) {
		return Text.of(this.limit).replace(Placeholders.LIMIT, limit).get();
	}

	@Override
	public Component getSuccess(Component home) {
		return Text.of(success).replace(Placeholders.HOME, home).get();
	}

}
