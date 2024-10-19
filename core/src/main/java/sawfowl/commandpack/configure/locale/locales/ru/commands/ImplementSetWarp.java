package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.SetWarp;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementSetWarp implements SetWarp {

	public ImplementSetWarp(){}

	@Setting("AllreadyExist")
	Component allreadyExist = TextUtils.deserializeLegacy("&cВарп с указанным именем уже существует.");
	@Setting("Limit")
	Component limit = TextUtils.deserializeLegacy("&cВаш лимит&7(&4" + Placeholders.LIMIT + "&7)&c Ваш лимит не позволяет установить новый варп.");
	@Setting("Success")
	Component success = TextUtils.deserializeLegacy("&aВарп установлен.");
	@Setting("SuccessAdmin")
	Component successAdmin = TextUtils.deserializeLegacy("&aАдминистративный варп установлен.");

	@Override
	public Component getAllreadyExist() {
		return allreadyExist;
	}

	@Override
	public Component getLimit(int limit) {
		return Text.of(this.limit).replace(Placeholders.LIMIT, limit).get();
	}

	@Override
	public Component getSuccess() {
		return success;
	}

	@Override
	public Component getSuccessAdmin() {
		return successAdmin;
	}

}
