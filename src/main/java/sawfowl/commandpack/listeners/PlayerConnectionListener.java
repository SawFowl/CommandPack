package sawfowl.commandpack.listeners;

import java.time.Duration;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.service.permission.Subject;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.data.kits.GiveRule;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.api.events.KitGiveEvent;
import sawfowl.commandpack.apiclasses.PlayersDataImpl;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.configs.player.GivedKitData;
import sawfowl.commandpack.configure.configs.player.PlayerData;

public class PlayerConnectionListener {

	private final CommandPack plugin;
	public PlayerConnectionListener(CommandPack plugin) {
		this.plugin = plugin;
	}

	@Listener
	public void onConnect(ServerSideConnectionEvent.Join event) {
		if(!plugin.getPlayersData().getPlayerData(event.player().uniqueId()).isPresent()) ((PlayersDataImpl) plugin.getPlayersData()).addPlayerData(new PlayerData(event.player()).save());
		((PlayerData) plugin.getPlayersData().getPlayerData(event.player().uniqueId()).get()).setLastJoin();
		if(plugin.getMainConfig().getSpawnData().isPresent() && plugin.getMainConfig().getSpawnData().get().isMoveAfterSpawn() && plugin.getMainConfig().getSpawnData().get().getLocationData().getServerLocation().isPresent()) {
			event.player().setLocation(plugin.getMainConfig().getSpawnData().get().getLocationData().getServerLocation().get());
			plugin.getMainConfig().getSpawnData().get().getLocationData().getPosition().getRotation().ifPresent(rotation -> {
				event.player().setRotation(rotation.asVector3d());
			});
		}
		plugin.getPlayersData().getTempData().updateLastActivity(event.player());
		if(plugin.getKitService().getKits().isEmpty()) return;
		plugin.getKitService().getKits().stream().filter(kit -> kit.isFirstTime() || kit.isGiveOnJoin()).forEach(kit -> {
			giveKit(event.player(), kit);
		});
	}

	@Listener
	public void onDisconnect(ServerSideConnectionEvent.Disconnect event) {
		if(!plugin.getPlayersData().getPlayerData(event.player().uniqueId()).isPresent()) ((PlayersDataImpl) plugin.getPlayersData()).addPlayerData(new PlayerData(event.player()).save());
		((PlayerData) plugin.getPlayersData().getPlayerData(event.player().uniqueId()).get()).setLastExit();
	}

	private void giveKit(ServerPlayer player, Kit kit) {
		PlayerData data = (PlayerData) plugin.getPlayersData().getPlayerData(player.uniqueId()).get();
		if(player.inventory().primary().freeCapacity() < kit.getContent().size()) return;
		long currentTime = Duration.ofMillis(System.currentTimeMillis()).getSeconds();
		if(kit.isFirstTime()) {
			boolean cancel = data.isGivedKit(kit);
			KitGiveEvent.Pre eventPre = createPreEvent(Sponge.server(), kit, player, currentTime + kit.getCooldown(), cancel ? data.getKitGivedTime(kit) : currentTime, cancel);
			Sponge.eventManager().post(eventPre);
			if(eventPre.isCancelled()) return;
			InventoryTransactionResult result = null;
			if(!kit.getContent().isEmpty()) result = player.inventory().offer(kit.getContent().toArray(new ItemStack[] {}));
			runCommands(player, kit);
			Sponge.eventManager().post(createPostEvent(Sponge.server(), kit, player, true, result, currentTime + kit.getCooldown()));
			if(data.givedKits().containsKey(kit.id())) {
				GivedKitData kitData = data.givedKits().get(kit.id());
				kitData.setLastGivedTime(currentTime);
				kitData.setGivedCount(kitData.getGivedCount() + 1);
				data.givedKits().remove(kit.id());
				data.givedKits().put(kit.id(), kitData);
			} else data.givedKits().put(kit.id(), new GivedKitData(currentTime, 1));
			data.save();
			return;
		}
		if(kit.isGiveOnJoin()) {
			boolean cancel = data.isGivedKit(kit) && data.getKitGivedTime(kit) + kit.getCooldown() > currentTime;
			KitGiveEvent.Pre eventPre = createPreEvent(Sponge.server(), kit, player, currentTime + kit.getCooldown(), cancel ? data.getKitGivedTime(kit) : currentTime, cancel);
			Sponge.eventManager().post(eventPre);
			if(eventPre.isCancelled()) return;
			InventoryTransactionResult result = null;
			if(!kit.getContent().isEmpty()) result = player.inventory().offer(kit.getContent().toArray(new ItemStack[] {}));
			runCommands(player, kit);
			Sponge.eventManager().post(createPostEvent(Sponge.server(), kit, player, true, result, currentTime + kit.getCooldown()));
			if(data.givedKits().containsKey(kit.id())) {
				GivedKitData kitData = data.givedKits().get(kit.id());
				kitData.setLastGivedTime(currentTime);
				kitData.setGivedCount(kitData.getGivedCount() + 1);
				data.givedKits().remove(kit.id());
				data.givedKits().put(kit.id(), kitData);
			} else data.givedKits().put(kit.id(), new GivedKitData(currentTime, 1));
			data.save();
			return;
		}
	}

	private void runCommands(ServerPlayer player, sawfowl.commandpack.api.data.kits.Kit kit) {
		kit.getExecuteCommands().ifPresent(commands -> {
		if(!commands.isEmpty()) commands.forEach(command -> {
				try {
					Sponge.server().commandManager().process(Sponge.systemSubject(), player, command.replace(Placeholders.PLAYER, player.name()).replace("%uuid%", player.uniqueId().toString()));
				} catch (CommandException e) {
					Sponge.systemSubject().sendMessage(e.componentMessage());
				}
			});
		});
	}

	private KitGiveEvent.Pre createPreEvent(Audience source, sawfowl.commandpack.api.data.kits.Kit kit, ServerPlayer player, long nextGiveTime, long reviousGiveTime, boolean cancel) {
		Cause cause = Cause.of(EventContext.builder().add(EventContextKeys.PLUGIN, plugin.getPluginContainer()).add(EventContextKeys.AUDIENCE, source).add(EventContextKeys.SUBJECT, source instanceof Subject ? (Subject) source : Sponge.systemSubject()).build(), source);
		return new KitGiveEvent.Pre() {

			boolean cancelled = cancel;

			@Override
			public Cause cause() {
				return cause;
			}

			@Override
			public sawfowl.commandpack.api.data.kits.Kit kit() {
				return kit;
			}

			@Override
			public ServerPlayer getPlayer() {
				return player;
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
			public long getCurrentTime() {
				return Duration.ofMillis(System.currentTimeMillis()).getSeconds();
			}

			@Override
			public long getPreviousGiveTime() {
				return reviousGiveTime;
			}

			@Override
			public long getNextAllowedAccess() {
				return nextGiveTime;
			}

			@Override
			public GiveRule getGiveRule() {
				return kit.getGiveRule();
			}
		};
	}

	private KitGiveEvent.Post createPostEvent(Audience source, sawfowl.commandpack.api.data.kits.Kit kit, ServerPlayer player, boolean isGived, InventoryTransactionResult result, long nextGiveTime) {
		Cause cause = Cause.of(EventContext.builder().add(EventContextKeys.PLUGIN, plugin.getPluginContainer()).add(EventContextKeys.AUDIENCE, source).add(EventContextKeys.SUBJECT, source instanceof Subject ? (Subject) source : Sponge.systemSubject()).build(), source);
		return new KitGiveEvent.Post() {
			
			@Override
			public Cause cause() {
				return cause;
			}
			
			@Override
			public sawfowl.commandpack.api.data.kits.Kit kit() {
				return kit;
			}
			
			@Override
			public ServerPlayer getPlayer() {
				return player;
			}
			
			@Override
			public boolean isGived() {
				return isGived;
			}
			
			@Override
			public InventoryTransactionResult getResult() {
				return result;
			}

			@Override
			public long getNextAllowedAccess() {
				return nextGiveTime;
			}
		};
	}

}
