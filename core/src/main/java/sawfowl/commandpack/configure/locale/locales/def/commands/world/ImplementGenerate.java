package sawfowl.commandpack.configure.locale.locales.def.commands.world;

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
	private String debug = "The generation of chunks in world \"" + Placeholders.WORLD + "\" in progress. Done: " + Placeholders.VALUE + "%. The last generated chunk: " + Placeholders.LOCATION + ".";
	@Setting("NotStarted")
	private Component notStarted = TextUtils.deserializeLegacy("&cThe task of generating chunks in world &e\"" + Placeholders.WORLD + "\"&c is missing.");
	@Setting("NotPaused")
	private Component notPaused = TextUtils.deserializeLegacy("&cYou cannot delete an active generation task. Put it on pause first.");
	@Setting("Start")
	private Component start = TextUtils.deserializeLegacy("&aChunk generation in world &e\"" + Placeholders.WORLD + "\" &ahas started.\n If you stop the server, when you start this operation again, generation will start from the beginning.");
	@Setting("Pause")
	private Component pause = TextUtils.deserializeLegacy("&aChunk generation in world &e\"" + Placeholders.WORLD + "\"&a is suspended.");
	@Setting("Stop")
	private Component stop = TextUtils.deserializeLegacy("&aThe generation of chunks in world&e\"" + Placeholders.WORLD + "\"&a has been stopped. The task is deleted.");

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
