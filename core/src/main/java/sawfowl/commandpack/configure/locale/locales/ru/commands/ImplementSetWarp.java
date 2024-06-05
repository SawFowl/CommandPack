package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.SetWarp;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementSetWarp implements SetWarp {

	public ImplementSetWarp(){}

	@Setting("AllreadyExist")
	Component allreadyExist = TextUtils.deserializeLegacy("&cA warp with the specified name already exists.");
	@Setting("Limit")
	Component limit = TextUtils.deserializeLegacy("&cYour limit does not allow you to set a new warp point.");
	@Setting("Success")
	Component success = TextUtils.deserializeLegacy("&aWarp is set.");
	@Setting("SuccessAdmin")
	Component successAdmin = TextUtils.deserializeLegacy("&aAdmin warp is set.");

	@Override
	public Component getAllreadyExist() {
		return allreadyExist;
	}

	@Override
	public Component getLimit() {
		return limit;
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
