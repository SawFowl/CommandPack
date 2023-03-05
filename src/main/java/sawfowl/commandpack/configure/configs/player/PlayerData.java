package sawfowl.commandpack.configure.configs.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.adventure.SpongeComponents;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.data.player.Home;
import sawfowl.commandpack.configure.locale.Locales;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class PlayerData implements sawfowl.commandpack.api.data.player.PlayerData {

	public PlayerData() {}
	public PlayerData(ServerPlayer player) {
		this.name = player.name();
		this.uuid = player.uniqueId();
	}

	@Setting("Name")
	private String name;
	@Setting("UUID")
	private UUID uuid;
	@Setting("Homes")
	private List<HomeData> homes = new ArrayList<>();

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
		return homes.stream().map(home -> ((Home) home)).collect(Collectors.toList());
	}

	@Override 
	public long getTotalHomes() {
		return homes.size();
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
	public Optional<Home> getDefaultHome() {
		return homes.stream().filter(Home::isDefault).map(home -> ((Home) home)).findFirst();
	}

	@Override
	public Optional<Home> getHome(String name) {
		return homes.stream().filter(home -> (name.equals(TextUtils.clearDecorations(home.asComponent())))).map(home -> ((Home) home)).findFirst();
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
				home.getLocation().moveToThis((Entity) cause.root());
			})) : locales.getText(locale, LocalesPaths.TELEPORT);
			Component homeName = home.asComponent();
			list.add(remove.append(teleport).append(homeName));
		});
		return list;
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
