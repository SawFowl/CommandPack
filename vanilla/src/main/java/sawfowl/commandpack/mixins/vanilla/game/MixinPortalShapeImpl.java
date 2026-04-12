package sawfowl.commandpack.mixins.vanilla.game;

import java.util.stream.StreamSupport;

import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.math.vector.Vector3i;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.portal.PortalShape;

import sawfowl.commandpack.mixins.PortalShapeAccessor;

@Mixin(PortalShape.class)
public abstract class MixinPortalShapeImpl implements PortalShapeAccessor {

    @Shadow @Final private int numPortalBlocks;
	@Shadow @Final private BlockPos bottomLeft;
	@Shadow @Final private int height;
	@Shadow @Final private int width;
	@Shadow @Final private Direction rightDir;
	@Unique private Vector3i commandPack$bl;
	@Unique private LevelAccessor commandPack$levelAccessor;

	@Shadow @Override public abstract boolean isValid();
	@Shadow public abstract void createPortalBlocks(LevelAccessor p_366077_);

	@Override
	public sawfowl.commandpack.api.game.PortalShape setWorld(ServerWorld world) {
		commandPack$levelAccessor = (LevelAccessor) world;
		return this;
	}

	@Override
	public void createPortalBlocks() {
		if(commandPack$levelAccessor != null) createPortalBlocks(commandPack$levelAccessor);
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
	public Vector3i getBottomLeft() {
		return commandPack$bl == null ? commandPack$bl = Vector3i.from(bottomLeft.getX(), bottomLeft.getY(), bottomLeft.getZ()) : commandPack$bl;
	}

	@Override
	public boolean isCanBeFilled() {
		return StreamSupport.stream(BlockPos.betweenClosed(this.bottomLeft, this.bottomLeft.relative(Direction.UP, this.height - 1).relative(this.rightDir, this.width - 1)).spliterator(), false).count() > 1;
	}

}
