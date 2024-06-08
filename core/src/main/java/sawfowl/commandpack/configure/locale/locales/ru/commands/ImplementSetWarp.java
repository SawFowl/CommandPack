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
	Component allreadyExist = TextUtils.deserializeLegacy("&cA warp with the specified name already exists.");
	@Setting("Limit")
	Component limit = TextUtils.deserializeLegacy("&cYour limit&7(&4" + Placeholders.LIMIT + "&7)&c does not allow you to set a new warp point.");
	@Setting("Success")
	Component success = TextUtils.deserializeLegacy("&aWarp is set.");
	@Setting("SuccessAdmin")
	Component successAdmin = TextUtils.deserializeLegacy("&aAdmin warp is set.");

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
