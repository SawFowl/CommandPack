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
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.configs.miscellaneous.SpawnData;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

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
			if(source.uniqueId().equals(player.uniqueId())) {
				delay(player, locale, consumer -> {
					teleport(player, spawn, getSourceName(context.cause(), src, player.locale(), player));
				});
			} else teleport(player, spawn, getSourceName(context.cause(), src, player.locale(), player));
		} else {
			ServerPlayer player = getPlayer(context).orElse((ServerPlayer) src);
			teleport(player, spawn, getSourceName(context.cause(), src, player.locale(), player));
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
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(Permissions.SPAWN_STAFF, true), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT));
	}

	@Override
	public String command() {
		return "spawn";
	}

	private void teleport(ServerPlayer player, Optional<SpawnData> location, String name) {
		plugin.getPlayersData().getTempData().setPreviousLocation(player);
		if(location.isPresent() && location.get().getLocationData().getServerLocation().isPresent()) {
			player.setLocation(location.get().getLocationData().getServerLocation().get());
			location.get().getLocationData().getPosition().getRotation().ifPresent(rotation -> {
				player.setRotation(rotation.asVector3d());
			});
		} else tpDefault(player);
		if(player.name().equals(name)) {
			player.sendMessage(getText(player.locale(), LocalesPaths.COMMANDS_SPAWN_SUCCESS));
		} else player.sendMessage(TextUtils.replace(getText(player.locale(), LocalesPaths.COMMANDS_SPAWN_SUCCESS_OTHER), Placeholders.SOURCE, name));
	}

	private void tpDefault(ServerPlayer player) {
		player.setLocation(defaultWorld().location(defaultWorld().properties().spawnPosition()));
	}

	private ServerWorld defaultWorld() {
		return Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get();
	}

}
