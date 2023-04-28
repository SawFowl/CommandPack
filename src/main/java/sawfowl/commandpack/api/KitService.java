package sawfowl.commandpack.api;

import java.util.Collection;
import java.util.Optional;

import sawfowl.commandpack.api.data.kits.Kit;

public interface KitService {

	boolean addKit(Kit kit);

	boolean removeKit(String id);

	boolean removeKit(Kit kit);

	Optional<Kit> getKit(String id);

	Collection<Kit> getKits();

}
