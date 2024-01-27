package sawfowl.commandpack.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.spongepowered.api.Sponge;
import org.spongepowered.plugin.PluginContainer;

import net.minecraftforge.fml.loading.FMLLoader;

import sawfowl.commandpack.api.data.miscellaneous.ModContainer;
import sawfowl.commandpack.apiclasses.ModContainerImpl;

public class ModListGetter {

	public static Collection<ModContainer> getMods() {
		Collection<ModContainer> mods = new ArrayList<ModContainer>();
		FMLLoader.getLoadingModList().getMods().forEach(mod -> {
			if(!mod.getOwningFile().getFile().getLoaders().stream().filter(loader -> loader.name().equalsIgnoreCase("java_plain")).findFirst().isPresent()) mods.add(new ModContainerImpl(mod));
		});
		return Collections.unmodifiableCollection(new ArrayList<>(mods));
	}

	public static Collection<PluginContainer> getPlugins(Collection<ModContainer> mods) {
		if(mods.isEmpty()) return Sponge.pluginManager().plugins();
		return Collections.unmodifiableCollection(new ArrayList<>(Sponge.pluginManager().plugins().stream().filter(container -> (!mods.stream().filter(mod -> mod.getModId().equals(container.metadata().id())).findFirst().isPresent())).toList()));
	}

}
