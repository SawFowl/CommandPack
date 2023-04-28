package sawfowl.commandpack.apiclasses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import sawfowl.commandpack.api.KitService;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.localeapi.api.TextUtils;

public class KitServiceImpl implements KitService {

	private Map<String, Kit> kits = new HashMap<>();

	@Override
	public boolean addKit(Kit kit) {
		if(kits.containsKey(TextUtils.clearDecorations(kit.id()))) return false;
		kits.put(TextUtils.clearDecorations(kit.id()), kit);
		kit.save();
		return true;
	}

	@Override
	public boolean removeKit(String id) {
		if(!kits.containsKey(TextUtils.clearDecorations(id))) return false;
		kits.remove(TextUtils.clearDecorations(id));
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
	public Collection<Kit> getKits() {
		return new ArrayList<>(kits.values());
	}

}
