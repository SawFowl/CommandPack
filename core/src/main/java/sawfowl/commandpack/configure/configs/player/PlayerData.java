package sawfowl.commandpack.configure.configs.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.data.player.Home;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.api.data.player.Backpack;
import sawfowl.commandpack.api.data.player.GivedKit;
import sawfowl.commandpack.api.data.player.Warp;
import sawfowl.commandpack.configure.locales.AbstractLocale;
import sawfowl.localeapi.api.LocalesList;
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
	private Set<HomeData> homes = new HashSet<>();
	@Setting("Warps")
	private Set<WarpData> warps = new HashSet<>();
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
		save();
		return true;
	}

	@Override
	public boolean removeHome(String name) {
		if(homes.removeIf(home -> (home.getName().equals(name)))) {
			save();
			return true;
		} else return false;
	}

	public PlayerData addWarp(Warp warp) {
		removeWarp(warp.getName());
		warps.add(((WarpData) warp).setOwnerName(this));
		return this;
	}

	public PlayerData removeWarp(String name) {
		if(warps.removeIf(warp -> warp.getName() == null || warp.getName().equals(name) || warp.getPlainName().equals(name))) {
			warps.removeIf(warp -> warp.getName() == null || warp.getPlainName() == null);
			save();
		}
		return this;
	}

	@Override
	public boolean containsWarp(String name) {
		return warps.stream().filter(warp -> (warp.getPlainName().equals(name) || warp.getPlainName().equals(this.name + "-" + name))).findFirst().isPresent();
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
		LocalesList<AbstractLocale> locales = geLocales();
		List<Component> list = new ArrayList<>();
		homes.forEach(home -> {
			Component remove = allowRemove ? locales.getAsReferenced(locale).getButtons().getRemove().clickEvent(SpongeComponents.executeCallback(cause -> {
				removeHome(home.getName());
				if(!homes.stream().filter(HomeData::isDefault).findFirst().isPresent()) {
					if(!homes.isEmpty()) homes.iterator().next().setDefault();
				}
				save();
			})) : Component.empty();
			Component teleport = home.getLocation().getServerLocation().isPresent() ? locales.getAsReferenced(locale).getButtons().getTeleportClickable().clickEvent(SpongeComponents.executeCallback(cause -> {
				CommandPackInstance.getInstance().getPlayersData().getTempData().setPreviousLocation((ServerPlayer) cause.root());
				home.getLocation().moveHere((ServerPlayer) cause.root());
			})) : locales.getAsReferenced(locale).getButtons().getTeleport();
			Component homeName = home.asComponent();
			list.add(remove.append(teleport).append(homeName));
		});
		return list;
	}

	@Override
	public List<Component> warpsListChatMenu(Locale locale, Predicate<Warp> allowRemove, Predicate<Warp> allowTeleport) {
		LocalesList<AbstractLocale> locales = geLocales();
		List<Component> list = new ArrayList<>();
		warps.forEach(warp -> {
			Component remove = allowRemove.test(warp) ? locales.getAsReferenced(locale).getButtons().getRemove().clickEvent(SpongeComponents.executeCallback(cause -> {
				removeWarp(warp.getName());
				save();
			})) : Component.empty();
			Component teleport =  warp.getLocation().getServerLocation().isPresent() && allowTeleport.test(warp) ? locales.getAsReferenced(locale).getButtons().getTeleportClickable().clickEvent(SpongeComponents.executeCallback(cause -> {
				CommandPackInstance.getInstance().getPlayersData().getTempData().setPreviousLocation((ServerPlayer) cause.root());
				warp.moveHere((ServerPlayer) cause.root());
			})) : locales.getAsReferenced(locale).getButtons().getTeleport();
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
		CommandPackInstance.getInstance().getConfigManager().savePlayerData(this);
		return this;
	}

	@SuppressWarnings("hiding")
	@Override
	public <ServerPlayer> CommandResult runCommand(Locale sourceLocale, String command) throws CommandException {
		if(!getPlayer().isPresent() || !getPlayer().get().isOnline()) return CommandResult.error(CommandPackInstance.getInstance().getLocales().getAsReferenced(sourceLocale).getCommandExceptions().getPlayerIsOffline(name));
		CommandMapping mapping = Sponge.server().commandManager().commandMapping(command.contains(" ") ? command.split(" ")[0] : command).get();
		try(StackFrame frame = Sponge.server().causeStackManager().pushCauseFrame()) {
			frame.addContext(EventContextKeys.SUBJECT, getPlayer().get());
			frame.pushCause(getPlayer().get());
			return mapping.registrar().canExecute(createPlayerCause(getPlayer().get(), command), mapping) ? Sponge.server().commandManager().process(getPlayer().get(), getPlayer().get(), command) : CommandResult.error(CommandPackInstance.getInstance().getLocales().getAsReferenced(sourceLocale).getCommands().getSudo().getCommandNotAllowed());
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

	public PlayerData updateWarpsOwnerData() {
		warps.forEach(warp -> warp.setOwner(this));
		return this;
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

			@Override
			public void sendMessage(Component message) {
				player.sendMessage(message);
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

	@Override
	public JsonObject asJson() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.add("Homes", putAllJsonArray(new JsonArray(), homes.stream().map(h -> h.asJson()).toArray(JsonObject[]::new)));
		jsonObject.add("Warps", putAllJsonArray(new JsonArray(), warps.stream().map(w -> w.asJson()).toArray(JsonObject[]::new)));
		jsonObject.add("Backpack", backpackData.asJson());
		JsonObject givedKits = new JsonObject();
		this.givedKits.forEach((kit, data) -> givedKits.add(kit, data.asJson()));
		jsonObject.add("GivedKits", givedKits);
		jsonObject.addProperty("LastJoin", lastJoin);
		jsonObject.addProperty("LastExit", lastExit);
		jsonObject.addProperty("Vanished", vanished);
		jsonObject.addProperty("GodMode", godMode);
		jsonObject.addProperty("Fly", fly);
		jsonObject.addProperty("HideBalance", hideBalance);
		return jsonObject;
	}

	@Override
	public void updateFromJson(JsonObject json) {
		if(json.has("Homes") && json.get("Homes").isJsonArray()) {
			Set<HomeData> newHomes = new HashSet<HomeData>();
			for(JsonElement jsonHomeElement : json.get("Homes").getAsJsonArray()) {
				if(jsonHomeElement instanceof JsonObject jsonHome && jsonHome.has("Name") && jsonHome.get("Name").isJsonPrimitive()) {
					HomeData home = Home.builder().fromJson(jsonHome).map(h -> (HomeData) h).orElse(null);
					if(home != null) {
						newHomes.add(home);
						home = null;
					}
				}
			}
			if(!newHomes.isEmpty()) {
				homes.clear();
				homes.addAll(newHomes);
				newHomes = null;
			}
		}
		if(json.has("Warps") && json.get("Warps").isJsonArray()) {
			Set<WarpData> newWarps = new HashSet<WarpData>();
			for(JsonElement jsonHomeElement : json.get("Homes").getAsJsonArray()) {
				if(jsonHomeElement instanceof JsonObject jsonWarp && jsonWarp.has("Name") && jsonWarp.get("Name").isJsonPrimitive()) {
					WarpData home = Warp.builder().fromJson(jsonWarp).map(w -> (WarpData) w).orElse(null);
					if(home != null) {
						newWarps.add(home);
						home = null;
					}
				}
			}
			if(!newWarps.isEmpty()) {
				warps.clear();
				warps.addAll(newWarps);
				newWarps = null;
			}
		}
		if(json.has("Backpack") && json.get("Backpack").isJsonObject()) backpackData = (BackpackData) Backpack.builder().fromJson(json.get("Backpack").getAsJsonObject()).orElse(backpackData);
		if(json.has("GivedKits") && json.get("GivedKits").isJsonObject()) {
			json.getAsJsonObject("GivedKits").asMap().forEach((kit, data) -> {
				if(data instanceof JsonObject jsonData) if(givedKits.containsKey(kit)) {
					givedKits.get(kit).updateFromJson(jsonData);
				} else givedKits.put(kit, new GivedKitData().updateFromJson(jsonData));
			});
		}
		if(json.has("LastJoin") && json.get("LastJoin") instanceof JsonPrimitive primitive && primitive.isNumber()) lastJoin = primitive.getAsLong();
		if(json.has("LastExit") && json.get("LastExit") instanceof JsonPrimitive primitive && primitive.isNumber()) lastExit = primitive.getAsLong();
		if(json.has("Vanished") && json.get("Vanished") instanceof JsonPrimitive primitive && primitive.isBoolean()) vanished = primitive.getAsBoolean();
		if(json.has("GodMode") && json.get("GodMode") instanceof JsonPrimitive primitive && primitive.isBoolean()) godMode = primitive.getAsBoolean();
		if(json.has("Fly") && json.get("Fly") instanceof JsonPrimitive primitive && primitive.isBoolean()) fly = primitive.getAsBoolean();
		if(json.has("HideBalance") && json.get("HideBalance") instanceof JsonPrimitive primitive && primitive.isBoolean()) hideBalance = primitive.getAsBoolean();
		save();
	}

	private JsonArray putAllJsonArray(JsonArray array, JsonElement... elements) {
		for(JsonElement e : elements) array.add(e);
		return array;
	}

	private LocalesList<AbstractLocale> geLocales() {
		return ((CommandPackInstance) Sponge.pluginManager().plugin("commandpack").get().instance()).getLocales();
	}

}
