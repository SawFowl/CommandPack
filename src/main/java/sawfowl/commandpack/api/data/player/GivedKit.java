package sawfowl.commandpack.api.data.player;

/**
 * @author SawFowl
 */
public interface GivedKit {

	long getLastGivedTime();

	int getGivedCount();

	void setLastGivedTime(long value);

	void setGivedCount(int value);

}
