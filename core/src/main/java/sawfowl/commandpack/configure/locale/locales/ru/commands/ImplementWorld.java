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
import sawfowl.commandpack.configure.locale.locales.def.commands.world.ImplementDifficulty;
import sawfowl.commandpack.configure.locale.locales.def.commands.world.ImplementGameRule;
import sawfowl.commandpack.configure.locale.locales.def.commands.world.ImplementGameMode;
import sawfowl.commandpack.configure.locale.locales.def.commands.world.ImplementGenerate;
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
	private Component create = TextUtils.deserializeLegacy("&aThe world &e\"" + Placeholders.WORLD + "\"&a is created.\n&eWorlds are created in asynchronous mode. Without server restart the world will be static.");
	@Setting("Teleport")
	private Component teleport = TextUtils.deserializeLegacy("&aYou teleported into the world &e\"" + Placeholders.WORLD + "\"&a.");
	@Setting("TeleportStaff")
	private Component teleportStaff = TextUtils.deserializeLegacy("&aYou teleported the player &e" + Placeholders.PLAYER + "&a to the world &e\"" + Placeholders.WORLD + "\"&a.");
	@Setting("Delete")
	private Component delete = TextUtils.deserializeLegacy("&aThe world &e\"" + Placeholders.WORLD + "\"&a is deleted.");
	@Setting("Unload")
	private Component unload = TextUtils.deserializeLegacy("&aThe world &e\"" + Placeholders.WORLD + "\"&a is unloaded.");
	@Setting("NotLoaded")
	private Component notLoaded = TextUtils.deserializeLegacy("&cThe world &e\"" + Placeholders.WORLD + "\"&c is not loaded.");
	@Setting("Load")
	private Component load = TextUtils.deserializeLegacy("&aThe world &e\"" + Placeholders.WORLD + "\"&a is loaded.");
	@Setting("AlreadyLoaded")
	private Component alreadyLoaded = TextUtils.deserializeLegacy("&cThe world &e\"" + Placeholders.WORLD + "\"&c is already loaded.");
	@Setting("SetSpawn")
	private Component setSpawn = TextUtils.deserializeLegacy("&aSpawn point is set in the world &e\"" + Placeholders.WORLD + "\"&a at the coordinates &e" + Placeholders.LOCATION + "&a.");
	@Setting("SetBorder")
	private Component setBorder = TextUtils.deserializeLegacy("&aThe diameter of world &e\"" + Placeholders.WORLD + "\"&a border is set to the value &e" + Placeholders.VALUE + "&a with the center at the coordinates &e" + Placeholders.LOCATION + "&a.");
	@Setting("Enable")
	private Component enable = TextUtils.deserializeLegacy("&aThe world &e\"" + Placeholders.WORLD + "\"&a will be loaded when the server starts up.");
	@Setting("Disable")
	private Component disable = TextUtils.deserializeLegacy("&aThe world &e\"" + Placeholders.WORLD + "\"&a will not load when the server starts.");
	@Setting("EnablePvP")
	private Component enablePvP = TextUtils.deserializeLegacy("&aPvP enabled in the world &e\"" + Placeholders.WORLD + "\"&a.");
	@Setting("DisablePvP")
	private Component disablePvP = TextUtils.deserializeLegacy("&aPvP disabled in the world &e\"" + Placeholders.WORLD + "\"&a.");
	@Setting("SetViewDistance")
	private Component setViewDistance = TextUtils.deserializeLegacy("&aThe view distance in world &e\"" + Placeholders.WORLD + "\"&a is set to &e" + Placeholders.VALUE + "&a.");

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
