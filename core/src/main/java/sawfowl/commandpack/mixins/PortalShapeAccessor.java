package sawfowl.commandpack.mixins;

import org.spongepowered.api.world.server.ServerWorld;

import sawfowl.commandpack.api.game.PortalShape;

public interface PortalShapeAccessor extends PortalShape {

	PortalShape setWorld(ServerWorld world);

}
