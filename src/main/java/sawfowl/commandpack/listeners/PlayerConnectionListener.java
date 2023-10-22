package sawfowl.commandpack.listeners;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.effect.VanishState;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.util.Ticks;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.FMLConnectionData;
import net.minecraftforge.fml.network.NetworkHooks;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.kits.GiveRule;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.api.events.KitGiveEvent;
import sawfowl.commandpack.apiclasses.PlayersDataImpl;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.configs.player.GivedKitData;
import sawfowl.commandpack.configure.configs.player.PlayerData;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class PlayerConnectionListener {

	private final CommandPack plugin;
	public PlayerConnectionListener(CommandPack plugin) {
		this.plugin = plugin;
	}

	@Listener
	public void onConnect(ServerSideConnectionEvent.Join event) {
		if(plugin.getMainConfig().getAfkConfig().isEnable()) plugin.getPlayersData().getTempData().updateLastActivity(event.player());
		if(plugin.isForgeServer()) {
			List<String> mods = getModList(event.player());
			if(plugin.getMainConfig().isPrintPlayerMods() && mods != null) plugin.getLogger().info(plugin.getLocales().getString(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.PLAYER_MODS_LIST).replace(Placeholders.PLAYER, event.player().name()).replace(Placeholders.VALUE, String.join(", ", mods)));
			if(plugin.getMainConfig().getRestrictMods().isEnable() && !event.player().hasPermission(Permissions.ALL_MODS_ACCESS)) {
				Sponge.server().scheduler().submit(Task.builder().plugin(plugin.getPluginContainer()).delay(Ticks.of(10)).execute(() -> {
					List<String> banedPlayerMods = new ArrayList<>();
					if(mods != null) mods.forEach(mod -> {
						if(!plugin.getMainConfig().getRestrictMods().isAllowedPlayerMod(mod)) banedPlayerMods.add(mod);
					});
					if(!banedPlayerMods.isEmpty() && event.player().isOnline()) event.player().kick(TextUtils.replace(plugin.getLocales().getText(event.player().locale(), LocalesPaths.ILLEGAL_MODS_LIST), Placeholders.VALUE, String.join(", ", banedPlayerMods)));
				}).build());
			}
		}
		if(plugin.getMainConfig().getEconomy().isEnable()) plugin.getEconomy().getEconomyService().checkAccounts(event.player());
		if(!plugin.getPlayersData().getPlayerData(event.player().uniqueId()).isPresent()) ((PlayersDataImpl) plugin.getPlayersData()).addPlayerData(new PlayerData(event.player()).save());
		PlayerData playerData = ((PlayerData) plugin.getPlayersData().getPlayerData(event.player().uniqueId()).get());
		playerData.setLastJoin();
		if(playerData.isVanished()) event.player().offer(Keys.VANISH_STATE, VanishState.vanished());
		if(playerData.isGodMode()) event.player().offer(Keys.INVULNERABLE, true);
		if(playerData.isFly()) {
			event.player().offer(Keys.CAN_FLY, true);
			event.player().offer(Keys.IS_FLYING, true);
		}
		sendMotd(event.player());
		if(!event.isMessageCancelled() && plugin.getMainConfig().isChangeConnectionMessages()) {
			event.setMessageCancelled(true);
			if(!playerData.isVanished() || !event.player().hasPermission(Permissions.HIDE_CONNECT)) sendJoinMessage(event.player());
			Sponge.systemSubject().sendMessage(TextUtils.replaceToComponents(plugin.getLocales().getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.JOIN_MESSAGE), new String[] {Placeholders.PREFIX, Placeholders.PLAYER, Placeholders.SUFFIX}, new Component[] {getPrefix(event.player()), event.player().get(Keys.DISPLAY_NAME).orElse(text(event.player().name())), getSuffix(event.player())}));
		}
		if(plugin.getMainConfig().getSpawnData().isPresent() && plugin.getMainConfig().getSpawnData().get().isMoveAfterSpawn() && plugin.getMainConfig().getSpawnData().get().getLocationData().getServerLocation().isPresent()) {
			event.player().setLocation(plugin.getMainConfig().getSpawnData().get().getLocationData().getServerLocation().get());
			plugin.getMainConfig().getSpawnData().get().getLocationData().getPosition().getRotation().ifPresent(rotation -> {
				event.player().setRotation(rotation.asVector3d());
			});
		}
		Sponge.server().scheduler().submit(Task.builder().delay(Ticks.of(10)).plugin(plugin.getPluginContainer()).execute(() -> {
			runCommands(event.player());
		}).build());
		if(plugin.getKitService().getKits().isEmpty()) return;
		plugin.getKitService().getKits().stream().filter(kit -> kit.isFirstTime() || kit.isGiveOnJoin()).forEach(kit -> {
			giveKit(event.player(), kit);
		});
	}

	@Listener
	public void onDisconnect(ServerSideConnectionEvent.Disconnect event) {
		if(!plugin.getPlayersData().getPlayerData(event.player().uniqueId()).isPresent()) ((PlayersDataImpl) plugin.getPlayersData()).addPlayerData(new PlayerData(event.player()).save());
		PlayerData playerData = ((PlayerData) plugin.getPlayersData().getPlayerData(event.player().uniqueId()).get());
		playerData.setLastExit();
		playerData.setVanished(event.player().get(Keys.VANISH_STATE).isPresent() && event.player().get(Keys.VANISH_STATE).get().invisible());
		playerData.setGodMode(event.player().get(Keys.INVULNERABLE).orElse(false));
		playerData.setFly(event.player().get(Keys.CAN_FLY).orElse(false));
		if(plugin.getMainConfig().isChangeConnectionMessages()) {
			if(TextUtils.serializeLegacy(event.message()).length() > 0) event.setMessage(Component.empty());
			if(!playerData.isVanished() || !event.player().hasPermission(Permissions.HIDE_CONNECT)) sendLeaveMessage(event.player());
			Sponge.systemSubject().sendMessage(TextUtils.replaceToComponents(plugin.getLocales().getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.LEAVE_MESSAGE), new String[] {Placeholders.PREFIX, Placeholders.PLAYER, Placeholders.SUFFIX}, new Component[] {getPrefix(event.player()), event.player().get(Keys.DISPLAY_NAME).orElse(text(event.player().name())), getSuffix(event.player())}));
		}
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

	private void runCommands(ServerPlayer player) {
		if(player.hasPlayedBefore()) {
			if(plugin.getConfigManager().getJoinCommands().isEnableRegularly() && !plugin.getConfigManager().getJoinCommands().getRegularly().isEmpty()) {
				plugin.getConfigManager().getJoinCommands().getRegularly().forEach(c -> {
					if(c.startsWith("console:")) {
						Sponge.server().commandManager().complete(c.split("console:")[1].replace(Placeholders.PLAYER, player.name()).replace("%uuid%", player.uniqueId().toString()));
					} else if(c.startsWith("player:")) {
						try {
							plugin.getPlayersData().getPlayerData(player.uniqueId()).get().runCommand(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), c.split("player:")[1].replace(Placeholders.PLAYER, player.name()).replace("%uuid%", player.uniqueId().toString()));
						} catch (CommandException e) {
							Sponge.systemSubject().sendMessage(e.componentMessage());
						}
					}
				});
			}
		} else if(plugin.getConfigManager().getJoinCommands().isEnableFirstJoin() && !plugin.getConfigManager().getJoinCommands().getFirstJoin().isEmpty()) {
			plugin.getConfigManager().getJoinCommands().getFirstJoin().forEach(c -> {
				if(c.startsWith("console:")) {
					Sponge.server().commandManager().complete(c.split("console:")[1].replace(Placeholders.PLAYER, player.name()).replace("%uuid%", player.uniqueId().toString()));
				} else if(c.startsWith("player:")) {
					try {
						plugin.getPlayersData().getPlayerData(player.uniqueId()).get().runCommand(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), c.split("player:")[1].replace(Placeholders.PLAYER, player.name()).replace("%uuid%", player.uniqueId().toString()));
					} catch (CommandException e) {
						Sponge.systemSubject().sendMessage(e.componentMessage());
					}
				}
			});
		}
	}

	private void sendMotd(ServerPlayer player) {
		if(!plugin.getMainConfig().isEnableMotd()) return;
		Sponge.server().scheduler().submit(Task.builder().delay(2, TimeUnit.SECONDS).plugin(plugin.getPluginContainer()).execute(() -> {
			Sponge.server().player(player.uniqueId()).ifPresent(p -> {
				List<Component> motd = plugin.getLocales().getListTexts(p.locale(), LocalesPaths.MOTD);
				if(!motd.isEmpty()) p.sendMessage(Component.join(JoinConfiguration.newlines(), motd.stream().map(c -> TextUtils.replace(c, Placeholders.PLAYER, player.get(Keys.DISPLAY_NAME).orElse(Component.text(p.name())))).collect(Collectors.toList())));
			});
		}).build());
	}

	private void sendJoinMessage(ServerPlayer player) {
		Component prefix = getPrefix(player);
		Component name = player.get(Keys.DISPLAY_NAME).orElse(text(player.name()));
		Component suffix = getSuffix(player);
		boolean before = player.hasPlayedBefore();
		Sponge.server().scheduler().submit(Task.builder().delay(2, TimeUnit.SECONDS).plugin(plugin.getPluginContainer()).execute(() -> {
			Sponge.server().onlinePlayers().forEach(p -> {
				p.sendMessage(TextUtils.replaceToComponents(plugin.getLocales().getText(p.locale(), before ? LocalesPaths.JOIN_MESSAGE : LocalesPaths.FIRST_JOIN_MESSAGE), new String[] {Placeholders.PREFIX, Placeholders.PLAYER, Placeholders.SUFFIX}, new Component[] {prefix, name, suffix}));
			});
		}).build());
	}

	private void sendLeaveMessage(ServerPlayer player) {
		Component prefix = getPrefix(player);
		Component name = player.get(Keys.DISPLAY_NAME).orElse(text(player.name()));
		Component suffix = getSuffix(player);
		Sponge.server().onlinePlayers().forEach(p -> {
			p.sendMessage(TextUtils.replaceToComponents(plugin.getLocales().getText(p.locale(), LocalesPaths.LEAVE_MESSAGE), new String[] {Placeholders.PREFIX, Placeholders.PLAYER, Placeholders.SUFFIX}, new Component[] {prefix, name, suffix}));
		});
	}

	private Component getPrefix(ServerPlayer player) {
		return player.option(Placeholders.PREFIX).isPresent() ? text(player.option("prefix").get()) : Component.empty();
	}

	private Component getSuffix(ServerPlayer player) {
		return player.option(Placeholders.PREFIX).isPresent() ? text(player.option("suffix").get()) : Component.empty();
	}

	private Component text(String string) {
		if(isLegacyDecor(string)) {
			return TextUtils.deserializeLegacy(string);
		} else {
			return TextUtils.deserialize(string);
		}
	}

	private boolean isLegacyDecor(String string) {
		return string.indexOf('&') != -1 && !string.endsWith("&") && isStyleChar(string.charAt(string.indexOf("&") + 1));
	}

	private boolean isStyleChar(char ch) {
		return "0123456789abcdefklmnor".indexOf(ch) != -1;
	}

	private List<String> getModList(ServerPlayer player) {
		FMLConnectionData data = NetworkHooks.getConnectionData(((ServerPlayerEntity) player).connection.connection);
		return data == null ? null : NetworkHooks.getConnectionData(((ServerPlayerEntity) player).connection.connection).getModList();
	}

}
