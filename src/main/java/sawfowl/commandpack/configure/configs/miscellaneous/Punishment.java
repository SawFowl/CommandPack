package sawfowl.commandpack.configure.configs.miscellaneous;

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
	@Setting("AnnounceBan")
	private boolean announceBan = false;
	@Setting("AnnounceBan")
	private boolean announceMute = false;
	@Setting("AnnounceMute")
	private boolean announceKick = false;
	@Setting("AnnounceWarn")
	private boolean announceWarn = false;

	public boolean isEnable() {
		return enable;
	}

	public StorageType getStorageType() {
		return StorageType.getType(storageType);
	}

	public boolean isAnnounceBan() {
		return announceBan;
	}

	public boolean isAnnounceMute() {
		return announceMute;
	}

	public boolean isAnnounceKick() {
		return announceKick;
	}

	public boolean isAnnounceWarn() {
		return announceWarn;
	}

}
