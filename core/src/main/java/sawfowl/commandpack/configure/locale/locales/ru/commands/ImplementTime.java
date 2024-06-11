package sawfowl.commandpack.configure.locale.locales.ru.commands;

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
	private Component morning = TextUtils.deserializeLegacy("&aВы установили утро в мире &e" + Placeholders.WORLD + "&a.");
	@Setting("Day")
	private Component day = TextUtils.deserializeLegacy("&aВы установили день в мире &e" + Placeholders.WORLD + "&a.");
	@Setting("Evening")
	private Component evening = TextUtils.deserializeLegacy("&aВы установили вечер в мире &e" + Placeholders.WORLD + "&a.");
	@Setting("Night")
	private Component night = TextUtils.deserializeLegacy("&aВы установили ночь в мире &e" + Placeholders.WORLD + "&a.");
	@Setting("Add")
	private Component add = TextUtils.deserializeLegacy("&aВы изменили время в мире &e" + Placeholders.WORLD + "&a.");

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
