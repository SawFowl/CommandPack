package sawfowl.commandpack.api;

import java.util.Collection;
import java.util.Optional;

import sawfowl.commandpack.api.data.kits.Kit;

/**
 * Kits API.
 * 
 * @author SawFowl
 */
public interface KitService {

	boolean addKit(Kit kit);

	boolean removeKit(String id);

	boolean removeKit(Kit kit);

	boolean kitExist(String id);

	Optional<Kit> getKit(String id);

	Collection<Kit> getKits();

}
