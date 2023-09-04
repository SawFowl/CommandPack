package sawfowl.commandpack.api.data.player;

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

}
