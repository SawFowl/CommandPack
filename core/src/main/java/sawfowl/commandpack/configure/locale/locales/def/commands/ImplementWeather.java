package sawfowl.commandpack.configure.locale.locales.def.commands;

import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Weather;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementWeather implements Weather {

	public ImplementWeather() {}

	@Setting("Sun")
	private Component sun = TextUtils.deserializeLegacy("&aYou have set a clear weather in the world  &e" + Placeholders.WORLD + "&a.");
	@Setting("Rain")
	private Component rain = TextUtils.deserializeLegacy("&aYou have set the rainy weather in the world  &e" + Placeholders.WORLD + "&a.");
	@Setting("Thunder")
	private Component thunder = TextUtils.deserializeLegacy("&aYou have set a thunder weather in the world &e" + Placeholders.WORLD + "&a.");

	@Override
	public Component getSun(ServerWorld world) {
		return Text.of(sun).replace(Placeholders.WORLD, world.key().asString()).get();
	}

	@Override
	public Component getRain(ServerWorld world) {
		return Text.of(rain).replace(Placeholders.WORLD, world.key().asString()).get();
	}

	@Override
	public Component getThunder(ServerWorld world) {
		return Text.of(thunder).replace(Placeholders.WORLD, world.key().asString()).get();
	}

}
