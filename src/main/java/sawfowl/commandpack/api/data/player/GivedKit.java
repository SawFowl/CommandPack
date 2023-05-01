package sawfowl.commandpack.api.data.player;

public interface GivedKit {

	long getLastGivedTime();

	int getGivedCount();

	void setLastGivedTime(long value);

	void setGivedCount(int value);

}
