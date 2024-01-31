package sawfowl.commandpack.configure.configs.commands;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.Command.Raw;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.reference.ValueReference;
import org.spongepowered.configurate.serialize.SerializationException;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModFileInfo;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.data.command.CancelRules;
import sawfowl.commandpack.api.data.command.Delay;
import sawfowl.commandpack.api.data.command.Price;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.commands.settings.Register;

@ConfigSerializable
public class CommandsConfig {

	public CommandsConfig() {}

	private Map<String, Settings> map = new HashMap<>();

	@Setting("Suicide")
	private Settings suicide = Settings.builder().build();
	@Setting("Hat")
	private Settings hat = Settings.builder().build();
	@Setting("Home")
	private Settings home = Settings.builder().setDelay(Delay.of(0, CancelRules.of(false, false))).build();
	@Setting("SetHome")
	private Settings setHome = Settings.builder().build();
	@Setting("SetSpawn")
	private Settings setSpawn = Settings.builder().build();
	@Setting("Spawn")
	private Settings spawn = Settings.builder().setDelay(Delay.of(3, CancelRules.of(false, false))).build();
	@Setting("SetWarp")
	private Settings setWarp = Settings.builder().build();
	@Setting("Warp")
	private Settings warp = Settings.builder().setCooldown(15).setDelay(Delay.of(3, CancelRules.of(false, false))).setRawAutoComplete(false).generateRawCommandTree(true).build();
	@Setting("Warps")
	private Settings warps = Settings.builder().setDelay(Delay.of(3, CancelRules.of(false, false))).build();
	@Setting("Tpa")
	private Settings tpa = Settings.builder().setDelay(Delay.of(3, CancelRules.of(false, false))).setPrice(Price.of("$", 5)).build();
	@Setting("Tpahere")
	private Settings tpahere = Settings.builder().setDelay(Delay.of(3, CancelRules.of(false, false))).setPrice(Price.of("$", 10)).build();
	@Setting("Tpahereall")
	private Settings tpahereall = Settings.builder().setDelay(Delay.of(5, CancelRules.of(false, false))).build();
	@Setting("Teleport")
	private Settings teleport = Settings.builder().setAliases("tp").build();
	@Setting("TeleportHere")
	private Settings teleporthere = Settings.builder().setAliases("tphere").build();
	@Setting("TeleportHereAll")
	private Settings teleporthereall = Settings.builder().setAliases("tphereall").build();
	@Setting("RandomTeleport")
	private Settings rtp = Settings.builder().setAliases("randomtp", "rtp").build();
	@Setting("Tppos")
	private Settings tppos = Settings.builder().build();
	@Setting("TpToggle")
	private Settings tptoggle = Settings.builder().build();
	@Setting("Clearinventory")
	private Settings clearinventory = Settings.builder().setAliases("clear", "ci").build();
	@Setting("Repair")
	private Settings repair = Settings.builder().setPrice(Price.of("$", 20)).setAliases("fix").build();
	@Setting("Enderchest")
	private Settings enderchest = Settings.builder().build();
	@Setting("InventorySee")
	private Settings inventorysee = Settings.builder().setAliases("invsee").build();
	@Setting("Top")
	private Settings top = Settings.builder().build();
	@Setting("Jump")
	private Settings jump = Settings.builder().build();
	@Setting("Back")
	private Settings back = Settings.builder().setDelay(Delay.of(3, CancelRules.of(false, false))).build();
	@Setting("Fly")
	private Settings fly = Settings.builder().setDelay(Delay.of(3, CancelRules.of(false, false))).build();
	@Setting("GodMode")
	private Settings godMode = Settings.builder().setDelay(Delay.of(5, CancelRules.of(false, false))).setAliases("god").build();
	@Setting("Speed")
	private Settings speed = Settings.builder().build();
	@Setting("Disposal")
	private Settings disposal = Settings.builder().setAliases("trash").build();
	@Setting("GameMode")
	private Settings gamemode = Settings.builder().setAliases("gm").build();
	@Setting("Creative")
	private Settings creative = Settings.builder().setAliases("gmc").build();
	@Setting("Spectator")
	private Settings spectator = Settings.builder().setAliases("gmsp").build();
	@Setting("Survival")
	private Settings survival = Settings.builder().setAliases("gms").build();
	@Setting("Adventure")
	private Settings adventure = Settings.builder().setAliases("gma").build();
	@Setting("Weather")
	private Settings weather = Settings.builder().setCooldown(120).build();
	@Setting("Sun")
	private Settings sun = Settings.builder().setCooldown(120).build();
	@Setting("Rain")
	private Settings rain = Settings.builder().setCooldown(120).build();
	@Setting("Thunder")
	private Settings thunder = Settings.builder().setCooldown(120).setAliases("storm").build();
	@Setting("ServerTime")
	private Settings time = Settings.builder().setCooldown(120).build();
	@Setting("Morning")
	private Settings morning = Settings.builder().setCooldown(120).build();
	@Setting("Day")
	private Settings day = Settings.builder().setCooldown(120).build();
	@Setting("Evening")
	private Settings evening = Settings.builder().setCooldown(120).build();
	@Setting("Night")
	private Settings night = Settings.builder().setCooldown(120).build();
	@Setting("Enchant")
	private Settings enchant = Settings.builder().setRawAutoComplete(true).generateRawCommandTree(true).build();
	@Setting("Anvil")
	private Settings anvil = Settings.builder().build();
	@Setting("CraftingTable")
	private Settings craftingTable = Settings.builder().setAliases("craft", "ct", "workbench").build();
	@Setting("EnchantmentTable")
	private Settings enchantmenttable = Settings.builder().build();
	@Setting("Backpack")
	private Settings backpack = Settings.builder().build();
	@Setting("Feed")
	private Settings feed = Settings.builder().setAliases("food", "eat").build();
	@Setting("Heal")
	private Settings heal = Settings.builder().build();
	@Setting("Broadcast")
	private Settings broadcast = Settings.builder().build();
	@Setting("Sudo")
	private Settings sudo = Settings.builder().setRawAutoComplete(true).generateRawCommandTree(true).build();
	@Setting("Vanish")
	private Settings vanish = Settings.builder().build();
	@Setting("Nick")
	private Settings nick = Settings.builder().setAliases("name").build();
	@Setting("Item")
	private Settings item = Settings.builder().build();
	@Setting("ServerStat")
	private Settings serverStat = Settings.builder().setAliases("serverinfo", "gc").build();
	@Setting("Plugins")
	private Settings plugins = Settings.builder().setAliases("pl").build();
	@Setting("Mods")
	private Settings mods = Settings.builder().build();
	@Setting("Tps")
	private Settings tps = Settings.builder().build();
	@Setting("ServerTime")
	private Settings serverTime = Settings.builder().build();
	@Setting("Kits")
	private Settings kits = Settings.builder().setRawAutoComplete(true).generateRawCommandTree(true).build();
	@Setting("Kit")
	private Settings kit = Settings.builder().setRawAutoComplete(true).generateRawCommandTree(true).build();
	@Setting("Afk")
	private Settings afk = Settings.builder().build();
	@Setting("World")
	private Settings world = Settings.builder().setRawAutoComplete(true).generateRawCommandTree(true).build();
	@Setting("CommandSpy")
	private Settings commandspy = Settings.builder().build();
	@Setting("Ping")
	private Settings ping = Settings.builder().build();
	@Setting("List")
	private Settings list = Settings.builder().build();
	@Setting("Seen")
	private Settings seen = Settings.builder().setAliases("playerinfo", "playerstat", "whois").build();
	@Setting("Help")
	private Settings help = Settings.builder().build();
	@Setting("Glow")
	private Settings glow = Settings.builder().build();
	@Setting("Flame")
	private Settings flame = Settings.builder().setAliases("burn", "fire").build();
	@Setting("Extinguish")
	private Settings extinguish = Settings.builder().setAliases("ext").build();
	@Setting("Ban")
	private Settings ban = Settings.builder().build();
	@Setting("Unban")
	private Settings unban = Settings.builder().setAliases("pardon").setRawAutoComplete(true).generateRawCommandTree(true).build();
	@Setting("Banip")
	private Settings banip = Settings.builder().build();
	@Setting("Unbanip")
	private Settings unbanip = Settings.builder().setAliases("pardonip").setRawAutoComplete(true).generateRawCommandTree(true).build();
	@Setting("Kick")
	private Settings kick = Settings.builder().build();
	@Setting("Mute")
	private Settings mute = Settings.builder().build();
	@Setting("Unmute")
	private Settings unmute = Settings.builder().setRawAutoComplete(true).generateRawCommandTree(true).build();
	@Setting("Warn")
	private Settings warn = Settings.builder().build();
	@Setting("Warnings")
	private Settings warns = Settings.builder().setAliases("warns").setRawAutoComplete(true).generateRawCommandTree(true).build();
	@Setting("BanList")
	private Settings banList = Settings.builder().setAliases("bans").build();
	@Setting("BanInfo")
	private Settings banInfo = Settings.builder().setRawAutoComplete(true).generateRawCommandTree(true).build();
	@Setting("MuteInfo")
	private Settings muteInfo = Settings.builder().setRawAutoComplete(true).generateRawCommandTree(true).build();
	@Setting("MuteList")
	private Settings muteList = Settings.builder().setAliases("mutes").build();
	@Setting("Balance")
	private Settings balance = Settings.builder().setAliases("money").setRawAutoComplete(true).generateRawCommandTree(true).build();
	@Setting("BalanceTop")
	private Settings balanceTop = Settings.builder().setAliases("baltop").setRawAutoComplete(true).generateRawCommandTree(true).build();
	@Setting("HideBalance")
	private Settings hideBalance = Settings.builder().setRawAutoComplete(true).generateRawCommandTree(true).build();
	@Setting("Economy")
	private Settings economy = Settings.builder().setAliases("eco").setRawAutoComplete(true).generateRawCommandTree(true).build();
	@Setting("Pay")
	private Settings pay = Settings.builder().setRawAutoComplete(true).generateRawCommandTree(true).build();
	@Setting("Tell")
	private Settings tell = Settings.builder().setAliases("say", "s", "m").setRawAutoComplete(true).generateRawCommandTree(true).build();
	@Setting("Reply")
	private Settings reply = Settings.builder().setAliases("r").build();

	public Settings getCommandConfig(String command) {
		return map.getOrDefault(command.toLowerCase(), map.values().stream().filter(config -> (config.getAliasesList().contains(command))).findFirst().orElse(CommandSettings.EMPTY));
	}

	public Optional<Settings> getOptCommandSettings(String command) {
		return Optional.ofNullable(map.getOrDefault(command, null));
	}

	public void registerParameterized(RegisterCommandEvent<Parameterized> event, CommandPack plugin) {
		try {
			for(Class<AbstractParameterizedCommand> clazz : findAllCommandsClasses(plugin, "sawfowl.commandpack.commands.parameterized", AbstractParameterizedCommand.class)) {
				try {
					AbstractParameterizedCommand command = clazz.getConstructor(CommandPack.class).newInstance(plugin);
					getOptCommandSettings(command.command()).ifPresent(settings -> {
						registerParameterizedCommand(event, plugin, settings, command);
					});
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					plugin.getLogger().error("Error when registering a command class '" + clazz.getName());
					e.printStackTrace();
				}
			}
		} catch (IOException | NoSuchMethodException | SecurityException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void registerRaw(RegisterCommandEvent<Raw> event, CommandPack plugin) {
		try {
			for(Class<AbstractRawCommand> clazz : findAllCommandsClasses(plugin, "sawfowl.commandpack.commands.raw", AbstractRawCommand.class)) {
				try {
					AbstractRawCommand command = clazz.getConstructor(CommandPack.class).newInstance(plugin);
					getOptCommandSettings(command.command()).ifPresent(settings -> {
						if(settings.isEnable()) {
							command.register(event);
							plugin.getPlayersData().getTempData().registerCommandTracking(command);
						}
					});
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					plugin.getLogger().error("Error when registering a command class '" + clazz.getName());
					e.printStackTrace();
				}
			}
		} catch (IOException | NoSuchMethodException | SecurityException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void updateCommandMap(ValueReference<CommandsConfig, CommentedConfigurationNode> reference) {
		map.clear();
		reference.node().childrenMap().entrySet().forEach(entry -> {
			map.put(entry.getKey().toString().toLowerCase(), getCommandFromNode(entry.getValue()));
		});
	}

	private Settings getCommandFromNode(CommentedConfigurationNode node) {
		try {
			return node.get(CommandSettings.class, CommandSettings.EMPTY);
		} catch (SerializationException e) {
			return CommandSettings.EMPTY;
		}
	}

	
	@SuppressWarnings("unchecked")
	private <T> Set<Class<T>> findAllCommandsClasses(CommandPack plugin, String packageName, Class<T> clazz) throws IOException, URISyntaxException {
		final String pkgPath = packageName.replace('.', '/');
		URI pkg = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(pkgPath)).toURI();
		if(plugin.isForgeServer() && ModList.get().getModContainerByObject(CommandPack.getInstance()).isPresent()) {
			ModContainer container = ModList.get().getModContainerByObject(CommandPack.getInstance()).get();
			IModFileInfo modFileInfo = container.getModInfo().getOwningFile();
			pkg = URI.create("jar:" + modFileInfo.getFile().getFilePath().toUri().toString()  + "!/" + pkgPath);
		}
		final Set<Class<T>> allClasses = new HashSet<Class<T>>();
		Path root;
		if (pkg.toString().startsWith("jar:")) {
			try {
				root = FileSystems.getFileSystem(pkg).getPath(pkgPath);
			} catch (final FileSystemNotFoundException e) {
				root = FileSystems.newFileSystem(pkg, Collections.emptyMap()).getPath(pkgPath);
			}
		} else {
			root = Paths.get(pkg);
		}
		final String extension = ".class";
		try (final Stream<Path> allPaths = Files.walk(root)) {
			allPaths.filter(Files::isRegularFile).forEach(file -> {
				try {
					final String path = file.toString().replace('/', '.');
					final String name = path.substring(path.indexOf(packageName), path.length() - extension.length());
					Class<?> found = Class.forName(name);
					if(found.isAnnotationPresent(Register.class) && ((found.getSuperclass() != null && found.getSuperclass() == clazz) || (found.getSuperclass().getSuperclass() != null && found.getSuperclass().getSuperclass() == clazz))) allClasses.add((Class<T>) found);
				} catch (final ClassNotFoundException | StringIndexOutOfBoundsException ignored) {
				}
			});
		}
		return allClasses;
	}

	private void registerParameterizedCommand(RegisterCommandEvent<Parameterized> event, CommandPack plugin, Settings settings, AbstractParameterizedCommand command) {
		if(!settings.isEnable() || (serverStat.isEnable() && (command.command().equalsIgnoreCase("mods") || command.command().equalsIgnoreCase("plugins") || command.command().equalsIgnoreCase("tps") || command.command().equalsIgnoreCase("servertime")))) return;
		command.register(event);
		plugin.getPlayersData().getTempData().registerCommandTracking(command);
	}

}
