package sawfowl.commandpack.configure.locale.locales.ru.commands.world;

import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.math.vector.Vector3i;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.world.Generate;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementGenerate implements Generate {

	public ImplementGenerate() {}

	@Setting("Debug")
	private String debug = "Выполняется генерация чанков в мире \"" + Placeholders.WORLD + "\". Завершено: " + Placeholders.VALUE + "%. Последний сгенерированный чанк: " + Placeholders.LOCATION + ".";
	@Setting("NotStarted")
	private Component notStarted = TextUtils.deserializeLegacy("&cНе найдена задача генерации чанков в мире &e\"" + Placeholders.WORLD + "\"&c.");
	@Setting("NotPaused")
	private Component notPaused = TextUtils.deserializeLegacy("&cВы не можете удалить активную задачу генерации. Сначала поставьте ее на паузу.");
	@Setting("Start")
	private Component start = TextUtils.deserializeLegacy("&aЗапущена генерация чанков в мире &e\"" + Placeholders.WORLD + "\" &a.\n Если вы остановите сервер, то при повторном запуске этой операции генерация начнется с самого начала.");
	@Setting("Pause")
	private Component pause = TextUtils.deserializeLegacy("&aГенерация чанков в мире &e\"" + Placeholders.WORLD + "\"&a поставлена на паузу.");
	@Setting("Stop")
	private Component stop = TextUtils.deserializeLegacy("&aОстановлена генерация чанков в мире&e\"" + Placeholders.WORLD + "\"&a. Задача удалена.");

	@Override
	public String getDebug(ServerWorld world, double value, Vector3i lastChunk) {
		return debug.replace(Placeholders.WORLD, world.key().asString()).replace(Placeholders.VALUE, String.valueOf(value)).replace(Placeholders.LOCATION, lastChunk.toString());
	}

	@Override
	public Component getNotStarted(ServerWorld world) {
		return Text.of(notStarted).replace(Placeholders.WORLD, world.key().asString()).get();
	}

	@Override
	public Component getNotPaused() {
		return notPaused;
	}

	@Override
	public Component getStart(ServerWorld world) {
		return Text.of(start).replace(Placeholders.WORLD, world.key().asString()).get();
	}

	@Override
	public Component getPause(ServerWorld world) {
		return Text.of(pause).replace(Placeholders.WORLD, world.key().asString()).get();
	}

	@Override
	public Component getStop(ServerWorld world) {
		return Text.of(stop).replace(Placeholders.WORLD, world.key().asString()).get();
	}

}
