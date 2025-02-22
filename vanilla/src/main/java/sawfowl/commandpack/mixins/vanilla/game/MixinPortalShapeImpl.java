package sawfowl.commandpack.mixins.vanilla.game;

import java.util.stream.StreamSupport;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.math.vector.Vector3i;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.portal.PortalShape;

@Mixin(PortalShape.class)
public abstract class MixinPortalShapeImpl implements sawfowl.commandpack.api.mixin.game.PortalShape {

	@Shadow private int numPortalBlocks;
	@Shadow private BlockPos bottomLeft;
	@Shadow private int height;
	@Shadow @Final private int width;
	@Shadow @Final private Direction rightDir;
	private Vector3i bl;

	@Shadow @Override public abstract boolean isValid();
	@Shadow @Override public abstract void createPortalBlocks();

	@Override
	public int totalPortalBlocks() {
		return numPortalBlocks;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public Vector3i getBottomLeft() {
		return bl == null ? bl = Vector3i.from(bottomLeft.getX(), bottomLeft.getY(), bottomLeft.getZ()) : bl;
	}

	@Override
	public boolean isCanBeFilled() {
		return StreamSupport.stream(BlockPos.betweenClosed(this.bottomLeft, this.bottomLeft.relative(Direction.UP, this.height - 1).relative(this.rightDir, this.width - 1)).spliterator(), false).count() > 1;
	}

}
