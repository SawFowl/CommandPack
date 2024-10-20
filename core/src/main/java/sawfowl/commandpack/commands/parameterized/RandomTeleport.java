package sawfowl.commandpack.commands.parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.math.vector.Vector3d;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.api.RandomTeleportService.RandomTeleportOptions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.events.RandomTeleportEvent;

@Register
public class RandomTeleport extends AbstractParameterizedCommand {

	public RandomTeleport(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			ServerPlayer player = getPlayer(context).orElse((ServerPlayer) src);
			ServerWorld targetWorld = getArgument(context, ServerWorld.class, "World").orElse(plugin.getMainConfig().getRtpConfig().getTargetWorld(player.world()));
			RandomTeleportOptions options = plugin.getRTPService().getOptions(targetWorld);
			src.sendMessage(getRandomTeleport(locale).getWait());
			CompletableFuture<Optional<ServerLocation>> futurePos = plugin.getRTPService().getLocationFuture(player.serverLocation(), options);
			futurePos.thenAccept(optional -> {
				if(!optional.isPresent()) {
					if(!player.uniqueId().equals(((ServerPlayer) src).uniqueId()) || context.cause().hasPermission(Permissions.RTP_STAFF)) {
						src.sendMessage(getRandomTeleport(locale).getPositionSearchErrorStaff(targetWorld, options.getAttempts()));
					} else {
						src.sendMessage(getRandomTeleport(player).getPositionSearchError());
					}
				} else {
					RandomTeleportEvent event = createEvent(context.contextCause().with(player), player, targetWorld, optional.get().position(), plugin.getRTPService().getOptions(player.world()));
					Sponge.eventManager().post(event);
					if(event.isCancelled()) {
						src.sendMessage(getRandomTeleport(locale).getCancelled());
						return;
					}
					if(context.cause().hasPermission(Permissions.RTP_STAFF)) {
						teleport(player, optional.get());
						if(player.uniqueId().equals(((ServerPlayer) src).uniqueId())) {
							src.sendMessage(getRandomTeleport(locale).getSuccess(optional.get()));
						} else {
							src.sendMessage(getRandomTeleport(locale).getSuccessStaff(player, optional.get()));
						}
					} else {
						try {
							delay(player, locale, consumer -> {
								teleport(player, optional.get());
							});
						} catch (CommandException e) {
							player.sendMessage(e.componentMessage());
						}
					}
				}
			});
		} else {
			ServerPlayer player = getPlayer(context).get();
			ServerWorld targetWorld = getArgument(context, ServerWorld.class, "World").orElse(plugin.getMainConfig().getRtpConfig().getTargetWorld(player.world()));
			RandomTeleportOptions options = plugin.getRTPService().getOptions(targetWorld);
			if(isCommandBlock(context.cause()) || isCommandBlockMinecart(context.cause())) {
				player.sendMessage(getRandomTeleport(player).getWait());
			} else src.sendMessage(getRandomTeleport(locale).getWait());
			CompletableFuture<Optional<ServerLocation>> futurePos = plugin.getRTPService().getLocationFuture(player.serverLocation(), options);
			futurePos.thenAccept(optional -> {
				if(!optional.isPresent()) {
					src.sendMessage(getRandomTeleport(locale).getPositionSearchErrorStaff(targetWorld, options.getAttempts()));
					if(isCommandBlock(context.cause()) || isCommandBlockMinecart(context.cause())) player.sendMessage(getRandomTeleport(player).getPositionSearchError());
				} else {
					RandomTeleportEvent event = createEvent(context.contextCause(), player, targetWorld, optional.get().position(), plugin.getRTPService().getOptions(player.world()));
					Sponge.eventManager().post(event);
					if(event.isCancelled()) {
						(isCommandBlock(context.cause()) || isCommandBlockMinecart(context.cause()) ? player : src).sendMessage(getRandomTeleport(locale).getCancelled());
						return;
					}
					if(isCommandBlock(context.cause()) || isCommandBlockMinecart(context.cause())) {
						try {
							delay(player, locale, consumer -> {
								teleport(player, optional.get());
								player.sendMessage(getRandomTeleport(player).getSuccess(optional.get()));
							});
						} catch (CommandException e) {
							player.sendMessage(e.componentMessage());
						}
					} else {
						Sponge.server().scheduler().executor(getContainer()).execute(() -> {
							teleport(player, optional.get());
							src.sendMessage(getRandomTeleport(locale).getSuccessStaff(player, optional.get()));
						});
					}
				}
			});
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(
			ParameterSettings.of(CommandParameters.createPlayer(Permissions.RTP_STAFF, true), false, locale -> getExceptions(locale).getPlayerNotPresent()),
			ParameterSettings.of(CommandParameters.createWorld(Permissions.RTP_STAFF, true), true, locale -> getExceptions(locale).getWorldNotPresent())
		);
	}

	@Override
	public String command() {
		return "randomteleport";
	}

	@Override
	public String permission() {
		return Permissions.RTP;
	}

	private RandomTeleportEvent createEvent(Cause cause, ServerPlayer player, ServerWorld dWorld, Vector3d destinationPos, RandomTeleportOptions options) {
		return new RandomTeleportEvent() {

			private Vector3d originalDestinationPosition = destinationPos;
			private Vector3d destinationPosition = destinationPos;
			private boolean cancelled = false;
			private ServerWorld destinationWorld = dWorld;
			private ServerWorld originalDestinationWorld = dWorld;

			@Override
			public ServerPlayer player() {
				return player;
			}

			@Override
			public Entity entity() {
				return player;
			}

			@Override
			public Vector3d originalPosition() {
				return destinationPos;
			}

			@Override
			public Vector3d originalDestinationPosition() {
				return originalDestinationPosition;
			}

			@Override
			public Vector3d destinationPosition() {
				return destinationPosition;
			}

			@Override
			public void setDestinationPosition(Vector3d position) {
				destinationPosition = position;
			}

			@Override
			public Cause cause() {
				return cause;
			}

			@Override
			public boolean isCancelled() {
				return cancelled;
			}

			@Override
			public void setCancelled(boolean cancel) {
				cancelled = cancel;
			}

			@Override
			public RandomTeleportOptions getOptions() {
				return options;
			}

			@Override
			public ServerWorld originalWorld() {
				return player.world();
			}

			@Override
			public ServerWorld originalDestinationWorld() {
				return originalDestinationWorld;
			}

			@Override
			public ServerWorld destinationWorld() {
				return destinationWorld;
			}

		};
	}

	private void teleport(ServerPlayer player, ServerLocation location) {
		plugin.getPlayersData().getTempData().setPreviousLocation(player);
		Sponge.server().scheduler().executor(getContainer()).execute(() -> {
			player.setLocation(location);
		});
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.RandomTeleport getRandomTeleport(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getRandomTeleport();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.RandomTeleport getRandomTeleport(ServerPlayer player) {
		return getRandomTeleport(player.locale());
	}

}