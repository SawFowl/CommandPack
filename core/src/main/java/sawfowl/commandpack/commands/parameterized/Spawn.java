package sawfowl.commandpack.commands.parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.DefaultWorldKeys;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.configure.configs.miscellaneous.SpawnData;

@Register
public class Spawn extends AbstractParameterizedCommand {

	public Spawn(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		Optional<SpawnData> spawn = plugin.getMainConfig().getSpawnData();
		if(isPlayer) {
			ServerPlayer source = (ServerPlayer) src;
			ServerPlayer player = getPlayer(context).orElse(source);
			if(!source.uniqueId().equals(player.uniqueId())) {
				teleport(player, spawn);
				src.sendMessage(plugin.getLocales().getLocale(locale).getCommands().getSpawn().getTeleportStaff(player));
			} else delay(player, locale, consumer -> {
				teleport(player, spawn);
			});
		} else {
			Optional<ServerPlayer> player = getPlayer(context);
			if(player.isPresent()) {
				teleport(player.get(), spawn);
				src.sendMessage(plugin.getLocales().getLocale(locale).getCommands().getSpawn().getTeleportStaff(player.get()));
			} else exception(getExceptions(locale).getPlayerNotPresent());
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
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(Permissions.SPAWN_STAFF, true), false, locale -> getExceptions(locale).getPlayerNotPresent()));
	}

	@Override
	public String command() {
		return "spawn";
	}

	private void teleport(ServerPlayer player, Optional<SpawnData> location) {
		plugin.getPlayersData().getTempData().setPreviousLocation(player);
		if(location.isPresent() && location.get().getLocationData().getServerLocation().isPresent()) {
			player.setLocation(location.get().getLocationData().getServerLocation().get());
			location.get().getLocationData().getPosition().getRotation().ifPresent(rotation -> {
				player.setRotation(rotation.asVector3d());
			});
		} else tpDefault(player);
		player.sendMessage(plugin.getLocales().getLocale(player).getCommands().getSpawn().getTeleport());
	}

	private void tpDefault(ServerPlayer player) {
		player.setLocation(defaultWorld().location(defaultWorld().properties().spawnPosition()));
	}

	private ServerWorld defaultWorld() {
		return Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get();
	}

}
