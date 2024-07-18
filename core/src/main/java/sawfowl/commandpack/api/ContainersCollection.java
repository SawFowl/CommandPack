package sawfowl.commandpack.api;

import java.util.Collection;
import java.util.Optional;

import org.spongepowered.plugin.PluginContainer;

import sawfowl.commandpack.api.data.miscellaneous.ModContainer;

public interface ContainersCollection {

	/**
	 * This method returns only plugins.
	 */
	Collection<PluginContainer> getPlugins();

	/**
	 * This method only returns a list of mods.
	 */
	Collection<ModContainer> getMods();

	/**
	 * This method can be used to get the plugin container as a container of mod information.<br>
	 * It can be useful if you need to access the plugin file on the modified server for some purpose.
	 */
	Optional<ModContainer> getPluginAsMod(PluginContainer container);

}
