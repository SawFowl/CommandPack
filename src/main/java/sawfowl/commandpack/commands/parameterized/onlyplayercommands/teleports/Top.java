package sawfowl.commandpack.commands.parameterized.onlyplayercommands.teleports;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.DefaultWorldKeys;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.math.vector.Vector3d;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;

public class Top extends AbstractPlayerCommand {

	public Top(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		delay(src, locale, consumer -> {
			plugin.getPlayersData().getTempData().setPreviousLocation(src);
			Vector3d position = src.world().highestPositionAt(src.blockPosition()).toDouble();
			if(src.world().key().equals(DefaultWorldKeys.THE_NETHER) && plugin.getMainConfig().isFixTopCommand(src.world())) {
				src.setPosition(findSafe(ServerLocation.of(src.world(), Vector3d.from(position.x(), position.y()-1, position.z()))).map(ServerLocation::position).orElse(position));
			} else src.setPosition(position);
		});
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

	@Override
	public String permission() {
		return Permissions.TOP;
	}

	@Override
	public String command() {
		return "top";
	}

	private Optional<ServerLocation> findSafe(ServerLocation location) {
		for(double y = location.y(); y > 0; y--) {
			ServerLocation find = ServerLocation.of(location.world(), location.x(), y, location.z());
			if(isSafe(find) && !find.block().type().equals(BlockTypes.BEDROCK.get())) return Optional.ofNullable(find);
		}
		return Optional.empty();
	}

	private boolean isSafe(ServerLocation location) {
		return location.block().type().equals(BlockTypes.AIR.get()) && ServerLocation.of(location.world(), location.x(), location.y() + 1, location.z()).block().type().equals(BlockTypes.AIR.get()) && !ServerLocation.of(location.world(), location.x(), location.y() - 1, location.z()).block().type().equals(BlockTypes.AIR.get());
	}

}
