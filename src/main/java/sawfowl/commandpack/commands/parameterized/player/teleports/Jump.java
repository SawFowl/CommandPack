package sawfowl.commandpack.commands.parameterized.player.teleports;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.util.blockray.RayTrace;
import org.spongepowered.api.util.blockray.RayTraceResult;
import org.spongepowered.api.world.LocatableBlock;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.math.vector.Vector3d;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.parameterized.settings.ParameterSettings;
import sawfowl.commandpack.configure.configs.commands.CommandSettings;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Jump extends AbstractPlayerCommand {

	public Jump(CommandPack plugin, String command, CommandSettings commandSettings) {
		super(plugin, command, commandSettings);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		Optional<RayTraceResult<LocatableBlock>> blockRayTraceResult = RayTrace.block()
			.sourceEyePosition(src)
			.direction(src.direction())
			.limit(50)
			.select(RayTrace.nonAir())
			.continueWhileBlock(RayTrace.onlyAir())
			.execute();
		if(!blockRayTraceResult.isPresent()) {
			Vector3d direction = src.direction();
			src.setPosition(src.position().add(Vector3d.from(direction.x() * 50, direction.y() * 50, direction.z() * 50)));
		} else {
			LocatableBlock block = blockRayTraceResult.get().selectedObject();
			ServerLocation targetLocation;
			if(RayTrace.onlyAir().test(block.serverLocation().add(0, 1, 0).asLocatableBlock()) &&
				RayTrace.onlyAir().test(block.serverLocation().add(0, 2, 0).asLocatableBlock())) {
				targetLocation = block.serverLocation().add(0, 1, 0);
			} else {
				Optional<ServerLocation> find = Sponge.server().teleportHelper().findSafeLocation(block.serverLocation());
				if(!find.isPresent()) exception(locale, LocalesPaths.COMMANDS_JUMP_EXCEPTION);
				targetLocation = find.get();
			}
			src.setLocation(targetLocation);
		}
	}

	@Override
	public Parameterized build() {
		return null;
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

	@Override
	protected String permission() {
		return Permissions.JUMP;
	}

}
