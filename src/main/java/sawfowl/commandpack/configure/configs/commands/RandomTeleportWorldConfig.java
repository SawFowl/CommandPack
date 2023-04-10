package sawfowl.commandpack.configure.configs.commands;

import java.util.Set;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.api.RandomTeleportService.RandomTeleportOptions;

@ConfigSerializable
public class RandomTeleportWorldConfig implements RandomTeleportOptions {

	public RandomTeleportWorldConfig(){}

	@Setting("Attempts")
	private int attempts = 10;
	@Setting("World")
	@Comment("The identifier of the target world.")
	private String world;
	@Setting("StartFromWorldSpawn")
	private boolean startFromWorldSpawn = false;
	@Setting("MinRadius")
	private int minRadius = 1000;
	@Setting("Radius")
	private int radius= 3000;
	@Setting("MaxY")
	private int maxY = 255;
	@Setting("MinY")
	private int minY = 10;
	@Setting("ProhibitedBiomes")
	private Set<String> prohibitedBiomes;
	@Setting("OnlySurface")
	private boolean onlySurface = true;

	public RandomTeleportWorldConfig(String world) {
		this.world = world;
	}

	public RandomTeleportWorldConfig(String world, Set<String> prohibitedBiomes) {
		this.world = world;
		this.prohibitedBiomes = prohibitedBiomes;
	}

	public RandomTeleportWorldConfig(String world, boolean onlySurface) {
		this.world = world;
		this.onlySurface = onlySurface;
	}

	public RandomTeleportWorldConfig(String world, Set<String> prohibitedBiomes, boolean onlySurface) {
		this.world = world;
		this.prohibitedBiomes = prohibitedBiomes;
		this.onlySurface = onlySurface;
	}

	public ResourceKey getWorldKey() {
		return ResourceKey.resolve(world);
	}

	public RandomTeleportOptions asOptions() {
		return this;
	}

	@Override
	public int getAttempts() {
		return attempts;
	}

	@Override
	public boolean isStartFromWorldSpawn() {
		return startFromWorldSpawn;
	}

	@Override
	public int getMinRadius() {
		return minRadius;
	}

	@Override
	public int getRadius() {
		return radius;
	}

	@Override
	public int getMaxY() {
		return maxY;
	}

	@Override
	public int getMinY() {
		return minY;
	}

	@Override
	public Set<String> getProhibitedBiomes() {
		return prohibitedBiomes;
	}

	@Override
	public boolean isOnlySurface() {
		return onlySurface;
	}

}
