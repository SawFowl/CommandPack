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

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Teleport extends AbstractWorldCommand {

	public Teleport(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		ServerWorld world = getWorld(args, 0).get();
		Optional<ServerPlayer> player = getPlayer(args, 1);
		ServerLocation location = findSafe(ServerLocation.of(world, world.properties().spawnPosition()));
		if(isPlayer) {
			if(player.isPresent()) {
				if(!player.get().uniqueId().equals(((ServerPlayer) audience).uniqueId())) {
					plugin.getPlayersData().getTempData().setPreviousLocation(player.get());
					player.get().setLocation(location);
					player.get().sendMessage(getText(player.get().locale(), LocalesPaths.COMMANDS_WORLD_TELEPORT).replace(Placeholders.WORLD, world.key().asString()).get());
					audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_WORLD_TELEPORT_OTHER).replace(new String[] {Placeholders.PLAYER, Placeholders.WORLD}, player.get().name(), world.key().asString()).get());
				} else {
					delay(((ServerPlayer) audience), locale, consumer -> {
						plugin.getPlayersData().getTempData().setPreviousLocation(((ServerPlayer) audience));
						((ServerPlayer) audience).setLocation(location);
						audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_WORLD_TELEPORT).replace(Placeholders.WORLD, world.key().asString()).get());
					});
				}
			} else {
				delay(((ServerPlayer) audience), locale, consumer -> {
					plugin.getPlayersData().getTempData().setPreviousLocation(((ServerPlayer) audience));
					((ServerPlayer) audience).setLocation(location);
					audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_WORLD_TELEPORT).replace(Placeholders.WORLD, world.key().asString()).get());
				});
			}
		} else {
			ServerPlayer target = player.get();
			plugin.getPlayersData().getTempData().setPreviousLocation(target);
			target.setLocation(location);
			target.sendMessage(getText(target.locale(), LocalesPaths.COMMANDS_WORLD_TELEPORT).replace(Placeholders.WORLD, world.key().asString()).get());
			audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_WORLD_TELEPORT_OTHER).replace(new String[] {Placeholders.PLAYER, Placeholders.WORLD}, new Object[] {target.name(), world.key().asString()}).get());
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
		return "tp";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/world teleport <world> [Player]").clickEvent(ClickEvent.suggestCommand("/world teleport"));
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			createWorldArg(),
			RawArguments.createPlayerArgument(true, false, 1, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT)
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

}
