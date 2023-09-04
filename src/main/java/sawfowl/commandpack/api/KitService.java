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

	/**
	 * Adding a kit.<br>
	 * The kit will be saved to the CommandPack vault.
	 */
	boolean addKit(Kit kit);

	/**
	 * Removing a kit.
	 */
	boolean removeKit(String id);

	/**
	 * Removing a kit.
	 */
	boolean removeKit(Kit kit);

	/**
	 * Checks if a set with the specified id exists.
	 */
	boolean kitExist(String id);

	/**
	 * Getting the kit.
	 */
	Optional<Kit> getKit(String id);

	/**
	 * Getting a copy of a collection of kits.
	 */
	Collection<Kit> getKits();

}
