package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Home;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementHome implements Home {

	public ImplementHome() {}

	@Setting("NotFound")
	private Component notFound = TextUtils.deserializeLegacy("&cТочка дома &e" + Placeholders.HOME + "&c не найдена.");
	@Setting("NotSet")
	private Component notSet = TextUtils.deserializeLegacy("&cТочка дома не установлена. Используйте команду &e/sethome&c для ее установки.");
	@Setting("Error")
	private Component error = TextUtils.deserializeLegacy("&cДомашняя точка недоступна. Возможно, мир не загружен.");
	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aВы телепортировались в домашнюю точку &e" + Placeholders.HOME + "&a.");
	@Setting("List")
	private Component list = TextUtils.deserializeLegacy("&bДома");

	@Override
	public Component getNotFound(String home) {
		return Text.of(notFound).replace(Placeholders.HOME, home).get();
	}

	@Override
	public Component getNotSet() {
		return notSet;
	}

	@Override
	public Component getError() {
		return error;
	}

	@Override
	public Component getSuccess(Component home) {
		return Text.of(success).replace(Placeholders.HOME, home).get();
	}

	@Override
	public Component getListTitle() {
		return list;
	}

}
