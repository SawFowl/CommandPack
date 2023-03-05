package sawfowl.commandpack.listeners;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.living.player.RespawnPlayerEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Ticks;

import sawfowl.commandpack.CommandPack;

public class PlayerRespawnListener {

	private final CommandPack plugin;
	public PlayerRespawnListener(CommandPack plugin) {
		this.plugin = plugin;
	}

	@Listener
	public void onRespawn(RespawnPlayerEvent.Post event) {
		Sponge.server().scheduler().submit(Task.builder().delay(Ticks.of(1)).plugin(plugin.getPluginContainer()).execute(() -> {
			if(plugin.getMainConfig().getSpawnData().isPresent() && plugin.getMainConfig().getSpawnData().get().isMoveAfterRespawn() && plugin.getMainConfig().getSpawnData().get().getLocationData().getServerLocation().isPresent()) {
				event.entity().setLocation(plugin.getMainConfig().getSpawnData().get().getLocationData().getServerLocation().get());
				plugin.getMainConfig().getSpawnData().get().getLocationData().getPosition().getRotation().ifPresent(rotation -> {
					event.entity().setRotation(rotation.rotation());
				});
			}
		}).build());
	}

}
