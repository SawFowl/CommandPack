package sawfowl.commandpack.apiclasses;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.math.vector.Vector3i;

public class RandomTeleportService implements sawfowl.commandpack.api.RandomTeleportService {

	public RandomTeleportService() {}

	@Override
	public Optional<ServerLocation> getLocation(ServerLocation currentLocation, ServerWorld world, RandomTeleportOptions options) {
		int attempts = 0;
		Optional<ServerLocation> location = Optional.empty();
		while(attempts < options.getAttempts()) {
			attempts++;
			Optional<Integer> optX = getRandomX(currentLocation, world, options, 0);
			if(!optX.isPresent()) break;
			Optional<Integer> optZ = getRandomZ(currentLocation, world, options, 0);
			if(!optZ.isPresent()) break;
			Vector3i newPos = Vector3i.from(optX.get(), getRandomInt(options.getMinY(), options.getMaxY()), optZ.get());
			if(!world.loadChunk(newPos, true).isPresent()) location = getLocation(currentLocation, world, options);
			if(options.isOnlySurface()) newPos = world.highestPositionAt(newPos);
			if(!options.getProhibitedBiomes().isEmpty()) {
				Optional<ResourceKey> optKey = Sponge.game().registry(RegistryTypes.BIOME).findValueKey(world.biome(newPos));
				if(optKey.isPresent() && !options.getProhibitedBiomes().contains(optKey.get().asString()) && isSafe(world, newPos)) location = Optional.ofNullable(world.location(newPos));
			} else {
				if(isSafe(world, newPos)) location = Optional.ofNullable(world.location(newPos));
			}
		}
		return location;
	}

	@Override
	public CompletableFuture<Optional<ServerLocation>> getLocationFuture(ServerLocation currentLocation,ServerWorld targetWorld, RandomTeleportOptions options) {
		return CompletableFuture.supplyAsync(new Supplier<Optional<ServerLocation>>() {
			@Override
			public Optional<ServerLocation> get() {
				return getLocation(currentLocation, targetWorld, options);
			}
		});
	}

	@Override
	public RandomTeleportOptions getOptions(ServerWorld world) {
		return null;
	}

	@Override
	public RandomTeleportOptions getOptions(ResourceKey worldKey) {
		return null;
	}

	@Override
	public RandomTeleportOptions getDefault() {
		return null;
	}

	private Optional<Integer> getRandomX(ServerLocation currentLocation, ServerWorld world, RandomTeleportOptions options, int attempts) {
		if(attempts >= options.getAttempts()) return Optional.empty();
		int x = options.isStartFromWorldSpawn() ? getRandomInt(world.properties().spawnPosition().x() + options.getMinRadius(), options.getRadius()) : getRandomInt(currentLocation.blockPosition().x() + options.getMinRadius(), options.getRadius());
		if(!ThreadLocalRandom.current().nextBoolean()) x = x * -1;
		if(x < world.min().x() || x > world.max().x()) {
			Optional<Integer> nextFind = getRandomX(currentLocation, world, options, attempts);
			if(nextFind.isPresent()) return nextFind;
		}
		return Optional.ofNullable(x);
	}

	private Optional<Integer> getRandomZ(ServerLocation currentLocation, ServerWorld world, RandomTeleportOptions options, int attempts) {
		if(attempts >= options.getAttempts()) return Optional.empty();
		int z = options.isStartFromWorldSpawn() ? getRandomInt(world.properties().spawnPosition().z() + options.getMinRadius(), options.getRadius()) : getRandomInt(currentLocation.blockPosition().z() + options.getMinRadius(), options.getRadius());
		if(!ThreadLocalRandom.current().nextBoolean()) z = z * -1;
		if(z < world.min().z() || z > world.max().z()) {
			Optional<Integer> nextFind = getRandomZ(currentLocation, world, options, attempts);
			if(nextFind.isPresent()) return nextFind;
		}
		return Optional.ofNullable(z);
	}
	
	private int getRandomInt(int first, int second) {
		return first < second ? ThreadLocalRandom.current().nextInt(first, second) : ThreadLocalRandom.current().nextInt(second, first);
	}

	private boolean isSafe(ServerWorld world, Vector3i pos) {
		return world.block(Vector3i.from(pos.x(), pos.y() + 1, pos.z())).type().equals(BlockTypes.AIR.get()) && world.block(Vector3i.from(pos.x(), pos.y() + 2, pos.z())).type().equals(BlockTypes.AIR.get()) && !world.block(pos).type().equals(BlockTypes.AIR.get());
	}

}
