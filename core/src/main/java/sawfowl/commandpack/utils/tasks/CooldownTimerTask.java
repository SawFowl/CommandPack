package sawfowl.commandpack.utils.tasks;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.scheduler.ScheduledTask;

import sawfowl.commandpack.api.data.command.Settings;

public class CooldownTimerTask implements Consumer<ScheduledTask> {

	public CooldownTimerTask(ServerPlayer player, Settings settings, Map<UUID, Long> cooldowns){
		uuid = player.uniqueId();
		cooldown = settings.getCooldown();
		this.cooldowns = cooldowns;
	}

	private final UUID uuid;
	final long cooldown;
	final Map<UUID, Long> cooldowns;
	@Override
	public void accept(ScheduledTask task) {
		if(cooldowns.containsKey(uuid)) {
			if((cooldowns.get(uuid) + cooldown) - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) <= 0) cooldowns.remove(uuid);
		} else {
			task.cancel();
		}
	}

}
