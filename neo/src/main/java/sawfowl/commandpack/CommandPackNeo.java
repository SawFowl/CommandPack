package sawfowl.commandpack;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;

import org.spongepowered.api.Sponge;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import net.neoforged.fml.common.Mod;

@Mod("commandpack")
public class CommandPackNeo {

	private Path serverPath, configDir;
	private File spongeConfig;
	private CommandPackInstance instance;
	public CommandPackNeo() {
		serverPath = Path.of("", new String[0]);
		spongeConfig = serverPath.resolve("config" + File.separator + "sponge" + File.separator + "sponge.conf").toFile();
		if(spongeConfig.exists()) {
			var loader = HoconConfigurationLoader.builder().file(spongeConfig).build();
			try {
				var node = loader.load();
				var spongeConfigDir = node.node("general", "plugin-config-dir").getString();
				configDir = serverPath.resolve(spongeConfigDir.replace("${CONFIG_DIR}", "config").replace("/", File.separator));
			} catch (ConfigurateException e) {
				configDir = serverPath.resolve("config" + File.separator + "commandpack");
				e.printStackTrace();
			}
		} else configDir = serverPath.resolve("config" + File.separator + "commandpack");
		var plugin = Sponge.pluginManager().plugin("commandpack").get();
		instance = new CommandPackInstance(plugin, configDir);
		Sponge.eventManager().registerListeners(plugin, instance, MethodHandles.lookup());
	}

}
