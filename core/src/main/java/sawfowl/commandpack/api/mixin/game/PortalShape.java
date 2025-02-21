package sawfowl.commandpack.api.mixin.game;

import org.spongepowered.math.vector.Vector3i;

public interface PortalShape {

	boolean isValid();

	void createPortalBlocks();

	int totalPortalBlocks();

	int getHeight();

	int getWidth();

	Vector3i getBottomLeft();

}
