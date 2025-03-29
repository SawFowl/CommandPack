package sawfowl.commandpack.api.data.player;

import com.google.gson.JsonObject;

import sawfowl.commandpack.configure.configs.player.GivedKitData;

/**
 * @author SawFowl
 */
public interface GivedKit {

	/**
	 * The time when the kit was last given to the player.
	 */
	long getLastGivedTime();

	/**
	 * How many times a kit has been given to a player.
	 */
	int getGivedCount();

	/**
	 * Set the time of the last time a kit was given to a player.
	 */
	void setLastGivedTime(long value);

	/**
	 * Set how many times a kit has been given to a player.
	 */
	void setGivedCount(int value);

	/**
	 * Convert to Json.
	 */
	JsonObject asJson();

	/**
	 * Set the time of the last time a kit was given to a player.
	 * Set how many times a kit has been given to a player.
	 * @return 
	 */
	GivedKitData updateFromJson(JsonObject json);

}
