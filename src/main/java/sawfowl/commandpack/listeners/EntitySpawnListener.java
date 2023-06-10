package sawfowl.commandpack.listeners;

import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.ConstructEntityEvent;

import sawfowl.commandpack.CommandPack;

public class EntitySpawnListener {

	private final CommandPack plugin;
	public EntitySpawnListener(CommandPack plugin) {
		this.plugin = plugin;
	}

	@Listener
	public void onSpawn(ConstructEntityEvent.Pre event) {
		if(!event.targetType().equals(EntityTypes.PLAYER.get()) && !plugin.getMainConfig().getRestrictEntitySpawn().isAllowSpawn(getId(event.targetType()), event.location().world().key().asString())) event.setCancelled(true);;
	}

	private String getId(EntityType<?> entity) {
		return EntityTypes.registry().valueKey(entity).asString();
	}

}
