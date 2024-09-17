package sawfowl.commandpack.configure.configs.player;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.api.data.player.GivedKit;

@ConfigSerializable
public class GivedKitData implements GivedKit {

	public GivedKitData() {}

	public GivedKitData(long lastGivedTime, int givedCount) {
		this.lastGivedTime = lastGivedTime;
		this.givedCount = givedCount;
	}

	@Setting("LastGivedTime")
	private long lastGivedTime = 0;
	@Setting("GivedCount")
	private int givedCount = 0;

	@Override
	public long getLastGivedTime() {
		return lastGivedTime;
	}

	@Override
	public int getGivedCount() {
		return givedCount;
	}

	@Override
	public void setLastGivedTime(long value) {
		this.lastGivedTime = value;
	}

	@Override
	public void setGivedCount(int value) {
		this.givedCount = value;
	}

}
