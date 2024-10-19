package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.math.vector.Vector3i;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.World;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.world.Difficulty;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.world.GameMode;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.world.GameRule;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.world.Generate;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.world.SpawnLogic;
import sawfowl.commandpack.configure.locale.locales.ru.commands.world.ImplementDifficulty;
import sawfowl.commandpack.configure.locale.locales.ru.commands.world.ImplementGameRule;
import sawfowl.commandpack.configure.locale.locales.ru.commands.world.ImplementGameMode;
import sawfowl.commandpack.configure.locale.locales.ru.commands.world.ImplementGenerate;
import sawfowl.commandpack.configure.locale.locales.ru.commands.world.ImplementSpawnLogic;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementWorld implements World {

	public ImplementWorld() {}

	@Setting("Difficulty")
	private ImplementDifficulty difficulty = new ImplementDifficulty();
	@Setting("GameRule")
	private ImplementGameRule gameRule = new ImplementGameRule();
	@Setting("GameMode")
	private ImplementGameMode gameMode = new ImplementGameMode();
	@Setting("Generate")
	private ImplementGenerate generate = new ImplementGenerate();
	@Setting("SpawnLogic")
	private ImplementSpawnLogic spawnLogic = new ImplementSpawnLogic();
	@Setting("Create")
	private Component create = TextUtils.deserializeLegacy("&aМир &e\"" + Placeholders.WORLD + "\"&a создан.\n&eМиры создаются в асинхронном режиме. Без перезагрузки сервера мир будет статичным.");
	@Setting("Teleport")
	private Component teleport = TextUtils.deserializeLegacy("&aВы телепортировались в мир &e\"" + Placeholders.WORLD + "\"&a.");
	@Setting("TeleportStaff")
	private Component teleportStaff = TextUtils.deserializeLegacy("&aВы телепортировали игрока &e" + Placeholders.PLAYER + "&a в мир &e\"" + Placeholders.WORLD + "\"&a.");
	@Setting("Delete")
	private Component delete = TextUtils.deserializeLegacy("&aМир &e\"" + Placeholders.WORLD + "\"&a удален.");
	@Setting("Unload")
	private Component unload = TextUtils.deserializeLegacy("&aМир &e\"" + Placeholders.WORLD + "\"&a выгружен.");
	@Setting("NotLoaded")
	private Component notLoaded = TextUtils.deserializeLegacy("&cМир &e\"" + Placeholders.WORLD + "\"&c не загружен.");
	@Setting("Load")
	private Component load = TextUtils.deserializeLegacy("&aМир &e\"" + Placeholders.WORLD + "\"&a загружен.");
	@Setting("AlreadyLoaded")
	private Component alreadyLoaded = TextUtils.deserializeLegacy("&cМир &e\"" + Placeholders.WORLD + "\"&c уже загружен.");
	@Setting("SetSpawn")
	private Component setSpawn = TextUtils.deserializeLegacy("&aТочка спавна в мире &e\"" + Placeholders.WORLD + "\"&a установленна по координатам &e" + Placeholders.LOCATION + "&a.");
	@Setting("SetBorder")
	private Component setBorder = TextUtils.deserializeLegacy("&aДиаметр границы мира &e\"" + Placeholders.WORLD + "\"&a установлен в занчении &e" + Placeholders.VALUE + "&a блок(а/ов) с центром по координатам &e" + Placeholders.LOCATION + "&a.");
	@Setting("Enable")
	private Component enable = TextUtils.deserializeLegacy("&aМир &e\"" + Placeholders.WORLD + "\"&a будет загружаться при запуске сервера.");
	@Setting("Disable")
	private Component disable = TextUtils.deserializeLegacy("&aМир &e\"" + Placeholders.WORLD + "\"&a не будет загружаться при запуске сервера.");
	@Setting("EnablePvP")
	private Component enablePvP = TextUtils.deserializeLegacy("&aPvP включено в мире &e\"" + Placeholders.WORLD + "\"&a.");
	@Setting("DisablePvP")
	private Component disablePvP = TextUtils.deserializeLegacy("&aPvP выключено в мире &e\"" + Placeholders.WORLD + "\"&a.");
	@Setting("SetViewDistance")
	private Component setViewDistance = TextUtils.deserializeLegacy("&aДальность видимости в мире &e\"" + Placeholders.WORLD + "\"&a теперь составляет &e" + Placeholders.VALUE + "&a.");

	@Override
	public Difficulty getDifficulty() {
		return difficulty;
	}

	@Override
	public GameRule getGameRule() {
		return gameRule;
	}

	@Override
	public GameMode getGameMode() {
		return gameMode;
	}

	@Override
	public Generate getGenerate() {
		return generate;
	}

	@Override
	public SpawnLogic getSpawnLogic() {
		return spawnLogic;
	}

	@Override
	public Component getCreate(String world) {
		return Text.of(create).replace(Placeholders.WORLD, world).get();
	}

	@Override
	public Component getTeleport(String world) {
		return Text.of(teleport).replace(Placeholders.WORLD, world).get();
	}

	@Override
	public Component getTeleportStaff(ServerPlayer player, String world) {
		return Text.of(teleportStaff).replace(Placeholders.PLAYER, player.name()).replace(Placeholders.WORLD, world).get();
	}

	@Override
	public Component getDelete(String world) {
		return Text.of(delete).replace(Placeholders.WORLD, world).get();
	}

	@Override
	public Component getUnload(String world) {
		return Text.of(unload).replace(Placeholders.WORLD, world).get();
	}

	@Override
	public Component getNotLoaded(String world) {
		return Text.of(notLoaded).replace(Placeholders.WORLD, world).get();
	}

	@Override
	public Component getLoad(String world) {
		return Text.of(load).replace(Placeholders.WORLD, world).get();
	}

	@Override
	public Component getAlreadyLoaded(String world) {
		return Text.of(alreadyLoaded).replace(Placeholders.WORLD, world).get();
	}

	@Override
	public Component getSetSpawn(ServerWorld world, Vector3i location) {
		return Text.of(setSpawn).replace(Placeholders.WORLD, world.key().asString()).replace(Placeholders.LOCATION, location.toString()).get();
	}

	@Override
	public Component getSetBorder(ServerWorld world, Vector3i location, int radius) {
		return Text.of(setBorder).replace(Placeholders.WORLD, world.key().asString()).replace(Placeholders.LOCATION, location.toString()).replace(Placeholders.VALUE, radius).get();
	}

	@Override
	public Component getEnable(ServerWorld world) {
		return Text.of(enable).replace(Placeholders.WORLD, world.key().asString()).get();
	}

	@Override
	public Component getDisable(ServerWorld world) {
		return Text.of(disable).replace(Placeholders.WORLD, world.key().asString()).get();
	}

	@Override
	public Component getEnablePvP(ServerWorld world) {
		return Text.of(enablePvP).replace(Placeholders.WORLD, world.key().asString()).get();
	}

	@Override
	public Component getDisablePvP(ServerWorld world) {
		return Text.of(disablePvP).replace(Placeholders.WORLD, world.key().asString()).get();
	}

	@Override
	public Component getSetViewDistance(ServerWorld world, int radius) {
		return Text.of(setViewDistance).replace(Placeholders.WORLD, world.key().asString()).replace(Placeholders.VALUE, radius).get();
	}

}
