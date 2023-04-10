package sawfowl.commandpack.apiclasses.events;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.impl.AbstractEvent;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.math.vector.Vector3d;

import sawfowl.commandpack.api.RandomTeleportService.RandomTeleportOptions;

public class RandomTeleportEvent extends AbstractEvent implements sawfowl.commandpack.api.events.RandomTeleportEvent {

	private final ServerPlayer player;
	private final Cause cause;
	private final Vector3d originalPosition;
	private final Vector3d originalDestinationPosition;
	private Vector3d destinationPosition;
	private boolean cancelled = false;
	private ServerWorld destinationWorld;
	private RandomTeleportOptions options;
	public RandomTeleportEvent(Cause cause, ServerPlayer player, ServerWorld destinationWorld, Vector3d destinationPosition, RandomTeleportOptions options) {
		this.player = player;
		this.cause = cause;
		originalPosition = player.position();
		originalDestinationPosition = destinationPosition;
		setDestinationPosition(destinationPosition);
		setDestinationWorld(destinationWorld);
	}

	@Override
	public ServerPlayer player() {
		return player;
	}

	@Override
	public Entity entity() {
		return player;
	}

	@Override
	public Vector3d originalPosition() {
		return originalPosition;
	}

	@Override
	public Vector3d originalDestinationPosition() {
		return originalDestinationPosition;
	}

	@Override
	public Vector3d destinationPosition() {
		return destinationPosition;
	}

	@Override
	public void setDestinationPosition(Vector3d position) {
		destinationPosition = position;
	}

	@Override
	public Cause cause() {
		return cause;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

	@Override
	public ServerWorld getDestinationWorld() {
		return destinationWorld;
	}

	@Override
	public void setDestinationWorld(ServerWorld world) {
		destinationWorld = world;
	}

	@Override
	public RandomTeleportOptions getOptions() {
		return options;
	}

}
