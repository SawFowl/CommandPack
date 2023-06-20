package sawfowl.commandpack.configure.configs.punishment;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.utils.StorageType;

@ConfigSerializable
public class Punishment {

	public Punishment(){}

	@Setting("Enable")
	private boolean enable = false;
	@Setting("StorageType")
	private String storageType = StorageType.FILE.typeName();
	@Setting("Announce")
	private Announce announce = new Announce();

	public boolean isEnable() {
		return enable;
	}

	public StorageType getStorageType() {
		return StorageType.getType(storageType);
	}

	public Announce getAnnounce() {
		return announce;
	}

}
