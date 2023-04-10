package sawfowl.commandpack.configure.configs.commands;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.Command.Raw;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.reference.ValueReference;
import org.spongepowered.configurate.serialize.SerializationException;

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
	private CommandSettings home = new CommandSettings(new Delay(3));
	@Setting("SetHome")
	private CommandSettings setHome = new CommandSettings();
	@Setting("SetSpawn")
	private CommandSettings setSpawn = new CommandSettings();
	@Setting("Spawn")
	private CommandSettings spawn = new CommandSettings(new Delay(3));
	@Setting("SetWarp")
	private CommandSettings setWarp = new CommandSettings();
	@Setting("Warp")
	private CommandSettings warp = new CommandSettings(15, new Delay(3));
	@Setting("Warps")
	private CommandSettings warps = new CommandSettings(new Delay(3));
	@Setting("Tpa")
	private CommandSettings tpa = new CommandSettings(new Delay(3), new CommandPrice("$", 5));
	@Setting("Tpahere")
	private CommandSettings tpahere = new CommandSettings(new Delay(3), new CommandPrice("$", 10));
	@Setting("Tpahereall")
	private CommandSettings tpahereall = new CommandSettings(new Delay(3));
	@Setting("Teleport")
	private CommandSettings teleport = new CommandSettings(new String[] {"tp"});
	@Setting("TeleportHere")
	private CommandSettings teleporthere = new CommandSettings(new String[] {"tphere"});
	@Setting("TeleportHereAll")
	private CommandSettings teleporthereall = new CommandSettings(new String[] {"tphereall"});
	@Setting("RandomTeleport")
	private CommandSettings rtp = new CommandSettings(new String[] {"randomtp", "rtp"});
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
	@Setting("Fly")
	private CommandSettings fly = new CommandSettings(new Delay(5));
	@Setting("GodMode")
	private CommandSettings godMode = new CommandSettings(new Delay(5), new String[] {"god"});

	public CommandSettings getCommandConfig(String command) {
		return map.getOrDefault(command.toLowerCase(), map.values().stream().filter(config -> (config.getAliasesList().contains(command))).findFirst().orElse(CommandSettings.EMPTY));
	}

	public Optional<CommandSettings> getOptCommandSettings(String command) {
		return Optional.ofNullable(map.getOrDefault(command, null));
	}

	public void registerParameterized(RegisterCommandEvent<Parameterized> event, CommandPack plugin) {
		try {
			for(Class<AbstractParameterizedCommand> clazz : findAllCommandsClasses(plugin, "sawfowl.commandpack.commands.parameterized", AbstractParameterizedCommand.class)) {
				Constructor<AbstractParameterizedCommand> constructor = clazz.getConstructor(CommandPack.class);
				try {
					AbstractParameterizedCommand command = constructor.newInstance(plugin);
					getOptCommandSettings(command.command()).ifPresent(settings -> {
						if(settings.isEnable()) command.register(event);
					});
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					plugin.getLogger().error("Error when registering a command class '" + clazz.getName() +"'\n" + e.getLocalizedMessage());
				}
			}
		} catch (IOException | NoSuchMethodException | SecurityException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void registerRaw(RegisterCommandEvent<Raw> event, CommandPack plugin) {
		try {
			for(Class<AbstractRawCommand> clazz : findAllCommandsClasses(plugin, "sawfowl.commandpack.commands.raw", AbstractRawCommand.class)) {
				Constructor<AbstractRawCommand> constructor = clazz.getConstructor(CommandPack.class);
				try {
					AbstractRawCommand command = constructor.newInstance(plugin);
					getOptCommandSettings(command.command()).ifPresent(settings -> {
						if(settings.isEnable()) command.register(event);
					});
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					plugin.getLogger().error("Error when registering a command class '" + clazz.getName() +"'\n" + e.getLocalizedMessage());
				}
			}
		} catch (IOException | NoSuchMethodException | SecurityException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void updateCommandMap(ValueReference<CommandsConfig, CommentedConfigurationNode> reference) {
		map.clear();
		reference.node().childrenMap().entrySet().forEach(entry -> {
			CommandSettings settings = getCommandFromNode(entry.getValue());
			if(settings.isEnable()) map.put(entry.getKey().toString().toLowerCase(), getCommandFromNode(entry.getValue()));
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
	private <T> ArrayList<Class<T>> findAllCommandsClasses(CommandPack plugin, String packageName, Class<T> clazz) throws IOException, URISyntaxException {
		final String pkgPath = packageName.replace('.', '/');
		final URI pkg = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(pkgPath)).toURI();
		final ArrayList<Class<T>> allClasses = new ArrayList<Class<T>>();
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
					Class<?> fined = Class.forName(name);
					if(fined.getSuperclass().getName().equals(clazz.getName()) || fined.getSuperclass().getSuperclass().getName().equals(clazz.getName())) allClasses.add((Class<T>) fined);
				} catch (final ClassNotFoundException | StringIndexOutOfBoundsException ignored) {
				}
			});
		}
		return allClasses;
	}

}
