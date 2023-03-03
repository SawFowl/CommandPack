package sawfowl.commandpack.commands.parameterized;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.DefaultWorldKeys;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.math.vector.Vector3d;

import net.kyori.adventure.audience.Audience;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.commands.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractCommand;
import sawfowl.commandpack.configure.configs.miscellaneous.LocationData;
import sawfowl.commandpack.configure.configs.miscellaneous.SpawnData;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Spawn extends AbstractCommand {

	public Spawn(CommandPack plugin, String command, String[] aliases) {
		super(plugin, command, aliases);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale) throws CommandException {
		Optional<SpawnData> spawn = plugin.getMainConfig().getSpawnData();
		if(!(src instanceof ServerPlayer)) {
			if(!getPlayer(context).isPresent()) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT);
			teleport(getPlayer(context).get(), spawn);
		} else {
			ServerPlayer player = getPlayer(context).orElse((ServerPlayer) src);
			teleport(player, spawn);
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.SPAWN;
	}

	@Override
	public Optional<List<ParameterSettings>> getParameterSettings() {
		return Optional.empty();
	}

	private void teleport(ServerPlayer player, Optional<SpawnData> location) {
		if(location.isPresent() && location.get().getLocationData().getServerLocation().isPresent()) {
			player.setLocation(location.get().getLocationData().getServerLocation().get());
			location.get().getLocationData().getPosition().getRotation().ifPresent(rotation -> {
				player.setRotation(rotation.rotation());
			});
		} else tpDefault(player);
	}

	private void tpDefault(ServerPlayer player) {
		player.setLocation(defaultWorld().location(defaultWorld().properties().spawnPosition()));
	}

	private ServerWorld defaultWorld() {
		return Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get();
	}

}
