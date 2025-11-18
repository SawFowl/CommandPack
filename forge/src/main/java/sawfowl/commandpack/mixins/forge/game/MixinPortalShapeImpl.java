package sawfowl.commandpack.mixins.forge.game;

import java.util.stream.StreamSupport;

import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.math.vector.Vector3i;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.portal.PortalShape;

import sawfowl.commandpack.mixins.PortalShapeAccessor;

@Mixin(PortalShape.class)
public abstract class MixinPortalShapeImpl implements PortalShapeAccessor {

	@Shadow private int numPortalBlocks;
	@Shadow private BlockPos bottomLeft;
	@Shadow private int height;
	@Shadow @Final private int width;
	@Shadow @Final private Direction rightDir;
	private Vector3i bl;
	private LevelAccessor levelAccessor;

	@Shadow @Override public abstract boolean isValid();
	@Shadow public abstract void createPortalBlocks(LevelAccessor p_366077_);

	@Override
	public sawfowl.commandpack.api.mixin.game.PortalShape setWorld(ServerWorld world) {
		levelAccessor = (LevelAccessor) world;
		return this;
	}

	@Override
	public void createPortalBlocks() {
		if(levelAccessor != null) createPortalBlocks(levelAccessor);
	}

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
	public void createPortalBlocks() {
		createPortalBlocks((LevelAccessor) world);
	}

	@Override
	public sawfowl.commandpack.api.mixin.game.PortalShape setWorld(ServerWorld world) {
		this.world = world;
		return this;
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
