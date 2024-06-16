package sawfowl.commandpack.listeners;

import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.AttackEntityEvent;
import org.spongepowered.api.event.filter.cause.First;

import sawfowl.commandpack.CommandPackInstance;

public class EntityDamageListener {

	private final CommandPackInstance plugin;
	public EntityDamageListener(CommandPackInstance commandPack) {
		this.plugin = commandPack;
	}

	@Listener
	public void onDamage(AttackEntityEvent event, @First ServerPlayer player) {
		if(plugin.getMainConfig().getPreventDamage().isGodMode() && player.get(Keys.INVULNERABLE).isPresent() && !player.gameMode().get().equals(GameModes.CREATIVE.get()) && event.entity() instanceof ServerPlayer) event.setCancelled(true);;
		if(plugin.getMainConfig().getPreventDamage().isVanish() && player.get(Keys.VANISH_STATE).isPresent() && !player.get(Keys.VANISH_STATE).get().createsParticles() && event.entity() instanceof ServerPlayer) event.setCancelled(true);
	}

}
