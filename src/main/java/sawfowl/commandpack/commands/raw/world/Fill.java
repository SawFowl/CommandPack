package sawfowl.commandpack.commands.raw.world;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.lang3.math.NumberUtils;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawArgument;
import sawfowl.commandpack.api.commands.raw.RawSupplier;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Fill extends AbstractWorldCommand {

	private List<String> actions = Arrays.asList("start", "stop");
	private Map<String, FillChunksTask> tasks = new HashMap<>();
	public Fill(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		ServerWorld world = getWorld(args, 0).get();
		boolean start = getString(args, 1).map(arg -> arg.equals("start")).isPresent();
		boolean stop = getString(args, 1).map(arg -> arg.equals("stop")).isPresent();
		Optional<Integer> interval = getInteger(args, 2);
		Optional<Long> maxMemory = getLong(args, 3);
		Optional<Integer> chunks = getInteger(args, 4);
		if(start) {
			if(tasks.containsKey(world.key().asString())) {
				FillChunksTask fillTask = tasks.get(world.key().asString());
				fillTask.stop();
				if(interval.isPresent()) fillTask.setInterval(interval.get());
				if(maxMemory.isPresent()) {
					long memoryLimit = Runtime.getRuntime().totalMemory() - (Runtime.getRuntime().totalMemory() / 10);
					if(memoryLimit > maxMemory.get()) fillTask.setMemory(maxMemory.get());
				}
				if(chunks.isPresent()) fillTask.setChunks(chunks.get());
				fillTask.start();
				fillTask.sendDebugMessage();
			} else {
				FillChunksTask fillTask = new FillChunksTask(world, interval.orElse(5), maxMemory.orElse(Runtime.getRuntime().totalMemory() - ((Runtime.getRuntime().totalMemory() / 100) * 20)), chunks.orElse(5));
				Sponge.asyncScheduler().submit(Task.builder().interval(interval.orElse(5), TimeUnit.SECONDS).plugin(getContainer()).execute(fillTask).build());
				tasks.put(world.key().asString(), fillTask);
			}
			audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_START_FILL), Placeholders.WORLD, world.key().asString()));
		} else if(stop) {
			tasks.get(world.key().asString()).stop().sendDebugMessage();
			audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_STOP_FILL), Placeholders.WORLD, world.key().asString()));
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
		return "fill";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/world fill <World> <Action> [Interval] [MaxMemory] [Chunks]");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
				RawArgument.of(ServerWorld.class, new Supplier<Stream<String>>() {
					@Override
					public Stream<String> get() {
						return Sponge.server().worldManager().worlds().stream().map(ServerWorld::key).map(ResourceKey::asString);
					}
				}, new RawSupplier<ServerWorld>() {
					@Override
					public Optional<ServerWorld> get(String[] args) {
						return args.length >= 1 ? Sponge.server().worldManager().world(ResourceKey.resolve(args[0])) : Optional.empty();
					}
				}, false, false, 0, LocalesPaths.COMMANDS_EXCEPTION_WORLD_NOT_PRESENT),
				RawArgument.of(String.class, new Supplier<Stream<String>>() {
					@Override
					public Stream<String> get() {
						return actions.stream();
					}
				}, new RawSupplier<String>() {

					@Override
					public Optional<String> get(String[] args) {
						return args.length >= 2 && tasks.containsKey(args[0]) ? actions.stream().filter(a -> a.equals(args[1])).findFirst() : Optional.empty();
					}
				}, false, false, 1, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
				RawArgument.of(Integer.class, null, new RawSupplier<Integer>() {
					@Override
					public Optional<Integer> get(String[] args) {
						return Optional.of(args.length >= 3 && NumberUtils.isParsable(args[2]) ? NumberUtils.createInteger(args[2]) : null);
					}
				}, true, true, 2, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
				RawArgument.of(Long.class, null, new RawSupplier<Long>() {
					@Override
					public Optional<Long> get(String[] args) {
						return Optional.of(args.length >= 4 && NumberUtils.isParsable(args[3]) ? NumberUtils.createLong(args[3]) : null);
					}
				}, true, true, 3, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
				RawArgument.of(Integer.class, null, new RawSupplier<Integer>() {
					@Override
					public Optional<Integer> get(String[] args) {
						return Optional.of(args.length >= 5 && NumberUtils.isParsable(args[4]) ? NumberUtils.createInteger(args[4]) : null);
					}
				}, true, true, 4, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT));
	}

	private class FillChunksTask implements Consumer<ScheduledTask> {

		private ScheduledTask task;
		private final ServerWorld world;
		private int interval;
		private long maxMemory;
		private int chunks;
		private long lastSendMessage;
		private int x;
		private int z;
		private int maxX;
		private int maxZ;
		private int lastMinZ;
		FillChunksTask(ServerWorld world, int interval, long maxMemory, int chunks) {
			this.world = world;
			this.interval = interval;
			this.maxMemory = maxMemory;
			this.chunks = chunks;
			x = world.chunk(world.min()).chunkPosition().x();
			z = world.chunk(world.min()).chunkPosition().z();
			lastMinZ = z;
			maxX = world.chunk(world.max()).chunkPosition().x();
			maxZ = world.chunk(world.max()).chunkPosition().z();
		}

		@Override
		public void accept(ScheduledTask t) {
			if(task == null) task = t;
			while(x < maxX && getUsedMemory() > maxMemory) {
				if(getCurrentTime() - lastSendMessage >= 30) sendDebugMessage();
				if(lastMinZ + chunks < z) z = lastMinZ + chunks;
				int i = 0;
				while(i < chunks) {
					world.loadChunk(x, 0, z + i, true);
					i++;
				}
				if(i >= chunks) x++;
			}
			if(x >= maxX) {
				lastMinZ = z;
				z = z + chunks;
			}
		}

		public void start() {
			if(!task.isCancelled()) stop();
			task.scheduler().submit(Task.builder().from(task.task()).interval(interval, TimeUnit.SECONDS).build());
		}

		public FillChunksTask stop() {
			task.cancel();
			return this;
		}

		private long getCurrentTime() {
			return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
		}

		private long getUsedMemory() {
			return Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory();
		}

		void setInterval(int interval) {
			this.interval = interval;
			start();
		}

		void setMemory(long maxMemory) {
			this.maxMemory = maxMemory;
		}

		void setChunks(int chunks) {
			this.chunks = chunks;
		}

		void sendDebugMessage() {
			lastSendMessage = getCurrentTime();
			plugin.getLogger().info(getMessage().replace(Placeholders.WORLD, world.key().asString()).replace(Placeholders.VALUE, getFilledPercent()).replace(Placeholders.LOCATION, "(" + x + ", " + (z + chunks) + ")"));
		}

		private String getMessage() {
			return getString(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WORLD_DEBUG_FILL);
		}

		private String getFilledPercent() {
			return String.valueOf(100 * (((Math.abs(x) + Math.abs(z)) / ( Math.abs(maxX) + Math.abs(maxZ))));
		}
		
	}

}
