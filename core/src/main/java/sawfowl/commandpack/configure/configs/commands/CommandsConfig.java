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
import sawfowl.commandpack.api.data.command.RawSettings;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.commandpack.api.data.command.UpdateTree;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.commands.settings.Register;

@ConfigSerializable
public class CommandsConfig {

	public CommandsConfig() {}

	private Map<String, Settings> map = new HashMap<>();

	@Setting("Suicide")
	private Settings suicide = new CommandSettings().builder().build();
	@Setting("Hat")
	private Settings hat = new CommandSettings().builder().build();
	@Setting("Home")
	private Settings home = new CommandSettings().builder().setDelay(DelayData.of(0, CancelRulesData.of(false, false))).build();
	@Setting("SetHome")
	private Settings setHome = new CommandSettings().builder().build();
	@Setting("SetSpawn")
	private Settings setSpawn = new CommandSettings().builder().build();
	@Setting("Spawn")
	private Settings spawn = new CommandSettings().builder().setDelay(DelayData.of(3, CancelRulesData.of(false, false))).build();
	@Setting("SetWarp")
	private Settings setWarp = new CommandSettings().builder().build();
	@Setting("Warp")
	private Settings warp = new CommandSettings().builder().setCooldown(15).setDelay(DelayData.of(3, CancelRulesData.of(false, false))).setRawSettings(new RawSettingsImpl().builder().setUpdateTree(UpdateTree.defaultValues()).setAutoComplete(false).setGenerateRawTree(true).build()).build();
	@Setting("Warps")
	private Settings warps = new CommandSettings().builder().setDelay(DelayData.of(3, CancelRulesData.of(false, false))).build();
	@Setting("Tpa")
	private Settings tpa = new CommandSettings().builder().setDelay(DelayData.of(3, CancelRulesData.of(false, false))).setPrice(CommandPrice.of("$", 5)).build();
	@Setting("Tpahere")
	private Settings tpahere = new CommandSettings().builder().setDelay(DelayData.of(3, CancelRulesData.of(false, false))).setPrice(CommandPrice.of("$", 10)).build();
	@Setting("Tpahereall")
	private Settings tpahereall = new CommandSettings().builder().setDelay(DelayData.of(5, CancelRulesData.of(false, false))).build();
	@Setting("Teleport")
	private Settings teleport = new CommandSettings().builder().setAliases("tp").build();
	@Setting("TeleportHere")
	private Settings teleporthere = new CommandSettings().builder().setAliases("tphere").build();
	@Setting("TeleportHereAll")
	private Settings teleporthereall = new CommandSettings().builder().setAliases("tphereall").build();
	@Setting("RandomTeleport")
	private Settings rtp = new CommandSettings().builder().setAliases("randomtp", "rtp").build();
	@Setting("Tppos")
	private Settings tppos = new CommandSettings().builder().build();
	@Setting("TpToggle")
	private Settings tptoggle = new CommandSettings().builder().build();
	@Setting("Clearinventory")
	private Settings clearinventory = new CommandSettings().builder().setAliases("clear", "ci").build();
	@Setting("Repair")
	private Settings repair = new CommandSettings().builder().setPrice(CommandPrice.of("$", 20)).setAliases("fix").build();
	@Setting("Enderchest")
	private Settings enderchest = new CommandSettings().builder().build();
	@Setting("InventorySee")
	private Settings inventorysee = new CommandSettings().builder().setAliases("invsee").build();
	@Setting("Top")
	private Settings top = new CommandSettings().builder().build();
	@Setting("Jump")
	private Settings jump = new CommandSettings().builder().build();
	@Setting("Back")
	private Settings back = new CommandSettings().builder().setDelay(DelayData.of(3, CancelRulesData.of(false, false))).build();
	@Setting("Fly")
	private Settings fly = new CommandSettings().builder().setDelay(DelayData.of(3, CancelRulesData.of(false, false))).build();
	@Setting("GodMode")
	private Settings godMode = new CommandSettings().builder().setDelay(DelayData.of(5, CancelRulesData.of(false, false))).setAliases("god").build();
	@Setting("Speed")
	private Settings speed = new CommandSettings().builder().build();
	@Setting("Disposal")
	private Settings disposal = new CommandSettings().builder().setAliases("trash").build();
	@Setting("GameMode")
	private Settings gamemode = new CommandSettings().builder().setAliases("gm").build();
	@Setting("Creative")
	private Settings creative = new CommandSettings().builder().setAliases("gmc").build();
	@Setting("Spectator")
	private Settings spectator = new CommandSettings().builder().setAliases("gmsp").build();
	@Setting("Survival")
	private Settings survival = new CommandSettings().builder().setAliases("gms").build();
	@Setting("Adventure")
	private Settings adventure = new CommandSettings().builder().setAliases("gma").build();
	@Setting("Weather")
	private Settings weather = new CommandSettings().builder().setCooldown(120).build();
	@Setting("Sun")
	private Settings sun = new CommandSettings().builder().setCooldown(120).build();
	@Setting("Rain")
	private Settings rain = new CommandSettings().builder().setCooldown(120).build();
	@Setting("Thunder")
	private Settings thunder = new CommandSettings().builder().setCooldown(120).setAliases("storm").build();
	@Setting("ServerTime")
	private Settings time = new CommandSettings().builder().setCooldown(120).build();
	@Setting("Morning")
	private Settings morning = new CommandSettings().builder().setCooldown(120).build();
	@Setting("Day")
	private Settings day = new CommandSettings().builder().setCooldown(120).build();
	@Setting("Evening")
	private Settings evening = new CommandSettings().builder().setCooldown(120).build();
	@Setting("Night")
	private Settings night = new CommandSettings().builder().setCooldown(120).build();
	@Setting("Enchant")
	private Settings enchant = new CommandSettings().builder().setRawSettings(RawSettings.of(new UpdateRawTree().builder().setEnable(false).build(), false, true)).build();
	@Setting("Anvil")
	private Settings anvil = new CommandSettings().builder().build();
	@Setting("CraftingTable")
	private Settings craftingTable = new CommandSettings().builder().setAliases("craft", "ct", "workbench").build();
	@Setting("EnchantmentTable")
	private Settings enchantmenttable = new CommandSettings().builder().build();
	@Setting("Backpack")
	private Settings backpack = new CommandSettings().builder().build();
	@Setting("Feed")
	private Settings feed = new CommandSettings().builder().setAliases("food", "eat").build();
	@Setting("Heal")
	private Settings heal = new CommandSettings().builder().build();
	@Setting("Broadcast")
	private Settings broadcast = new CommandSettings().builder().build();
	@Setting("Sudo")
	private Settings sudo = new CommandSettings().builder().setRawSettings(RawSettings.of(new UpdateRawTree().builder().setEnable(false).setInterval(15).build(), true, false)).build();
	@Setting("Vanish")
	private Settings vanish = new CommandSettings().builder().build();
	@Setting("Nick")
	private Settings nick = new CommandSettings().builder().setAliases("name").build();
	@Setting("Item")
	private Settings item = new CommandSettings().builder().build();
	@Setting("ServerStat")
	private Settings serverStat = new CommandSettings().builder().setAliases("serverinfo", "gc").build();
	@Setting("Plugins")
	private Settings plugins = new CommandSettings().builder().setAliases("pl").build();
	@Setting("Mods")
	private Settings mods = new CommandSettings().builder().build();
	@Setting("Tps")
	private Settings tps = new CommandSettings().builder().build();
	@Setting("ServerTime")
	private Settings serverTime = new CommandSettings().builder().build();
	@Setting("Kits")
	private Settings kits = new CommandSettings().builder().setRawSettings(RawSettings.of(new UpdateRawTree().builder().setEnable(false).build(), false, true)).build();
	@Setting("Kit")
	private Settings kit = new CommandSettings().builder().setRawSettings(RawSettings.of(new UpdateRawTree().builder().setEnable(true).setInterval(300).build(), false, true)).build();
	@Setting("Afk")
	private Settings afk = new CommandSettings().builder().build();
	@Setting("World")
	private Settings world = new CommandSettings().builder().setRawSettings(RawSettings.of(new UpdateRawTree().builder().setEnable(false).setInterval(300).build(), true, false)).build();
	@Setting("CommandSpy")
	private Settings commandspy = new CommandSettings().builder().build();
	@Setting("Ping")
	private Settings ping = new CommandSettings().builder().build();
	@Setting("List")
	private Settings list = new CommandSettings().builder().build();
	@Setting("Seen")
	private Settings seen = new CommandSettings().builder().setAliases("playerinfo", "playerstat", "whois").build();
	@Setting("Help")
	private Settings help = new CommandSettings().builder().build();
	@Setting("Glow")
	private Settings glow = new CommandSettings().builder().build();
	@Setting("Flame")
	private Settings flame = new CommandSettings().builder().setAliases("burn", "fire").build();
	@Setting("Extinguish")
	private Settings extinguish = new CommandSettings().builder().setAliases("ext").build();
	@Setting("Ban")
	private Settings ban = new CommandSettings().builder().build();
	@Setting("Unban")
	private Settings unban = new CommandSettings().builder().setAliases("pardon").setRawSettings(RawSettings.of(new UpdateRawTree().builder().setEnable(false).setInterval(30).build(), true, false)).build();
	@Setting("Banip")
	private Settings banip = new CommandSettings().builder().build();
	@Setting("Unbanip")
	private Settings unbanip = new CommandSettings().builder().setAliases("pardonip").setRawSettings(RawSettings.defaultValues()).build();
	@Setting("Kick")
	private Settings kick = new CommandSettings().builder().build();
	@Setting("Mute")
	private Settings mute = new CommandSettings().builder().build();
	@Setting("Unmute")
	private Settings unmute = new CommandSettings().builder().setRawSettings(RawSettings.of(new UpdateRawTree().builder().setEnable(false).setInterval(30).build(), true, false)).build();
	@Setting("Warn")
	private Settings warn = new CommandSettings().builder().build();
	@Setting("Warnings")
	private Settings warns = new CommandSettings().builder().setAliases("warns").setRawSettings(RawSettings.of(new UpdateRawTree().builder().setEnable(true).setInterval(30).build(), true, true)).build();
	@Setting("BanList")
	private Settings banList = new CommandSettings().builder().setAliases("bans").build();
	@Setting("BanInfo")
	private Settings banInfo = new CommandSettings().builder().setRawSettings(RawSettings.of(new UpdateRawTree().builder().setEnable(false).setInterval(30).build(), true, false)).build();
	@Setting("MuteInfo")
	private Settings muteInfo = new CommandSettings().builder().setRawSettings(RawSettings.of(new UpdateRawTree().builder().setEnable(false).setInterval(30).build(), true, false)).build();
	@Setting("MuteList")
	private Settings muteList = new CommandSettings().builder().setAliases("mutes").build();
	@Setting("Balance")
	private Settings balance = new CommandSettings().builder().setAliases("money").setRawSettings(RawSettings.of(new UpdateRawTree().builder().setEnable(false).setInterval(10).build(), true, false)).build();
	@Setting("BalanceTop")
	private Settings balanceTop = new CommandSettings().builder().setAliases("baltop").setRawSettings(RawSettings.of(new UpdateRawTree().builder().setEnable(false).setInterval(30).build(), true, false)).build();
	@Setting("HideBalance")
	private Settings hideBalance = new CommandSettings().builder().setRawSettings(RawSettings.defaultValues()).build();
	@Setting("Economy")
	private Settings economy = new CommandSettings().builder().setAliases("eco").setRawSettings(RawSettings.of(new UpdateRawTree().builder().setEnable(false).setInterval(30).build(), true, false)).build();
	@Setting("Pay")
	private Settings pay = new CommandSettings().builder().setRawSettings(RawSettings.defaultValues()).build();
	@Setting("Tell")
	private Settings tell = new CommandSettings().builder().setAliases("say", "s", "m").setRawSettings(RawSettings.of(new UpdateRawTree().builder().setEnable(false).setInterval(10).build(), true, false)).build();
	@Setting("Reply")
	private Settings reply = new CommandSettings().builder().setAliases("r").build();

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
