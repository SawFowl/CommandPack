package sawfowl.commandpack.api.tps;

import org.spongepowered.api.world.server.ServerWorld;

public interface TPS {

	/**
	 * View average TPS values over time intervals of 1m, 5m, 10m.
	 */
	AverageTPS getAverageTPS();

	/**
	 * Getting the TPS of the world.
	 */
	double getWorldTPS(ServerWorld world);

	/**
	 * Getting the world's ticking time.
	 */
	double getWorldTickTime(ServerWorld world);

}
