package sawfowl.commandpack.configure.configs.punishment;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class SyncIntervals {

	public SyncIntervals(){}

	@Setting("Delete")
	private int delete = 60;
	@Setting("FindNew")
	private int findNew = 15;

	public int getDelete() {
		return delete;
	}

	public int getFindNew() {
		return findNew;
	}

}
