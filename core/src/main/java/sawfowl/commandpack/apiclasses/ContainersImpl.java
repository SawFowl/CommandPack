package sawfowl.commandpack.apiclasses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.plugin.PluginContainer;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.ContainersCollection;
import sawfowl.commandpack.api.data.miscellaneous.ModContainer;

public class ContainersImpl implements ContainersCollection {

	private CommandPackInstance plugin;
	private final Collection<PluginContainer> plugins;;
	private final Collection<ModContainer> allMods;
	private final Collection<ModContainer> mods;
	public ContainersImpl(CommandPackInstance plugin) {
		this.plugin = plugin;
		allMods = findMods();
		mods = allMods.stream().filter(mod -> !mod.getLoaders().stream().filter(loader -> loader.equalsIgnoreCase("java_plain")).findFirst().isPresent()).toList();
		plugins = findPlugins();
	}

	@Override
	public Collection<PluginContainer> getPlugins() {
		return plugins;
	}

	@Override
	public Collection<ModContainer> getMods() {
		return mods;
	}

	@Override
	public Optional<ModContainer> getPluginAsMod(PluginContainer container) {
		return plugin.isModifiedServer()
			?
			allMods.stream().filter(mod -> mod.getModId().equals(container.metadata().id())).findFirst()
			:
			Optional.empty();
	}

	private List<ModContainer> findMods() {
		return new ArrayList<ModContainer>();
	}

	private Collection<PluginContainer> findPlugins() {
		return plugin.isModifiedServer()
			?
			Collections.unmodifiableCollection(new ArrayList<>(Sponge.pluginManager().plugins().stream().filter(container -> (!mods.stream().filter(mod -> mod.getModId().equals(container.metadata().id())).findFirst().isPresent())).toList()))
			:
			Sponge.pluginManager().plugins();
	}

}
