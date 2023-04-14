package sawfowl.commandpack.configure.configs.commands;

import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.Queries;
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
	private String world = null;
	@Setting("StartFromWorldSpawn")
	private boolean startFromWorldSpawn = false;
	@Setting("MinRadius")
	private int minRadius = 1000;
	@Setting("Radius")
	private int radius = 3000;
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

	public Builder builder() {
		return new Builder();
	}

	@Override
	public RandomTeleportOptions copy() {
		RandomTeleportWorldConfig copy = new RandomTeleportWorldConfig();
		copy.attempts = attempts;
		copy.world = world;
		copy.startFromWorldSpawn = startFromWorldSpawn;
		copy.minRadius = minRadius;
		copy.radius = radius;
		copy.maxY = maxY;
		copy.minY = minY;
		copy.prohibitedBiomes = prohibitedBiomes;
		copy.onlySurface = onlySurface;
		return copy;
	}

	@Override
	public ResourceKey getWorldKey() {
		return ResourceKey.resolve(world);
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

	@Override
	public int contentVersion() {
		return 1;
	}

	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew()
				.set(DataQuery.of("Attempts"), attempts)
				.set(DataQuery.of("World"), world)
				.set(DataQuery.of("StartFromWorldSpawn"), startFromWorldSpawn)
				.set(DataQuery.of("MinRadius"), minRadius)
				.set(DataQuery.of("Radius"), radius)
				.set(DataQuery.of("MaxY"), maxY)
				.set(DataQuery.of("MinY"), minY)
				.set(DataQuery.of("ProhibitedBiomes"), prohibitedBiomes)
				.set(DataQuery.of("OnlySurface"), onlySurface)
				.set(Queries.CONTENT_VERSION, contentVersion());
	}

	class Builder implements RandomTeleportOptions.Builder {

		@Override
		public Builder setAttempts(int attempts) {
			RandomTeleportWorldConfig.this.attempts = attempts;
			return this;
		}

		@Override
		public @NotNull RandomTeleportOptions build() {
			return RandomTeleportWorldConfig.this;
		}

		@Override
		public Builder setWorldKey(ResourceKey worldKey) {
			world = worldKey.asString();
			return this;
		}

		@Override
		public Builder setWorldID(String world) throws Exception {
			if(!world.contains(":")) throw new Exception("The string is not an identifier of any world.");
			RandomTeleportWorldConfig.this.world = world;
			return this;
		}

		@Override
		public Builder setStartFromWorldSpawn(boolean value) {
			startFromWorldSpawn = value;
			return this;
		}

		@Override
		public Builder setMinRadius(int value) {
			minRadius = value;
			return this;
		}

		@Override
		public Builder setRadius(int value) {
			radius = value;
			return this;
		}

		@Override
		public Builder setMaxY(int value) {
			maxY = value;
			return this;
		}

		@Override
		public Builder setMinY(int value) {
			minY = value;
			return this;
		}

		@Override
		public Builder setProhibitedBiomes(Set<String> value) {
			prohibitedBiomes = value;
			return this;
		}

		@Override
		public Builder setOnlySurface(boolean value) {
			onlySurface = value;
			return this;
		}
		
	}

}
