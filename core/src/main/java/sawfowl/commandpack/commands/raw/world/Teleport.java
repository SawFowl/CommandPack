package sawfowl.commandpack.commands.raw.world;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.commands.raw.arguments.RawBasicArgumentData;
import sawfowl.commandpack.api.commands.raw.arguments.RawOptional;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;

public class Teleport extends AbstractWorldCommand {

	public Teleport(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		ServerWorld world = args.getWorld(0).get();
		Optional<ServerPlayer> player = args.getPlayer(1);
		ServerLocation location = findSafe(ServerLocation.of(world, world.properties().spawnPosition()));
		if(isPlayer) {
			if(player.isPresent()) {
				if(!player.get().uniqueId().equals(((ServerPlayer) audience).uniqueId())) {
					plugin.getPlayersData().getTempData().setPreviousLocation(player.get());
					player.get().setLocation(location);
					player.get().sendMessage(getWorld(player.get().locale()).getTeleport(location.worldKey().asString()));
					audience.sendMessage(getWorld(locale).getTeleportStaff(player.get(), location.worldKey().asString()));
				} else {
					delay(((ServerPlayer) audience), locale, consumer -> {
						plugin.getPlayersData().getTempData().setPreviousLocation(((ServerPlayer) audience));
						((ServerPlayer) audience).setLocation(location);
						audience.sendMessage(getWorld(locale).getTeleport(location.worldKey().asString()));
					});
				}
			} else {
				delay(((ServerPlayer) audience), locale, consumer -> {
					plugin.getPlayersData().getTempData().setPreviousLocation(((ServerPlayer) audience));
					((ServerPlayer) audience).setLocation(location);
					audience.sendMessage(getWorld(locale).getTeleport(location.worldKey().asString()));
				});
			}
		} else {
			ServerPlayer target = player.get();
			plugin.getPlayersData().getTempData().setPreviousLocation(target);
			target.setLocation(location);
			target.sendMessage(getWorld(target.locale()).getTeleport(location.worldKey().asString()));
			audience.sendMessage(getWorld(locale).getTeleportStaff(target, location.worldKey().asString()));
		}
	}

	@Override
	public Component shortDescription(Locale locale) {
		return null;
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return null;
	}

	@Override
	public String command() {
		return "teleport";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/world teleport <world> [Player]").clickEvent(ClickEvent.suggestCommand("/world teleport"));
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			createWorldArg(),
			RawArguments.createPlayerArgument(RawBasicArgumentData.createPlayer(1, null, null), RawOptional.player(), locale -> getExceptions(locale).getPlayerNotPresent())
		);
	}

	private boolean isSafe(ServerLocation location) {
		return location.block().type().equals(BlockTypes.AIR.get()) && ServerLocation.of(location.world(), location.x(), location.y() + 1, location.z()).block().type().equals(BlockTypes.AIR.get()) && !ServerLocation.of(location.world(), location.x(), location.y() - 1, location.z()).block().type().equals(BlockTypes.AIR.get());
	}

	private ServerLocation findSafe(ServerLocation location) {
		if(location.block().type().equals(BlockTypes.AIR.get())) {
			for(double y = location.y(); y > 5; y--) {
				ServerLocation find = ServerLocation.of(location.world(), location.x(), y, location.z());
				if(isSafe(find) && !find.block().type().equals(BlockTypes.BEDROCK.get())) return find;
			}
		} else {
			for(double y = location.y(); y < location.world().height(); y++) {
				ServerLocation find = ServerLocation.of(location.world(), location.x(), y, location.z());
				if(isSafe(find) && !find.block().type().equals(BlockTypes.BEDROCK.get())) return find;
			}
		}
		return location;
	}

	@Override
	public Settings getCommandSettings() {
		return Settings.builder().setAliases("tp").build();
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
