package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.TpToggle;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementTpToggle implements TpToggle {

	public ImplementTpToggle() {}

	@Setting("Enable")
	private Component enable = TextUtils.deserializeLegacy("&aТеперь вы будете получать запросы на телепортацию.");
	@Setting("Disable")
	private Component disable = TextUtils.deserializeLegacy("&aВходящие запросы на телепортацию теперь будут автоматически отклоняться.");

	@Override
	public Component getEnable() {
		return enable;
	}

	@Override
	public Component getDisable() {
		return disable;
	}

}
