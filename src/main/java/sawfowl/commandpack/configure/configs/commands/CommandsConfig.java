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

import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;

@ConfigSerializable
public class CommandsConfig {

	public CommandsConfig() {}

	private Map<String, CommandSettings> map = new HashMap<>();

	@Setting("Suicide")
	private CommandSettings suicide = new CommandSettings();
	@Setting("Hat")
	private CommandSettings hat = new CommandSettings();
	@Setting("Home")
	private CommandSettings home = new CommandSettings(new DelayData(3));
	@Setting("SetHome")
	private CommandSettings setHome = new CommandSettings();
	@Setting("SetSpawn")
	private CommandSettings setSpawn = new CommandSettings();
	@Setting("Spawn")
	private CommandSettings spawn = new CommandSettings(new DelayData(3));
	@Setting("SetWarp")
	private CommandSettings setWarp = new CommandSettings();
	@Setting("Warp")
	private CommandSettings warp = new CommandSettings(15, new DelayData(3));
	@Setting("Warps")
	private CommandSettings warps = new CommandSettings(new DelayData(3));
	@Setting("Tpa")
	private CommandSettings tpa = new CommandSettings(new DelayData(3), new CommandPrice("$", 5));
	@Setting("Tpahere")
	private CommandSettings tpahere = new CommandSettings(new DelayData(3), new CommandPrice("$", 10));
	@Setting("Tpahereall")
	private CommandSettings tpahereall = new CommandSettings(new DelayData(3));
	@Setting("Teleport")
	private CommandSettings teleport = new CommandSettings(new String[] {"tp"});
	@Setting("TeleportHere")
	private CommandSettings teleporthere = new CommandSettings(new String[] {"tphere"});
	@Setting("TeleportHereAll")
	private CommandSettings teleporthereall = new CommandSettings(new String[] {"tphereall"});
	@Setting("RandomTeleport")
	private CommandSettings rtp = new CommandSettings(300, new String[] {"randomtp", "rtp"});
	@Setting("Tppos")
	private CommandSettings tppos = new CommandSettings();
	@Setting("TpToggle")
	private CommandSettings tptoggle = new CommandSettings();
	@Setting("Clearinventory")
	private CommandSettings clearinventory = new CommandSettings(new String[] {"clear"});
	@Setting("Repair")
	private CommandSettings repair = new CommandSettings(new CommandPrice("$", 20), new String[] {"fix"});
	@Setting("Enderchest")
	private CommandSettings enderchest = new CommandSettings();
	@Setting("InventorySee")
	private CommandSettings inventorysee = new CommandSettings(new String[] {"invsee"});
	@Setting("Top")
	private CommandSettings top = new CommandSettings();
	@Setting("Jump")
	private CommandSettings jump = new CommandSettings();
	@Setting("Back")
	private CommandSettings back = new CommandSettings(new DelayData(15));
	@Setting("Fly")
	private CommandSettings fly = new CommandSettings(new DelayData(5));
	@Setting("GodMode")
	private CommandSettings godMode = new CommandSettings(new DelayData(5), new String[] {"god"});
	@Setting("Speed")
	private CommandSettings speed = new CommandSettings();
	@Setting("Disposal")
	private CommandSettings disposal = new CommandSettings(new String[] {"trash"});
	@Setting("GameMode")
	private CommandSettings gamemode = new CommandSettings(new String[] {"gm"});
	@Setting("Creative")
	private CommandSettings creative = new CommandSettings(new String[] {"gmc", "gm1"});
	@Setting("Spectator")
	private CommandSettings spectator = new CommandSettings(new String[] {"gmsp", "gm3"});
	@Setting("Survival")
	private CommandSettings survival = new CommandSettings(new String[] {"gms", "gm0"});
	@Setting("Adventure")
	private CommandSettings adventure = new CommandSettings(new String[] {"gma", "gm2"});
	@Setting("Weather")
	private CommandSettings weather = new CommandSettings(120);
	@Setting("Sun")
	private CommandSettings sun = new CommandSettings(120);
	@Setting("Rain")
	private CommandSettings rain = new CommandSettings(120);
	@Setting("Thunder")
	private CommandSettings thunder = new CommandSettings(120, new String[] {"storm"});
	@Setting("ServerTime")
	private CommandSettings time = new CommandSettings(120);
	@Setting("Morning")
	private CommandSettings morning = new CommandSettings(120);
	@Setting("Day")
	private CommandSettings day = new CommandSettings(120);
	@Setting("Evening")
	private CommandSettings evening = new CommandSettings(120);
	@Setting("Night")
	private CommandSettings night = new CommandSettings(120);
	@Setting("Enchant")
	private CommandSettings enchant = new CommandSettings();
	@Setting("Anvil")
	private CommandSettings anvil = new CommandSettings();
	@Setting("CraftingTable")
	private CommandSettings craftingTable = new CommandSettings(new String[] {"craft", "ct", "workbench"});
	@Setting("EnchantmentTable")
	private CommandSettings enchantmenttable = new CommandSettings();
	@Setting("Backpack")
	private CommandSettings backpack = new CommandSettings();
	@Setting("Feed")
	private CommandSettings feed = new CommandSettings(new String[] {"food", "eat"});
	@Setting("Heal")
	private CommandSettings heal = new CommandSettings();
	@Setting("Broadcast")
	private CommandSettings broadcast = new CommandSettings();
	@Setting("Sudo")
	private CommandSettings sudo = new CommandSettings();
	@Setting("Vanish")
	private CommandSettings vanish = new CommandSettings();
	@Setting("Nick")
	private CommandSettings nick = new CommandSettings(new String[] {"name"});
	@Setting("Item")
	private CommandSettings item = new CommandSettings();
	@Setting("ServerStat")
	private CommandSettings serverStat = new CommandSettings(new String[] {"serverinfo", "gc"});
	@Setting("Plugins")
	private CommandSettings plugins = new CommandSettings();
	@Setting("Mods")
	private CommandSettings mods = new CommandSettings();
	@Setting("Tps")
	private CommandSettings tps = new CommandSettings();
	@Setting("ServerTime")
	private CommandSettings serverTime = new CommandSettings();
	@Setting("Kits")
	private CommandSettings kits = new CommandSettings();
	@Setting("Kit")
	private CommandSettings kit = new CommandSettings();
	@Setting("Afk")
	private CommandSettings afk = new CommandSettings();
	@Setting("World")
	private CommandSettings world = new CommandSettings();

	public CommandSettings getCommandConfig(String command) {
		return map.getOrDefault(command.toLowerCase(), map.values().stream().filter(config -> (config.getAliasesList().contains(command))).findFirst().orElse(CommandSettings.EMPTY));
	}

	public Optional<CommandSettings> getOptCommandSettings(String command) {
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
						if(settings.isEnable()) command.register(event);
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

	private CommandSettings getCommandFromNode(CommentedConfigurationNode node) {
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
		if(plugin.isForgeServer() && plugin.getPluginContainer() instanceof FMLModContainer) {
			FMLModContainer container = (FMLModContainer) plugin.getPluginContainer();
			ModFileInfo modFileInfo = (ModFileInfo) container.getModInfo().getOwningFile();
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
					if((found.getSuperclass() != null && found.getSuperclass() == clazz) || (found.getSuperclass().getSuperclass() != null && found.getSuperclass().getSuperclass() == clazz)) allClasses.add((Class<T>) found);
				} catch (final ClassNotFoundException | StringIndexOutOfBoundsException ignored) {
				}
			});
		}
		return allClasses;
	}

	private void registerParameterizedCommand(RegisterCommandEvent<Parameterized> event, CommandPack plugin, CommandSettings settings, AbstractParameterizedCommand command) {
		if(!settings.isEnable() || (serverStat.isEnable() && (command.command().equalsIgnoreCase("mods") || command.command().equalsIgnoreCase("plugins") || command.command().equalsIgnoreCase("tps") || command.command().equalsIgnoreCase("servertime")))) return;
		command.register(event);
	}

}
