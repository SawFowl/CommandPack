package sawfowl.commandpack.configure.locale.locales.ru.commands.world;

import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.world.Difficulty;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementDifficulty implements Difficulty {

	public ImplementDifficulty() {}

	@Setting("Peaceful")
	private Component peaceful = TextUtils.deserializeLegacy("&aA peaceful difficulty in the world of&e\"" + Placeholders.WORLD + "\"&a has been set.");
	@Setting("Low")
	private Component low = TextUtils.deserializeLegacy("&aThe low difficulty in the world &e\"" + Placeholders.WORLD + "\"&a has been set.");
	@Setting("Normal")
	private Component normal = TextUtils.deserializeLegacy("&aThe normal difficulty in the world &e\"" + Placeholders.WORLD + "\"&a has been set.");
	@Setting("Hard")
	private Component hard = TextUtils.deserializeLegacy("&aThe hard difficulty in the world &e\"" + Placeholders.WORLD + "\"&a has been set.");

	@Override
	public Component getPeaceful(ServerWorld world) {
		return Text.of(peaceful).replace(Placeholders.WORLD, world.key().asString()).get();
	}

	@Override
	public Component getLow(ServerWorld world) {
		return Text.of(low).replace(Placeholders.WORLD, world.key().asString()).get();
	}

	@Override
	public Component getNormal(ServerWorld world) {
		return Text.of(normal).replace(Placeholders.WORLD, world.key().asString()).get();
	}

	@Override
	public Component getHard(ServerWorld world) {
		return Text.of(hard).replace(Placeholders.WORLD, world.key().asString()).get();
	}

}
