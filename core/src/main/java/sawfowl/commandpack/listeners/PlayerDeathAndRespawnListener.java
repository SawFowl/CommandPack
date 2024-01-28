package sawfowl.commandpack.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.living.player.RespawnPlayerEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Ticks;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class PlayerDeathAndRespawnListener {

	private final CommandPack plugin;
	Map<UUID, Map<Integer, ItemStack>> inventories = new HashMap<UUID, Map<Integer, ItemStack>>();
	Map<UUID, Integer> exps = new HashMap<UUID, Integer>();
	public PlayerDeathAndRespawnListener(CommandPack plugin) {
		this.plugin = plugin;
	}

	@Listener(order = Order.LAST)
	public void onDeath(DestructEntityEvent.Death event) {
		if(!(event.entity() instanceof ServerPlayer)) return;
		ServerPlayer player = (ServerPlayer) event.entity();
		plugin.getPlayersData().getTempData().setPreviousLocation(player);
		if(event.keepInventory()) return;
		double keepInventory = Permissions.getKeepInventoryLimit(player);
		double keepExp = Permissions.getKeepInventoryLimit(player);
		if(player.inventory().totalQuantity() > 0) {
			if(keepInventory >= 100) {
				Map<Integer, ItemStack> map = new HashMap<Integer, ItemStack>();
				player.inventory().slots().forEach(slot -> {
					if(slot.totalQuantity() > 0 && slot.get(Keys.SLOT_INDEX).isPresent()) {
						map.put(slot.get(Keys.SLOT_INDEX).get(), slot.peek());
						slot.clear();
					}
				});
				if(!map.isEmpty()) {
					inventories.put(player.uniqueId(), map);
					player.sendMessage(plugin.getLocales().getText(player.locale(), LocalesPaths.KEEP_INVENTORY).replace(Placeholders.VALUE, String.valueOf(keepInventory)).get());
				}
			} else if(keepInventory > 0) {
				Map<Integer, ItemStack> map = new HashMap<Integer, ItemStack>();
				player.inventory().slots().forEach(slot -> {
					if(slot.totalQuantity() > 0 && slot.get(Keys.SLOT_INDEX).isPresent() && ThreadLocalRandom.current().nextDouble(0, 100) < keepInventory) {
						map.put(slot.get(Keys.SLOT_INDEX).get(), slot.peek());
						slot.clear();
					}
				});
				if(!map.isEmpty()) {
					inventories.put(player.uniqueId(), map);
					player.sendMessage(plugin.getLocales().getText(player.locale(), LocalesPaths.KEEP_INVENTORY).replace(Placeholders.VALUE, String.valueOf(keepInventory)).get());
				}
			}
		}
		if(!player.get(Keys.EXPERIENCE).isPresent() || player.get(Keys.EXPERIENCE).get() <= 0) return;
		if(keepExp >= 100) {
			exps.put(player.uniqueId(), player.get(Keys.EXPERIENCE).get());
			player.offer(Keys.EXPERIENCE, 0);
			player.sendMessage(plugin.getLocales().getText(player.locale(), LocalesPaths.KEEP_EXP).replace(Placeholders.VALUE, String.valueOf(keepExp)).get());
		} else if(keepExp > 0) {
			int keepSize = (int) ((player.get(Keys.EXPERIENCE).get() / 100) * keepExp);
			exps.put(player.uniqueId(), (int) ((player.get(Keys.EXPERIENCE).get() / 100) * keepExp));
			player.offer(Keys.EXPERIENCE, player.get(Keys.EXPERIENCE).get() - keepSize);
			player.sendMessage(plugin.getLocales().getText(player.locale(), LocalesPaths.KEEP_EXP).replace(Placeholders.VALUE, String.valueOf(keepExp)).get());
		}
	}

	@Listener
	public void onRespawn(RespawnPlayerEvent.Post event) {
		Sponge.server().scheduler().submit(Task.builder().delay(Ticks.of(1)).plugin(plugin.getPluginContainer()).execute(() -> {
			if(inventories.containsKey(event.entity().uniqueId()) && !inventories.get(event.entity().uniqueId()).isEmpty())  {
				Map<Integer, ItemStack> items = inventories.get(event.entity().uniqueId()); 
				for(Entry<Integer, ItemStack> entry : items.entrySet()) event.entity().inventory().offer(entry.getKey(), entry.getValue());
				inventories.remove(event.entity().uniqueId());
			}
			if(exps.containsKey(event.entity().uniqueId())) {
				event.entity().offer(Keys.EXPERIENCE, exps.get(event.entity().uniqueId()));
				exps.remove(event.entity().uniqueId());
			}
			if(plugin.getMainConfig().getSpawnData().isPresent() && plugin.getMainConfig().getSpawnData().get().isMoveAfterRespawn() && plugin.getMainConfig().getSpawnData().get().getLocationData().getServerLocation().isPresent()) {
				event.entity().setLocation(plugin.getMainConfig().getSpawnData().get().getLocationData().getServerLocation().get());
				plugin.getMainConfig().getSpawnData().get().getLocationData().getPosition().getRotation().ifPresent(rotation -> {
					event.entity().setRotation(rotation.asVector3d());
				});
			}
		}).build());
	}

}
