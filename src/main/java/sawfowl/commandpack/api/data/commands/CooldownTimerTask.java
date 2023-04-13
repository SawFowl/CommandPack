package sawfowl.commandpack.api.data.commands;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.scheduler.ScheduledTask;

public class CooldownTimerTask implements Consumer<ScheduledTask> {

	public CooldownTimerTask(ServerPlayer player, CommandSettings commandSettings, Map<UUID, Long> cooldowns){
		uuid = player.uniqueId();
		cooldown = commandSettings.getCooldown();
		this.cooldowns = cooldowns;
	}

	private final UUID uuid;
	private final long currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
	final long cooldown;
	final Map<UUID, Long> cooldowns;
	@Override
	public void accept(ScheduledTask task) {
		if(cooldowns.containsKey(uuid)) {
			if((cooldowns.get(uuid) + cooldown) - currentTime <= 0) cooldowns.remove(uuid);
		} else {
			task.cancel();
		}
	}

}