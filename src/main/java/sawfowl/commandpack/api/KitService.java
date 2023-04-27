package sawfowl.commandpack.api;

import java.util.Optional;

import sawfowl.commandpack.api.data.kits.Kit;

public interface KitService {

	boolean addKit(Kit kit);

	boolean remove(String id);

	Optional<Kit> getKit(String id);

}
