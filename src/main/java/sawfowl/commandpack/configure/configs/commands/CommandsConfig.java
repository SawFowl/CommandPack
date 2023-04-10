package sawfowl.commandpack.configure.configs.commands;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
			Enumeration<URL> resources = plugin.getClass().getClassLoader().getResources("sawfowl.commandpack.commands.parameterized".replace(".", File.separator));
			List<File> dirs = new ArrayList<File>();
			plugin.getLogger().warn("Проверка поиска классов по указанному пути = " + resources.hasMoreElements());
			while(resources.hasMoreElements()) {
				dirs.add(new File(resources.nextElement().getFile()));
			}
			plugin.getLogger().warn("Найдено файлов и каталогов => " + dirs.size());
			for(File directory : dirs) {
				for(Class<? extends AbstractParameterizedCommand> clazz : findParameterizedClasses(directory, "sawfowl.commandpack.commands.parameterized")) {
					AbstractParameterizedCommand command = clazz.getDeclaredConstructor().newInstance(plugin);
					if(getOptCommandSettings(command.command()).isPresent()) command.register(event);
				}
			}
		} catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

	public void registerRaw(RegisterCommandEvent<Raw> event, CommandPack plugin) {
		try {
			Enumeration<URL> resources = plugin.getClass().getClassLoader().getResources("sawfowl.commandpack.commands.raw".replace(".", File.separator));
			List<File> dirs = new ArrayList<File>();
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				dirs.add(new File(resource.getFile()));
			}
			for(File directory : dirs) {
				for(Class<? extends AbstractRawCommand> clazz : findRawClasses(directory, "sawfowl.commandpack.commands.raw")) {
					AbstractRawCommand command = clazz.getDeclaredConstructor().newInstance(plugin);
					if(getOptCommandSettings(command.command()).isPresent()) command.register(event);
				}
			}
		} catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
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
	private static List<Class<? extends AbstractParameterizedCommand>> findParameterizedClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class<? extends AbstractParameterizedCommand>> classes = new ArrayList<Class<? extends AbstractParameterizedCommand>>();
		if (!directory.exists()) {
			System.out.println("Найдено классов => 0");
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findParameterizedClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				Class<?> clazz = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
				if(clazz.isInstance(AbstractParameterizedCommand.class)) classes.add((Class<? extends AbstractParameterizedCommand>) clazz);
			}
		}
		System.out.println("Найдено классов => " + classes.size());
		return classes;
	}

	@SuppressWarnings("unchecked")
	private static List<Class<? extends AbstractRawCommand>> findRawClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class<? extends AbstractRawCommand>> classes = new ArrayList<Class<? extends AbstractRawCommand>>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findRawClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				Class<?> clazz = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
				if(clazz.isInstance(AbstractRawCommand.class)) classes.add((Class<? extends AbstractRawCommand>) clazz);
			}
		}
		return classes;
	}

}
