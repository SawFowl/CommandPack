package sawfowl.commandpack.apiclasses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.KitService;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.localeapi.api.TextUtils;

public class KitServiceImpl implements KitService {

	private Map<String, Kit> kits = new HashMap<>();
	private final CommandPack plugin;
	public KitServiceImpl(CommandPack plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean addKit(Kit kit) {
		if(kits.containsKey(TextUtils.clearDecorations(kit.id()))) return false;
		kits.put(TextUtils.clearDecorations(kit.id()), kit);
		plugin.getConfigManager().saveKit(kit);
		return true;
	}

	@Override
	public boolean removeKit(String id) {
		if(!kits.containsKey(TextUtils.clearDecorations(id))) return false;
		kits.remove(TextUtils.clearDecorations(id));
		plugin.getConfigManager().deleteKit(id);
		return true;
	}

	@Override
	public boolean removeKit(Kit kit) {
		return removeKit(kit.id());
	}

	@Override
	public Optional<Kit> getKit(String id) {
		return Optional.ofNullable(kits.getOrDefault(TextUtils.clearDecorations(id), null));
	}

	@Override
	public boolean kitExist(String id) {
		return kits.containsKey(id);
	}

	@Override
	public Collection<Kit> getKits() {
		return new ArrayList<>(kits.values());
	}

}
