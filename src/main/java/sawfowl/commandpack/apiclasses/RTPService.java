package sawfowl.commandpack.apiclasses;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.type.MatterTypes;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.world.biome.Biome;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.math.vector.Vector3i;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.RandomTeleportService;

public class RTPService implements RandomTeleportService {

	private final CommandPack plugin;
	public RTPService(CommandPack plugin) {
		this.plugin = plugin;
	}

	@Override
	public Optional<ServerLocation> getLocation(ServerLocation currentLocation, RandomTeleportOptions options) {
		int attempts = 0;
		ServerWorld world = options.getWorldKey() == null ? currentLocation.world() : Sponge.server().worldManager().world(options.getWorldKey()).orElse(currentLocation.world());
		while(attempts < options.getAttempts()) {
			attempts++;
			Optional<Integer> optX = getRandomX(currentLocation, world, options, attempts);
			if(!optX.isPresent()) break;
			Optional<Integer> optZ = getRandomZ(currentLocation, world, options, attempts);
			if(!optZ.isPresent()) break;
			Vector3i newPos = Vector3i.from(optX.get(), getRandomInt(options.getMinY(), options.getMaxY()), optZ.get());
			boolean prohibitedFluidBlock = options.isProhibitedLiquids() && isLiquid(world.location(newPos));
			boolean prohibitedBiome = options.getProhibitedBiomes() != null && options.getProhibitedBiomes().contains(biomeID(world.biome(newPos)));
			boolean prohibitedBlock = options.getProhibitedBlocks() != null && options.getProhibitedBlocks().contains(blockID(world.block(newPos)));
			if(!prohibitedFluidBlock && !prohibitedBiome && !prohibitedBlock) {
				Optional<ServerLocation> optLocation = Sponge.server().teleportHelper().findSafeLocation(world.location(newPos), 10, 10, 10);
				if(optLocation.isPresent()) {
					if(options.isOnlySurface() || !isSafe(optLocation.get())) optLocation = Optional.ofNullable(ServerLocation.of(world, world.highestPositionAt(optLocation.get().blockPosition())));
					attempts = options.getAttempts() + 1;
					return optLocation;
				}
			}
		}
		return Optional.empty();
	}

	@Override
	public CompletableFuture<Optional<ServerLocation>> getLocationFuture(ServerLocation currentLocation, RandomTeleportOptions options) {
		return CompletableFuture.supplyAsync(new Supplier<Optional<ServerLocation>>() {
			@Override
			public Optional<ServerLocation> get() {
				return getLocation(currentLocation, options);
			}
		});
	}

	@Override
	public RandomTeleportOptions getOptions(ServerWorld world) {
		return getOptions(world.key());
	}

	@Override
	public RandomTeleportOptions getOptions(ResourceKey worldKey) {
		return plugin.getMainConfig().getRtpConfig().getRandomTeleportWorldConfig(worldKey).orElse(plugin.getMainConfig().getRtpConfig()).copy();
	}

	@Override
	public RandomTeleportOptions getDefault() {
		return plugin.getMainConfig().getRtpConfig().copy();
	}

	private Optional<Integer> getRandomX(ServerLocation currentLocation, ServerWorld world, RandomTeleportOptions options, int attempts) {
		if(attempts >= options.getAttempts()) return Optional.empty();
		attempts++;
		boolean rand = ThreadLocalRandom.current().nextBoolean();
		int minRadius = rand ? options.getMinRadius() : options.getMinRadius() * -1;
		int radius = rand ? options.getRadius() : options.getRadius() * -1;
		int x = (options.isStartFromWorldSpawn() ? world.properties().spawnPosition() : currentLocation.blockPosition()).x();
		x = getRandomInt(x + minRadius, x + radius);
		if(!ThreadLocalRandom.current().nextBoolean()) x = x * -1;
		if(x < world.min().x() || x > world.max().x()) {
			Optional<Integer> nextFind = getRandomX(currentLocation, world, options, attempts);
			if(nextFind.isPresent()) return nextFind;
		}
		return Optional.ofNullable(x);
	}

	private Optional<Integer> getRandomZ(ServerLocation currentLocation, ServerWorld world, RandomTeleportOptions options, int attempts) {
		if(attempts >= options.getAttempts()) return Optional.empty();
		attempts++;
		boolean rand = ThreadLocalRandom.current().nextBoolean();
		int minRadius = rand ? options.getMinRadius() : options.getMinRadius() * -1;
		int radius = rand ? options.getRadius() : options.getRadius() * -1;
		int z = (options.isStartFromWorldSpawn() ? world.properties().spawnPosition() : currentLocation.blockPosition()).z();
		z = getRandomInt(z + minRadius, z + radius);
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

	private boolean isSafe(ServerLocation location) {
		return location.world().block(location.blockPosition()).type().equals(BlockTypes.AIR.get()) && location.world().block(location.blockPosition()).type().equals(BlockTypes.AIR.get()) && !location.world().block(location.blockPosition()).type().equals(BlockTypes.AIR.get());
	}

	private boolean isLiquid(ServerLocation location) {
		return location.get(Keys.MATTER_TYPE).isPresent() && location.get(Keys.MATTER_TYPE).get().equals(MatterTypes.LIQUID.get());
	}

	private String blockID(BlockState block) {
		return Sponge.game().registry(RegistryTypes.BLOCK_TYPE).valueKey(block.type()).asString();
	}

	private String biomeID(Biome biome) {
		return Sponge.game().registry(RegistryTypes.BIOME).valueKey(biome).asString();
	}

}
