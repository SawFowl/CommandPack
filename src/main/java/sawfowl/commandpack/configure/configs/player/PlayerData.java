package sawfowl.commandpack.configure.configs.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.adventure.SpongeComponents;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.data.player.Home;
import sawfowl.commandpack.api.data.player.Backpack;
import sawfowl.commandpack.api.data.player.Warp;
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
	private Map<String, Long> givedKits = new HashMap<>();

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

	public Map<String, Long> givedKits() {
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

	@Override
	public String toString() {
		return "PlayerData [name=" + name + ", uuid=" + uuid + ", homes=" + homes + "]";
	}

}
