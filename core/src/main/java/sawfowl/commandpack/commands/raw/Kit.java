package sawfowl.commandpack.commands.raw;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.CauseStackManager.StackFrame;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.event.cause.entity.SpawnTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.service.permission.Subject;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.data.kits.GiveRule;
import sawfowl.commandpack.api.data.player.Backpack;
import sawfowl.commandpack.api.events.KitGiveEvent;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.configs.player.GivedKitData;
import sawfowl.commandpack.configure.locale.LocalesPaths;

import sawfowl.localeapi.api.TextUtils;

@Register
public class Kit extends AbstractRawCommand {

	public Kit(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		if(plugin.getKitService().getKits().isEmpty()) exception(locale, LocalesPaths.COMMANDS_KIT_NO_KITS);
		Optional<sawfowl.commandpack.api.data.kits.Kit> optKit = args.get(0);
		if(!optKit.isPresent()) {
			sendKitsList(cause, audience, locale, isPlayer);
			return;
		}
		sawfowl.commandpack.api.data.kits.Kit kit = optKit.get();
		if(isPlayer) {
			ServerPlayer src = (ServerPlayer) audience;
			if(kit.isNeedPerm() && !src.hasPermission(kit.permission()) && !src.hasPermission(Permissions.KIT_STAFF)) {
				src.sendMessage(getComponent(locale, LocalesPaths.COMMANDS_KIT_NO_PERM));
				return;
			}
			ServerPlayer target = args.getPlayer(1).orElse(src);
			sawfowl.commandpack.configure.configs.player.PlayerData data = (sawfowl.commandpack.configure.configs.player.PlayerData) plugin.getPlayersData().getOrCreatePlayerData(target);
			if(target.uniqueId().equals(src.uniqueId())) {
				delay(target, locale, consumer -> {
					prepare(cause, audience, locale, src, data, kit, true, Duration.ofMillis(System.currentTimeMillis()).getSeconds(), false);
				});
			} else prepare(cause, audience, locale, target, data, kit, false, Duration.ofMillis(System.currentTimeMillis()).getSeconds(), true);
		} else {
			ServerPlayer target = args.getPlayer(1).get();
			sawfowl.commandpack.configure.configs.player.PlayerData data = (sawfowl.commandpack.configure.configs.player.PlayerData) plugin.getPlayersData().getOrCreatePlayerData(target);
			prepare(cause, audience, locale, target, data, kit, false, Duration.ofMillis(System.currentTimeMillis()).getSeconds(), true);
		}
	}

	@Override
	public Component shortDescription(Locale locale) {
		return text("&3The command for giving the kit.");
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return text("&3The command for giving the kit.");
	}

	@Override
	public String permission() {
		return Permissions.KIT;
	}

	@Override
	public String command() {
		return "kit";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/kit <Kit> [Player]");
	}

	private void sendKitsList(CommandCause cause, Audience audience, Locale locale, boolean isPlayer) {
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) audience;
			List<Component> kits = new ArrayList<>();
			Component header = getComponent(locale, LocalesPaths.COMMANDS_KIT_LIST_HEADER);
			plugin.getKitService().getKits().forEach(kit -> {
				Component access = !kit.isNeedPerm() || (player.hasPermission(kit.permission()) || player.hasPermission(Permissions.KIT_STAFF)) ? text(" &7[&a+&7] ") : text(" &7[&c-&7] ");
				kits.add(TextUtils.createCallBack(getComponent(locale, LocalesPaths.COMMANDS_KIT_VIEW), c -> {
							kit.asMenu(getContainer(), player, true).open(player);
						}
					)
					.append(TextUtils.createCallBack(access, () -> {
								if(kit.isNeedPerm() && !player.hasPermission(kit.permission()) && !player.hasPermission(Permissions.KIT_STAFF)) {
									player.sendMessage(getComponent(locale, LocalesPaths.COMMANDS_KIT_NO_PERM));
									return;
								}
								sawfowl.commandpack.configure.configs.player.PlayerData data = (sawfowl.commandpack.configure.configs.player.PlayerData) plugin.getPlayersData().getOrCreatePlayerData(player);
								prepare(cause, audience, locale, player, data, kit, true, Duration.ofMillis(System.currentTimeMillis()).getSeconds(), false);
							}
						)
						.append(kit.getLocalizedName(locale))
					)
				);
			});
			sendPaginationList(player, header, Component.text("=").color(header.color()), 15, kits);
		} else {
			int size = plugin.getKitService().getKits().size();
			Component kits = getComponent(locale, LocalesPaths.COMMANDS_KIT_LIST_HEADER).append(text("&f: "));
			for(sawfowl.commandpack.api.data.kits.Kit kit : plugin.getKitService().getKits()) {
				kits = kits.append(text("&e" + kit.id()));
				if(size > 1) {
					kits = kits.append(text("&f, "));
				} else kits = kits.append(text("&f."));
				size--;
			}
			audience.sendMessage(kits);
		}
	}

	private void prepare(CommandCause cause, Audience audience, Locale locale, ServerPlayer player, sawfowl.commandpack.configure.configs.player.PlayerData data, sawfowl.commandpack.api.data.kits.Kit kit, boolean equals, long currentTime, boolean ignoreRules) {
		if(ignoreRules) {
			List<ItemStack> toGive = new ArrayList<>();
			List<ItemStack> toSpawn = new ArrayList<>();
			int emptySlots = player.inventory().primary().freeCapacity();
			for(ItemStack item : kit.getContent()) {
				if(emptySlots > 0) {
					toGive.add(item);
				} else toSpawn.add(item);
				emptySlots--;
			}
			player.inventory().primary().offer(toGive.toArray(new ItemStack[] {}));
			spawnItems(toSpawn, player);
			runCommands(player, kit);
			if(!equals) {
				player.sendMessage(getText(player.locale(), LocalesPaths.COMMANDS_KIT_SUCCESS).replace(Placeholders.VALUE, kit.getLocalizedName(player.locale())).get());
				audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_KIT_SUCCESS_STAFF).replace(new String[] {Placeholders.VALUE, Placeholders.PLAYER}, kit.getLocalizedName(locale), text(player.name())).get());
			} else player.sendMessage(getText(player.locale(), LocalesPaths.COMMANDS_KIT_SUCCESS).replace(Placeholders.VALUE, kit.getLocalizedName(player.locale())).get());
			return;
		}
		ItemStack[] items = kit.getContent().toArray(new ItemStack[] {});
		if(player.inventory().primary().freeCapacity() < items.length) {
			switch (kit.getGiveRule()) {
			case IGNORE_FULL_INVENTORY:
				giveKit(cause, audience, locale, player, data, kit, equals, currentTime, items, null, null, null);
				break;
			case MESSAGE_IF_INVENTORY_FULL:
				player.sendMessage(TextUtils.createCallBack(getComponent(locale, LocalesPaths.COMMANDS_KIT_INVENTORY_FULL), c -> {
					List<ItemStack> toGive = new ArrayList<>();
					List<ItemStack> toSpawn = new ArrayList<>();
					int emptySlots = player.inventory().primary().freeCapacity();
					for(ItemStack item : kit.getContent()) {
						if(emptySlots > 0) {
							toGive.add(item);
						} else toSpawn.add(item);
						emptySlots--;
					}
					giveKit(cause, audience, locale, player, data, kit, equals, currentTime, toGive.toArray(new ItemStack[] {}), null, null, toSpawn);
				}));
				return;
			case USE_BACKPACK:
				List<ItemStack> toGive = new ArrayList<>();
				List<ItemStack> toBackPack = new ArrayList<>();
				List<ItemStack> toSpawn = new ArrayList<>();
				int emptySlots = player.inventory().primary().freeCapacity();
				int backPackslots = 53 - data.getBackpack().size();
				for(ItemStack item : kit.getContent()) {
					if(emptySlots > 0) {
						toGive.add(item);
					} else if(backPackslots > 0) {
						toBackPack.add(item);
					} else toSpawn.add(item);
					emptySlots--;
				}
				giveKit(cause, audience, locale, player, data, kit, equals, currentTime, toGive.toArray(new ItemStack[] {}), null, toBackPack, toSpawn);
				break;
			case USE_ENDECHEST:
				List<ItemStack> toGive1 = new ArrayList<>();
				List<ItemStack> toEnderChest = new ArrayList<>();
				List<ItemStack> toSpawn1 = new ArrayList<>();
				int emptySlots1 = player.inventory().primary().freeCapacity();
				int emptySlotsEnderchest = 53 - player.enderChestInventory().freeCapacity();
				for(ItemStack item : kit.getContent()) {
					if(emptySlots1 > 0) {
						toGive1.add(item);
					} else if(emptySlotsEnderchest > 0) {
						toEnderChest.add(item);
					} else toSpawn1.add(item);
					emptySlots1--;
				}
				giveKit(cause, audience, locale, player, data, kit, equals, currentTime, toGive1.toArray(new ItemStack[] {}), toEnderChest.toArray(new ItemStack[] {}), null, toSpawn1);
				break;
			default:
				List<ItemStack> toGive2 = new ArrayList<>();
				List<ItemStack> toSpawn2 = new ArrayList<>();
				int emptySlots2 = player.inventory().primary().freeCapacity();
				for(ItemStack item : kit.getContent()) {
					if(emptySlots2 > 0) {
						toGive2.add(item);
					} else toSpawn2.add(item);
					emptySlots2--;
				}
				giveKit(cause, audience, locale, player, data, kit, equals, currentTime, toGive2.toArray(new ItemStack[] {}), null, null, toSpawn2);
				break;
			}
		} else giveKit(cause, audience, locale, player, data, kit, equals, currentTime, items, null, null, null);
	}

	private void giveKit(CommandCause cause, Audience audience, Locale locale, ServerPlayer player, sawfowl.commandpack.configure.configs.player.PlayerData data, sawfowl.commandpack.api.data.kits.Kit kit, boolean equals, long currentTime, ItemStack[] give, ItemStack[] enderchest, List<ItemStack> backpack, List<ItemStack> spawn) {
		boolean allowLimit = kit.isUnlimited() || !data.isGivedKit(kit) || data.getKitGivedData(kit).getGivedCount() < kit.getGiveLimit();
		boolean economyCancelGive = kit.getKitPrice().isPresent() && !plugin.getEconomy().checkPlayerBalance((audience instanceof ServerPlayer ? (ServerPlayer) audience : player).uniqueId(), kit.getKitPrice().get().getCurrency(), kit.getKitPrice().get().getMoney());
		KitGiveEvent.Pre eventPre = createPreEvent(cause, audience, kit, player, currentTime + kit.getCooldown(), data.getKitGivedTime(kit), (kit.isNeedPerm() && !player.hasPermission(kit.permission()) && !player.hasPermission(Permissions.KIT_STAFF)) || !allowLimit || economyCancelGive || data.getKitGivedTime(kit) + kit.getCooldown() > currentTime);
		Sponge.eventManager().post(eventPre);
		if(eventPre.isCancelled()) {
			if(data.getKitGivedTime(kit) + kit.getCooldown() > currentTime) {
				audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_KIT_WAIT).replace(Placeholders.VALUE, timeFormat((data.getKitGivedTime(kit) + kit.getCooldown()) - currentTime, locale)).get());
				Sponge.eventManager().post(createPostEvent(audience, kit, player, false, null, currentTime + kit.getCooldown()));
				return;
			}
			if(!allowLimit) {
				audience.sendMessage(getComponent(locale, LocalesPaths.COMMANDS_KIT_GIVE_LIMIT));
				Sponge.eventManager().post(createPostEvent(audience, kit, player, false, null, currentTime + kit.getCooldown()));
				return;
			}
			if(economyCancelGive) {
				audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_KIT_NO_MONEY).replace(Placeholders.VALUE, kit.getKitPrice().get().asComponent()).get());
				Sponge.eventManager().post(createPostEvent(audience, kit, player, false, null, currentTime + kit.getCooldown()));
				return;
			}
			return;
		}
		if(data.givedKits().containsKey(kit.id())) {
			GivedKitData kitData = data.givedKits().get(kit.id());
			kitData.setLastGivedTime(currentTime);
			kitData.setGivedCount(kitData.getGivedCount() + 1);
			data.givedKits().remove(kit.id());
			data.givedKits().put(kit.id(), kitData);
		} else data.givedKits().put(kit.id(), new GivedKitData(currentTime, 1));
		if(kit.getKitPrice().isPresent() && plugin.getEconomy().checkPlayerBalance((audience instanceof ServerPlayer ? (ServerPlayer) audience : player).uniqueId(), kit.getKitPrice().get().getCurrency(), kit.getKitPrice().get().getMoney())) {
			plugin.getEconomy().removeFromPlayerBalance(audience instanceof ServerPlayer ? (ServerPlayer) audience : player, kit.getKitPrice().get().getCurrency(), kit.getKitPrice().get().getMoney());
		}
		InventoryTransactionResult result = give == null ? InventoryTransactionResult.successNoTransactions() : player.inventory().primary().offer(give);
		if(enderchest != null) player.enderChestInventory().offer(enderchest);
		if(backpack != null && !backpack.isEmpty()) backpack.forEach(item -> {
			addToBackPack(data.getBackpack(), item);
		});
		if(spawn != null && !spawn.isEmpty()) spawnItems(spawn, player);
		runCommands(player, kit);
		data.save();
		if(!equals) {
			player.sendMessage(getText(player.locale(), LocalesPaths.COMMANDS_KIT_SUCCESS).replace(Placeholders.VALUE, kit.getLocalizedName(player.locale())).get());
			audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_KIT_SUCCESS_STAFF).replace(new String[] {Placeholders.VALUE, Placeholders.PLAYER}, kit.getLocalizedName(locale), text(player.name())).get());
		} else player.sendMessage(getText(player.locale(), LocalesPaths.COMMANDS_KIT_SUCCESS).replace(Placeholders.VALUE, kit.getLocalizedName(player.locale())).get());
		Sponge.eventManager().post(createPostEvent(audience, kit, player, true, result, currentTime + kit.getCooldown()));
	}

	private void runCommands(ServerPlayer player, sawfowl.commandpack.api.data.kits.Kit kit) {
		kit.getExecuteCommands().ifPresent(commands -> {
		if(!commands.isEmpty()) commands.forEach(command -> {
				try {
					try(StackFrame frame = Sponge.server().causeStackManager().pushCauseFrame()) {
						frame.addContext(EventContextKeys.SUBJECT, Sponge.systemSubject());
						frame.pushCause(Sponge.systemSubject());
						Sponge.server().commandManager().process(Sponge.systemSubject(), command.replace(Placeholders.PLAYER, player.name()).replace("%uuid%", player.uniqueId().toString()));
					}
				} catch (CommandException e) {
					Sponge.systemSubject().sendMessage(e.componentMessage());
				}
			});
		});
	}

	private void addToBackPack(Backpack backpack, ItemStack itemStack) {
		for(int i = 0; i <= 53; i++) {
			if(!backpack.getSlots().contains(i)) {
				backpack.addItem(i, itemStack);
				break;
			}
		}
	}

	private void spawnItems(List<ItemStack> items, ServerPlayer player) {
		if(items.isEmpty()) return;
		List<Item> itemsSpawn = new ArrayList<>();
		items.forEach(i -> {
			Item item = player.world().createEntity(EntityTypes.ITEM.get(), player.blockPosition());
			item.offer(Keys.ITEM_STACK_SNAPSHOT, i.createSnapshot());
			itemsSpawn.add(item);
		});
		try(StackFrame frame = Sponge.server().causeStackManager().pushCauseFrame()) {
			frame.addContext(EventContextKeys.SPAWN_TYPE, SpawnTypes.DROPPED_ITEM);
			player.world().spawnEntities(itemsSpawn);
		}
	}

	private KitGiveEvent.Pre createPreEvent(CommandCause commandCause, Audience source, sawfowl.commandpack.api.data.kits.Kit kit, ServerPlayer player, long nextGiveTime, long reviousGiveTime, boolean cancel) {
		Cause cause = Cause.of(EventContext.builder().add(EventContextKeys.PLUGIN, getContainer()).add(EventContextKeys.AUDIENCE, source).add(EventContextKeys.SUBJECT, source instanceof Subject ? (Subject) source : Sponge.systemSubject()).build(), source);
		return new KitGiveEvent.Pre() {

			boolean cancelled = !commandCause.hasPermission(Permissions.KIT_STAFF) && cancel;

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
		Cause cause = Cause.of(EventContext.builder().add(EventContextKeys.PLUGIN, getContainer()).add(EventContextKeys.AUDIENCE, source).add(EventContextKeys.SUBJECT, source instanceof Subject ? (Subject) source : Sponge.systemSubject()).build(), source);
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

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			RawArguments.createKitArgument(true, true, 0, null, null, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
			RawArguments.createPlayerArgument(true, false, 1, null, null, null, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT)
		);
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
