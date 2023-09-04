package sawfowl.commandpack.configure.configs.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.adventure.SpongeComponents;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.manager.CommandMapping;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.event.CauseStackManager.StackFrame;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.math.vector.Vector3d;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.data.player.Home;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.api.data.player.Backpack;
import sawfowl.commandpack.api.data.player.GivedKit;
import sawfowl.commandpack.api.data.player.Warp;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.Locales;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class PlayerData implements sawfowl.commandpack.api.data.player.PlayerData {

	public PlayerData() {
		backpackData.setSaveConsumer(consumer -> {
			save();
		});
	}
	public PlayerData(ServerPlayer player) {
		this.name = player.name();
		this.uuid = player.uniqueId();
		backpackData.setSaveConsumer(consumer -> {
			save();
		});
	}

	@Setting("Name")
	private String name;
	@Setting("UUID")
	private UUID uuid;
	@Setting("Homes")
	private List<HomeData> homes = new ArrayList<>();
	@Setting("Warps")
	private List<WarpData> warps = new ArrayList<>();
	@Setting("Backpack")
	private BackpackData backpackData = new BackpackData();
	@Setting("GivedKits")
	private Map<String, GivedKitData> givedKits = new HashMap<>();
	@Setting("LastJoin")
	private long lastJoin;
	@Setting("LastExit")
	private long lastExit;
	@Setting("Vanished")
	private boolean vanished = false;
	@Setting("GodMode")
	private boolean godMode = false;
	@Setting("Fly")
	private boolean fly = false;
	@Setting("HideBalance")
	private boolean hideBalance = false;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public UUID getUniqueId() {
		return uuid;
	}

	@Override
	public Optional<ServerPlayer> getPlayer() {
		return Sponge.server().player(uuid);
	}

	@Override
	public Optional<User> getUser() {
		try {
			return Sponge.server().userManager().load(uuid).get();
		} catch (InterruptedException | ExecutionException e) {
			return Optional.empty();
		}
	}

	@Override
	public List<Home> getHomes() {
		return homes.stream().map(HomeData::toInterface).collect(Collectors.toList());
	}

	@Override
	public List<Warp> getWarps() {
		return warps.stream().map(WarpData::toInterface).collect(Collectors.toList());
	}

	@Override 
	public long getTotalHomes() {
		return homes.size();
	}

	@Override 
	public long getTotalWarps() {
		return warps.size();
	}

	@Override
	public boolean addHome(Home home, int limit) {
		if(!removeHome(home.getName()) && homes.size() >= limit) return false;
		if(homes.size() == 0) ((HomeData) home).setDefault();
		homes.add((HomeData) home);
		return true;
	}

	@Override
	public boolean removeHome(String name) {
		return homes.removeIf(home -> (home.getName().equals(name)));
	}

	@Override
	public boolean addWarp(Warp warp, int limit) {
		if(!removeWarp(warp.getName()) && warps.size() >= limit) return false;
		warps.add((WarpData) warp);
		return true;
	}

	@Override
	public boolean removeWarp(String name) {
		return warps.removeIf(warp -> (warp.getName().equals(name)));
	}

	@Override
	public boolean containsWarp(String name) {
		return warps.stream().filter(warp -> (warp.getName().equals(name))).findFirst().isPresent();
	}

	@Override
	public Optional<Home> getDefaultHome() {
		return homes.stream().filter(Home::isDefault).map(home -> ((Home) home)).findFirst();
	}

	@Override
	public Optional<Home> getHome(String name) {
		return homes.stream().filter(home -> (name.equals(TextUtils.clearDecorations(home.asComponent())))).map(HomeData::toInterface).findFirst();
	}

	@Override
	public Optional<Warp> getWarp(String name) {
		return warps.stream().filter(warp -> (name.equals(TextUtils.clearDecorations(warp.asComponent())))).map(WarpData::toInterface).findFirst();
	}
	@Override
	public Backpack getBackpack() {
		if(!backpackData.canSave()) backpackData.setSaveConsumer(consumer -> {
			save();
		});
		return backpackData;
	}

	@Override
	public void setBackpack(Backpack backpack) {
		this.backpackData = backpack instanceof BackpackData ? (BackpackData) backpack : (BackpackData) Backpack.builder().copyFrom(backpack).build();
		save();
		backpackData.setSaveConsumer(consumer -> {
			save();
		});
	}

	@Override
	public List<Component> homesListChatMenu(Locale locale, boolean allowRemove) {
		Locales locales = ((CommandPack) Sponge.pluginManager().plugin("commandpack").get().instance()).getLocales();
		List<Component> list = new ArrayList<>();
		homes.forEach(home -> {
			Component remove = allowRemove ? locales.getText(locale, LocalesPaths.REMOVE).clickEvent(SpongeComponents.executeCallback(cause -> {
				removeHome(home.getName());
				if(!homes.stream().filter(HomeData::isDefault).findFirst().isPresent()) {
					if(!homes.isEmpty()) homes.iterator().next().setDefault();
				}
				save();
			})) : Component.empty();
			Component teleport = home.getLocation().getServerLocation().isPresent() ? locales.getText(locale, LocalesPaths.TELEPORTCLICKABLE).clickEvent(SpongeComponents.executeCallback(cause -> {
				CommandPack.getInstance().getPlayersData().getTempData().setPreviousLocation((ServerPlayer) cause.root());
				home.getLocation().moveHere((ServerPlayer) cause.root());
			})) : locales.getText(locale, LocalesPaths.TELEPORT);
			Component homeName = home.asComponent();
			list.add(remove.append(teleport).append(homeName));
		});
		return list;
	}

	@Override
	public List<Component> warpsListChatMenu(Locale locale, Predicate<Warp> allowRemove, Predicate<Warp> allowTeleport) {
		Locales locales = ((CommandPack) Sponge.pluginManager().plugin("commandpack").get().instance()).getLocales();
		List<Component> list = new ArrayList<>();
		warps.forEach(warp -> {
			Component remove = allowRemove.test(warp) ? locales.getText(locale, LocalesPaths.REMOVE).clickEvent(SpongeComponents.executeCallback(cause -> {
				removeWarp(warp.getName());
				save();
			})) : Component.empty();
			Component teleport =  warp.getLocation().getServerLocation().isPresent() && allowTeleport.test(warp) ? locales.getText(locale, LocalesPaths.TELEPORTCLICKABLE).clickEvent(SpongeComponents.executeCallback(cause -> {
				CommandPack.getInstance().getPlayersData().getTempData().setPreviousLocation((ServerPlayer) cause.root());
				warp.moveHere((ServerPlayer) cause.root());
			})) : locales.getText(locale, LocalesPaths.TELEPORT);
			Component homeName = warp.asComponent();
			list.add(remove.append(teleport).append(homeName));
		});
		return list;
	}

	@Override
	public long getKitGivedTime(Kit kit) {
		return givedKits.containsKey(kit.id()) ? givedKits.get(kit.id()).getLastGivedTime() : 0l;
	}

	@Override
	public boolean isGivedKit(Kit kit) {
		return givedKits.containsKey(kit.id());
	}

	@Override
	public GivedKit getKitGivedData(Kit kit) {
		return givedKits.containsKey(kit.id()) ? givedKits.get(kit.id()) : null;
	}

	public Map<String, GivedKitData> givedKits() {
		return givedKits;
	}

	@Override
	public void sendMessage(Component component) {
		getPlayer().ifPresent(player -> {
			player.sendMessage(component);
		});
	}

	@Override
	public void sendMessage(String string) {
		sendMessage(TextUtils.deserialize(string));
	}

	@Override
	public sawfowl.commandpack.api.data.player.PlayerData save() {
		if(getPlayer().isPresent() && !getPlayer().get().name().equals(name)) name = getPlayer().get().name();
		((CommandPack) Sponge.pluginManager().plugin("commandpack").get().instance()).getConfigManager().savePlayerData(this);
		return this;
	}
	@SuppressWarnings("hiding")
	@Override
	public <ServerPlayer> CommandResult runCommand(@NotNull Locale sourceLocale, @NotNull String command) throws CommandException {
		if(!getPlayer().isPresent() || !getPlayer().get().isOnline()) return CommandResult.error(TextUtils.replace(CommandPack.getInstance().getLocales().getText(sourceLocale, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_IS_OFFLINE), Placeholders.PLAYER, name));
		CommandMapping mapping = Sponge.server().commandManager().commandMapping(command.contains(" ") ? command.split(" ")[0] : command).get();
		try(StackFrame frame = Sponge.server().causeStackManager().pushCauseFrame()) {
			frame.addContext(EventContextKeys.SUBJECT, getPlayer().get());
			frame.pushCause(getPlayer().get());
			return mapping.registrar().canExecute(createPlayerCause(getPlayer().get(), command), mapping) ? Sponge.server().commandManager().process(getPlayer().get(), getPlayer().get(), command) : CommandResult.error(CommandPack.getInstance().getLocales().getText(sourceLocale, LocalesPaths.COMMANDS_SUDO_EXECUTE_NOT_ALLOWED));
		}
	}

	@Override
	public long getLastJoinTime() {
		return lastJoin;
	}

	public void setLastJoin() {
		lastJoin = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
		save();
	}

	@Override
	public long getLastExitTime() {
		return lastExit;
	}

	public void setLastExit() {
		lastExit = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
		save();
	}

	private CommandCause createPlayerCause(ServerPlayer player, String command) {
		Cause cause = Sponge.server().causeStackManager().addContext(EventContextKeys.SUBJECT, player).addContext(EventContextKeys.COMMAND, command).pushCause(player).currentCause();
		return new CommandCause() {
			@Override
			public Optional<BlockSnapshot> targetBlock() {
				return Optional.empty();
			}
			
			@Override
			public Subject subject() {
				return player;
			}
			
			@Override
			public void sendMessage(Identity source, Component message) {
				player.sendMessage(message);
			}
			
			@Override
			public void sendMessage(Identified source, Component message) {
				sendMessage(player, message);
			}
			
			@Override
			public Optional<Vector3d> rotation() {
				return Optional.ofNullable(player.rotation());
			}
			
			@Override
			public Optional<ServerLocation> location() {
				return Optional.ofNullable(player.serverLocation());
			}
			
			@Override
			public Cause cause() {
				return cause;
			}
			
			@Override
			public Audience audience() {
				return player;
			}
		};
	}

	public boolean isVanished() {
		return vanished;
	}

	public void setVanished(boolean vanished) {
		this.vanished = vanished;
		save();
	}

	public boolean isGodMode() {
		return godMode;
	}

	public void setGodMode(boolean godMode) {
		this.godMode = godMode;
	}

	public boolean isFly() {
		return fly;
	}

	public void setFly(boolean fly) {
		this.fly = fly;
	}

	@Override
	public boolean isHideBalance() {
		return hideBalance;
	}

	@Override
	public void setHideBalance(boolean hideBalance) {
		this.hideBalance = hideBalance;
	}

}
