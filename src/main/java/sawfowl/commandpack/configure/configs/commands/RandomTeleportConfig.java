package sawfowl.commandpack.configure.configs.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.api.data.miscellaneous.RandomTeleportOptions;

@ConfigSerializable
public class RandomTeleportConfig {

	@Setting("Attempts")
	@Comment("The number of attempts to find the position.")
	private int attempts = 10;
	@Setting("WorldSelector")
	@Comment("Specifies the target world for teleportation by the player's current world.\nIf the player's current world is not listed in this worlds map as a source world, the player will be teleported to the same world he is in.")
	private Map<String, RandomTeleportWorldConfig> map = createDefault();
	@Setting("StartFromWorldSpawn")
	@Comment("If true, the search for a random position will be performed from the world spawn point.\nIf false, the search will be performed from the current coordinates of the player.")
	private boolean startFromWorldSpawn = false;
	@Setting("MinRadius")
	@Comment("Minimum teleportation distance.")
	private int minRadius = 1000;
	@Setting("Radius")
	@Comment("Maximum teleportation distance.")
	private int radius= 3000;
	@Setting("MaxY")
	@Comment("Maximum height for finding a position.")
	private int maxY = 255;
	@Setting("MinY")
	@Comment("Minimum height for finding a position.")
	private int minY = 10;
	@Setting("ProhibitedBiomes")
	@Comment("Biomes specified in this list will not be available for teleportation by random coordinates.")
	private Set<String> prohibitedBiomes = new HashSet<>();
	@Setting("OnlySurface")
	@Comment("If true, the player will always move to the surface.")
	private boolean onlySurface = false;

	public ServerWorld getTargetWorld(ServerWorld source) {
		return Sponge.server().worldManager().world(getTargetWorldId(getWorldId(source))).orElse(source);
	}

	public int getAttempts(ServerWorld world) {
		return map.containsKey(getWorldId(world)) ? map.get(getWorldId(world)).getAttempts() : attempts;
	}

	public boolean isStartFromWorldSpawn(ServerWorld world) {
		return map.containsKey(getWorldId(world)) ? map.get(getWorldId(world)).isStartFromWorldSpawn() : startFromWorldSpawn;
	}

	public int getMinRadius(ServerWorld world) {
		return map.containsKey(getWorldId(world)) ? map.get(getWorldId(world)).getMinRadius() : minRadius;
	}

	public int getRadius(ServerWorld world) {
		return map.containsKey(getWorldId(world)) ? map.get(getWorldId(world)).getRadius() : radius;
	}

	public int getMaxY(ServerWorld world) {
		return map.containsKey(getWorldId(world)) ? map.get(getWorldId(world)).getMaxY() : maxY;
	}

	public int getMinY(ServerWorld world) {
		return map.containsKey(getWorldId(world)) ? map.get(getWorldId(world)).getMinY() : minY;
	}

	public boolean isOnlySurface(ServerWorld world) {
		return map.containsKey(getWorldId(world)) ? map.get(getWorldId(world)).isOnlySurface() : onlySurface;
	}

	public Optional<RandomTeleportOptions> getRandomTeleportWorldConfig(ServerWorld world) {
		return Optional.ofNullable(map.getOrDefault(getWorldId(world), null));
	}

	private ResourceKey getTargetWorldId(String sourceWorldId) {
		return map.containsKey(sourceWorldId) ? map.get(sourceWorldId).getWorldKey() : ResourceKey.resolve(sourceWorldId);
	}

	private String getWorldId(ServerWorld world) {
		return world.key().asString();
	}

	private Map<String, RandomTeleportWorldConfig> createDefault() {
		Map<String, RandomTeleportWorldConfig> map = new HashMap<>();
		map.put("minecraft:overworld", new RandomTeleportWorldConfig("minecraft:overworld", new HashSet<>(Arrays.asList("minecraft:ocean", "minecraft:deep_ocean", "minecraft:frozen_ocean"))));
		map.put("minecraft:the_nether", new RandomTeleportWorldConfig("minecraft:the_nether", false));
		map.put("minecraft:the_end", new RandomTeleportWorldConfig("minecraft:the_end"));
		return map;
	}

}
