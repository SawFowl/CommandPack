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
	private Component peaceful = TextUtils.deserializeLegacy("&aУстановлена мирная сложность в мире &e\"" + Placeholders.WORLD + "\"&a.");
	@Setting("Low")
	private Component low = TextUtils.deserializeLegacy("&aУстановлена легкая сложность в мире &e\"" + Placeholders.WORLD + "\"&a.");
	@Setting("Normal")
	private Component normal = TextUtils.deserializeLegacy("&aУстановлена нормальная сложность в мире &e\"" + Placeholders.WORLD + "\"&a.");
	@Setting("Hard")
	private Component hard = TextUtils.deserializeLegacy("&aУстановлена высокая сложность в мире &e\"" + Placeholders.WORLD + "\"&a.");

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
