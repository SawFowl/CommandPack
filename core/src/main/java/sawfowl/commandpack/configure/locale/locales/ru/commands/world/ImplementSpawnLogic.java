package sawfowl.commandpack.configure.locale.locales.ru.commands.world;

import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.world.SpawnLogic;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementSpawnLogic implements SpawnLogic {

	public ImplementSpawnLogic() {}
	@Setting("Enable")
	private Component enable = TextUtils.deserializeLegacy("&aВключена логика спавна в мире &e\"" + Placeholders.WORLD + "\"&a.");
	@Setting("Disable")
	private Component disable = TextUtils.deserializeLegacy("&aОтключена логика спавна в мире &e\"" + Placeholders.WORLD + "\"&a.");

	@Override
	public Component getEnable(ServerWorld world) {
		return Text.of(enable).replace(Placeholders.WORLD, world.key().asString()).get();
	}

	@Override
	public Component getDisable(ServerWorld world) {
		return Text.of(disable).replace(Placeholders.WORLD, world.key().asString()).get();
	}

}
