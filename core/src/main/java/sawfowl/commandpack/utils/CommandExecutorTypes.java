package sawfowl.commandpack.utils;

import java.util.Optional;
import java.util.stream.Stream;

import org.spongepowered.api.Server;
import org.spongepowered.api.SystemSubject;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.util.Nameable;
import org.spongepowered.api.world.LocatableBlock;

public enum CommandExecutorTypes {

	SYSTEM {

		@Override
		protected boolean check(CommandCause cause) {
			return !(cause.audience() instanceof Nameable) && (cause.audience() instanceof SystemSubject || cause.audience() instanceof Server);
		}

	},
	COMMAND_BLOCK {

		@Override
		protected boolean check(CommandCause cause) {
			return getLocatableBlock(cause).filter(block -> (block.blockState().type().equals(BlockTypes.COMMAND_BLOCK.get()) || block.blockState().type().equals(BlockTypes.CHAIN_COMMAND_BLOCK.get()) || block.blockState().type().equals(BlockTypes.REPEATING_COMMAND_BLOCK.get()))).isPresent();
		}

	},
	COMMAND_BLOCK_MINECART {

		@Override
		protected boolean check(CommandCause cause) {
			return getEntity(cause).isPresent();
		}

	},
	NAMEABLE {

		@Override
		protected boolean check(CommandCause cause) {
			return cause.audience() instanceof Nameable;
		}

	},
	CUSTOM_NPC {

		@Override
		protected boolean check(CommandCause cause) {
			return cause.cause().root().getClass().getName().contains("noppes.npcs.NoppesUtilServer");
		}

	},
	UNKNOWN {

		@Override
		protected boolean check(CommandCause cause) {
			return false;
		}

	};

	public static CommandExecutorTypes findType(CommandCause cause) {
		return Stream.of(CommandExecutorTypes.values()).filter(type -> type.check(cause)).findFirst().orElse(UNKNOWN);
	}

	protected abstract boolean check(CommandCause cause);

	private static Optional<LocatableBlock> getLocatableBlock(CommandCause cause) {
		return cause.first(LocatableBlock.class);
	}

	private static Optional<Entity> getEntity(CommandCause cause) {
		return cause.first(Entity.class).filter(entity -> (entity.type().equals(EntityTypes.COMMAND_BLOCK_MINECART.get())));
	}

}
