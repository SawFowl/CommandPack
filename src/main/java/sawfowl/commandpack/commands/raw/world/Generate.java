package sawfowl.commandpack.commands.raw.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.math.vector.Vector3i;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Generate extends AbstractWorldCommand {

	private List<String> actions = Arrays.asList("start", "pause", "stop");
	private Map<String, FillChunksTask> tasks = new HashMap<>();
	public Generate(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		ServerWorld world = getWorld(args, 0).get();
		String action = getString(args, 1).get();
		Optional<Integer> interval = getInteger(args, 2);
		Optional<Long> maxMemory = getLong(args, 3);
		Optional<Integer> chunks = getInteger(args, 4);
		if(action.equals("start")) {
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
				FillChunksTask fillTask = new FillChunksTask(world, interval.orElse(5), maxMemory.orElse((((long) (((double) Runtime.getRuntime().maxMemory()) * 0.8) / 1024) / 1024)), chunks.orElse(5));
				Sponge.asyncScheduler().submit(Task.builder().interval(interval.orElse(5), TimeUnit.SECONDS).plugin(getContainer()).execute(fillTask).build());
				tasks.put(world.key().asString(), fillTask);
			}
			audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_WORLD_START_GENERATE).replace(Placeholders.WORLD, world.key().asString()).get());
		} else if(action.equals("pause")) {
			if(!tasks.containsKey(world.key().asString())) exception(getText(locale, LocalesPaths.COMMANDS_WORLD_NOT_STARTED_GENERATE).replace(Placeholders.WORLD, world.key().asString()).get());
			tasks.get(world.key().asString()).stop().sendDebugMessage();
			audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_WORLD_PAUSE_GENERATE).replace(Placeholders.WORLD, world.key().asString()).get());
		} else if(action.equals("stop")) {
			if(!tasks.containsKey(world.key().asString())) exception(getText(locale, LocalesPaths.COMMANDS_WORLD_NOT_STARTED_GENERATE).replace(Placeholders.WORLD, world.key().asString()).get());
			if(!tasks.get(world.key().asString()).isCancelled()) exception(locale, LocalesPaths.COMMANDS_WORLD_NOT_PAUSED_GENERATE);
			tasks.remove(world.key().asString());
			audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_WORLD_STOP_GENERATE).replace(Placeholders.WORLD, world.key().asString()).get());
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
		return "generate";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/world generate <World> <Action> [Interval] [MaxMemory] [Chunks]").clickEvent(ClickEvent.suggestCommand("/world generate"));
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			RawArguments.createWorldArgument(false, false, 0, null, LocalesPaths.COMMANDS_EXCEPTION_WORLD_NOT_PRESENT),
			RawArguments.createStringArgument(actions, false, false, 1, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
			RawArguments.createIntegerArgument(new ArrayList<>(), true, true, 2, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
			RawArguments.createLongArgument(new ArrayList<>(), true, true, 3, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
			RawArguments.createIntegerArgument(new ArrayList<>(), true, true, 4, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

	private class FillChunksTask implements Consumer<ScheduledTask> {

		private ScheduledTask task;
		private final ServerWorld world;
		private int interval;
		private long maxMemory;
		private int chunks;
		private long lastSendMessage;
		private Vector3i min;
		private Vector3i max;
		private Vector3i current;
		private int generated = 0;
		private int totalChunksToGenerate;
		FillChunksTask(ServerWorld world, int interval, long maxMemory, int chunks) {
			this.world = world;
			this.interval = interval;
			this.maxMemory = maxMemory;
			this.chunks = chunks;
			int radius = (int) world.border().diameter() / 2;
			min = world.chunk(world.border().center().floorX() - radius, 0, world.border().center().floorY() - radius).chunkPosition();
			max = world.chunk(world.border().center().floorX() + radius, 0, world.border().center().floorY() + radius).chunkPosition();
			current = min;
			world.loadChunk(current, true);
			generated++;
			totalChunksToGenerate = calcTotalChunks();
		}

		@Override
		public void accept(ScheduledTask t) {
			if(task == null) task = t;
			int i = 0;
			while(Sponge.server().targetTicksPerSecond() > 10 && getUsedMemory() < maxMemory && i < chunks && max.z() - current.z() > 0) {
				Vector3i next = nextPos(current);
				if(next.z() < max.z()) {
					current = next;
					world.loadChunk(current, true);
				}
				generated++;
				i++;
			}
			if(getCurrentTime() - lastSendMessage >= 5) sendDebugMessage();
			if(generated >= totalChunksToGenerate) {
				if(getCurrentTime() != lastSendMessage) sendDebugMessage();
				tasks.remove(world.key().asString());
				stop();
			}
		}

		void start() {
			if(!task.isCancelled()) stop();
			task = task.scheduler().submit(Task.builder().from(task.task()).interval(interval, TimeUnit.SECONDS).build());
		}

		FillChunksTask stop() {
			task.cancel();
			return this;
		}

		boolean isCancelled() {
			return task.isCancelled();
		}

		long getCurrentTime() {
			return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
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
			plugin.getLogger().info(getMessage().replace(Placeholders.WORLD, world.key().asString()).replace(Placeholders.VALUE, getFilledPercent()).replace(Placeholders.LOCATION, current.toString()));
		}

		private Vector3i nextPos(Vector3i current) {
			Vector3i next = world.chunk(current.x() >= max.x() ? Vector3i.from(min.x(), 0, current.z() + 16) : Vector3i.from(current.x() + 16, 0, current.z())).chunkPosition();
			return next.x() >= max.x() ? world.chunk(min.x(), 0, next.z() + 16).chunkPosition() : next;
		}

		private long getUsedMemory() {
			return Runtime.getRuntime().totalMemory() / 1024 / 1024;
		}

		private String getMessage() {
			return getString(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WORLD_DEBUG_GENERATE);
		}

		private String getFilledPercent() {
			int value = (int) (((double) generated / totalChunksToGenerate) * 100);
			return String.valueOf(value > 100 ? 100 : value);
		}

		private int calcTotalChunks() {
			int totalX = 0;
			int totalZ = 0;
			for(int x = min.x() ; x < max.x() ; x = x + 16) {
				totalX++;
			}
			for(int z = min.z() ; z < max.z() ; z = z + 16) {
				totalZ++;
			}
			return totalX * totalZ;
		}
		
	}

}
