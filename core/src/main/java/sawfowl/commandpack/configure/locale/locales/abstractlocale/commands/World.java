package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.math.vector.Vector3i;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.world.Difficulty;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.world.GameMode;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.world.GameRule;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.world.Generate;

public interface World {

	Difficulty getDifficulty();

	GameRule getGameRule();

	GameMode getGameMode();

	Generate getGenerate();

	Component getCreate(String world);

	Component getTeleport(String world);

	Component getTeleportStaff(ServerPlayer player, String world);

	Component getDelete(String world);

	Component getUnload(String world);

	Component getNotLoaded(String world);

	Component getLoad(String world);

	Component getAlreadyLoaded(String world);

	Component getSetSpawn(ServerWorld world, Vector3i location);

	Component getSetBorder(ServerWorld world, Vector3i location, int radius);

	Component getEnable(ServerWorld world);

	Component getDisable(ServerWorld world);

	Component getEnablePvP(ServerWorld world);

	Component getDisablePvP(ServerWorld world);

	Component getSetViewDistance(ServerWorld world, int radius);

}
