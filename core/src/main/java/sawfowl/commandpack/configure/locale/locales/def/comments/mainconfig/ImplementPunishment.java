package sawfowl.commandpack.configure.locale.locales.def.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Punishment;

@ConfigSerializable
public class ImplementPunishment implements Punishment {

	public ImplementPunishment() {}

	@Setting("DBSettings")
	private ImplementDBSettings dbSettings = new ImplementDBSettings();
	@Setting("WarnsBefore")
	private ImplementWarnsBefore warnsBefore = new ImplementWarnsBefore();
	@Setting("StorageType")
	private String storageType = "Available values: File, H2, MySql.";
	@Setting("Announce")
	private String announce = "Enabling and disabling the annoncing of player punishments.";
	@Setting("DateTimeFormat")
	private String dateTimeFormat = "Don't change unnecessarily.";
	@Setting("TimeZone")
	private String timeZone = "Set your date time zone.\nAvailable options can be viewed at the link - https://gist.github.com/SawFowl/12dc8342e14bce41f95411f833d911f4";

	@Override
	public DBSettings getDBSettings() {
		return dbSettings;
	}

	@Override
	public WarnsBefore getWarnsBefore() {
		return warnsBefore;
	}

	@Override
	public String getStorageType() {
		return storageType;
	}

	@Override
	public String getAnnounce() {
		return announce;
	}

	@Override
	public String getDateTimeFormat() {
		return dateTimeFormat;
	}

	@Override
	public String getTimeZone() {
		return timeZone;
	}

}
