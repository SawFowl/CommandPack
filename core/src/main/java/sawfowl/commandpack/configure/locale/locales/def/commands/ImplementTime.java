package sawfowl.commandpack.configure.locale.locales.def.commands;

import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Time;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementTime implements Time {

	public ImplementTime() {}

	@Setting("Morning")
	private Component morning = TextUtils.deserializeLegacy("&aYou have set the morning in the world &e" + Placeholders.WORLD + "&a.");
	@Setting("Day")
	private Component day = TextUtils.deserializeLegacy("&aYou have set the day in the world &e" + Placeholders.WORLD + "&a.");
	@Setting("Evening")
	private Component evening = TextUtils.deserializeLegacy("&aYou have set an evening in the world &e" + Placeholders.WORLD + "&a.");
	@Setting("Night")
	private Component night = TextUtils.deserializeLegacy("&aYou have set the night in the world &e" + Placeholders.WORLD + "&a.");
	@Setting("Add")
	private Component add = TextUtils.deserializeLegacy("&aYou have changed the time in the world &e" + Placeholders.WORLD + "&a.");

	@Override
	public Component getMorning(ServerWorld world) {
		return Text.of(morning).replace(Placeholders.WORLD, world.key().asString()).get();
	}

	@Override
	public Component getDay(ServerWorld world) {
		return Text.of(day).replace(Placeholders.WORLD, world.key().asString()).get();
	}

	@Override
	public Component getEvening(ServerWorld world) {
		return Text.of(evening).replace(Placeholders.WORLD, world.key().asString()).get();
	}

	@Override
	public Component getNight(ServerWorld world) {
		return Text.of(night).replace(Placeholders.WORLD, world.key().asString()).get();
	}

	@Override
	public Component getAdd(ServerWorld world) {
		return Text.of(add).replace(Placeholders.WORLD, world.key().asString()).get();
	}

}
