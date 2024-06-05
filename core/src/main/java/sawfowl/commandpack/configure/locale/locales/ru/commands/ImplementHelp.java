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
	private Component title = toComponent("&3Help");
	@Setting("List")
	private List<Component> list = Arrays.asList(
			toComponent("&eThe commands will be available if you have permission for them."),
			toComponent("&2/spawn &f- &eteleport to a spawnpoint"),
			toComponent("&2/home &f- &eteleporting to your home")
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
