package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Kits;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementKits implements Kits {

	public ImplementKits() {}

	@Setting("Title")
	Component title = TextUtils.deserializeLegacy("&3Наборы");
	@Setting("AlreadyExist")
	Component alreadyExist = TextUtils.deserializeLegacy("&cНабор с таким идентификатором уже существует.");
	@Setting("Saved")
	Component saved = TextUtils.deserializeLegacy("&aНабор &e" + Placeholders.VALUE + "&a сохранен.");
	@Setting("Empty")
	Component empty = TextUtils.deserializeLegacy("&cНе создано ни одного набора.");
	@Setting("CooldownIncorectTime")
	Component cooldownIncorectTime = TextUtils.deserializeLegacy("&cВремя указано неверно. Укажите продолжительность в формате времени ISO.");
	@Setting("CooldownSuccess")
	Component cooldownSuccess = TextUtils.deserializeLegacy("&aУстановленно время востановления доступа к набору &e" + Placeholders.VALUE + "&a.");
	@Setting("SetName")
	Component setName = TextUtils.deserializeLegacy("&aУстановлено локализованное название набора.");
	@Setting("GiveRule")
	Component giveRule = TextUtils.deserializeLegacy("&aУстановлено правило выдачи набора.");
	@Setting("CreateLore")
	Component createLore = TextUtils.deserializeLegacy("&aВ комплект добавлено описание для локализации по умолчанию. Чтобы изменить его, отредактируйте файл конфигурации набора.");
	@Setting("GiveLimit")
	Component giveLimit = TextUtils.deserializeLegacy("&aУстановлен лимит на получение игроком набора.");
	@Setting("EnableFirstTime")
	Component enableFirstTime = TextUtils.deserializeLegacy("&aТеперь комплект будет автоматически выдаваться игроку один раз.");
	@Setting("DisableFirstTime")
	Component disableFirstTime = TextUtils.deserializeLegacy("&aНабор больше не будет автоматически выдаваться игроку.");
	@Setting("EnableGiveOnJoin")
	Component enableGiveOnJoin = TextUtils.deserializeLegacy("&aТеперь сервер будет пытаться автоматически выдать игроку набор при его подключении.");
	@Setting("DisableGiveOnJoin")
	Component disableGiveOnJoin = TextUtils.deserializeLegacy("&aНабор больше не будет автоматически выдаваться игроку.");
	@Setting("EnablePerm")
	Component enablePerm = TextUtils.deserializeLegacy("&aТеперь для получения набора требуется разрешение.");
	@Setting("DisablePerm")
	Component disablePerm = TextUtils.deserializeLegacy("&aДля получения набора больше не требуется разрешение.");
	@Setting("AddCommand")
	Component addCommand = TextUtils.deserializeLegacy("&aВ набор была добавлена команда.");
	@Setting("CommandRemoveFail")
	Component commandRemoveFail = TextUtils.deserializeLegacy("&cВ наборе нет этой команды.");
	@Setting("CommandRemoveSuccess")
	Component commandRemoveSuccess = TextUtils.deserializeLegacy("&aКоманда удалена.");
	@Setting("CommandsEmpty")
	Component commandsEmpty = TextUtils.deserializeLegacy("&aНабор не содержит никаких команд.");
	@Setting("CommandsTitle")
	Component commandsTitle = TextUtils.deserializeLegacy("&3&lКоманды");
	@Setting("SetPrice")
	Component setPrice = TextUtils.deserializeLegacy("&aУстановлена новая цена для получения набора.");

	@Override
	public Component getTitle() {
		return title;
	}

	@Override
	public Component getAlreadyExist() {
		return alreadyExist;
	}

	@Override
	public Component getSaved(Component name) {
		return Text.of(saved).replace(Placeholders.VALUE, name).get();
	}

	@Override
	public Component getEmpty() {
		return empty;
	}

	@Override
	public Component getCooldownIncorectTime() {
		return cooldownIncorectTime;
	}

	@Override
	public Component getCooldownSuccess(Component value) {
		return Text.of(cooldownSuccess).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getSetName() {
		return setName;
	}

	@Override
	public Component getGiveRule() {
		return giveRule;
	}

	@Override
	public Component getCreateLore() {
		return createLore;
	}

	@Override
	public Component getGiveLimit() {
		return giveLimit;
	}

	@Override
	public Component getEnableFirstTime() {
		return enableFirstTime;
	}

	@Override
	public Component getDisableFirstTime() {
		return disableFirstTime;
	}

	@Override
	public Component getEnableGiveOnJoin() {
		return enableGiveOnJoin;
	}

	@Override
	public Component getDisableGiveOnJoin() {
		return disableGiveOnJoin;
	}

	@Override
	public Component getEnablePerm() {
		return enablePerm;
	}

	@Override
	public Component getDisablePerm() {
		return disablePerm;
	}

	@Override
	public Component getAddCommand() {
		return addCommand;
	}

	@Override
	public Component getCommandRemoveFail() {
		return commandRemoveFail;
	}

	@Override
	public Component getCommandRemoveSuccess() {
		return commandRemoveSuccess;
	}

	@Override
	public Component getCommandsEmpty() {
		return commandsEmpty;
	}

	@Override
	public Component getCommandsTitle() {
		return commandsTitle;
	}

	@Override
	public Component getSetPrice() {
		return setPrice;
	}

}
