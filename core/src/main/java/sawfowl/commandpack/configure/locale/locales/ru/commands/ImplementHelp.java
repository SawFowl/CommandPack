package sawfowl.commandpack.configure.locale.locales.ru.commands;

import java.util.Arrays;
import java.util.List;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Help;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementHelp implements Help {

	public ImplementHelp() {}

	@Setting("Title")
	private Component title = toComponent("&3Справка");
	@Setting("List")
	private List<Component> list = Arrays.asList(
			toComponent("&eКоманды будут доступны, если у вас есть на них разрешение."),
			toComponent("&2/spawn &f- &eтелепорт на точку спавна"),
			toComponent("&2/home &f- &eтелепорт домой")
		);

	@Override
	public Component getTitle() {
		return title;
	}

	@Override
	public List<Component> getList() {
		return list;
	}

	private Component toComponent(String string) {
		return TextUtils.deserializeLegacy(string);
	}

}
