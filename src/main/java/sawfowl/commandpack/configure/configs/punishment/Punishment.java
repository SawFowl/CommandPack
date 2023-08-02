package sawfowl.commandpack.configure.configs.punishment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.spongepowered.api.util.locale.Locales;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.utils.StorageType;

@ConfigSerializable
public class Punishment {

	public Punishment(){}

	@Setting("Enable")
	private boolean enable = false;
	@Setting("StorageType")
	@Comment("Available values: File, H2, MySql.")
	private String storageType = StorageType.FILE.typeName();
	@Setting("Announce")
	private Announce announce = new Announce();
	@Setting("WarnsBefore")
	private WarnsBefore warnsBefore = new WarnsBefore();
	@Setting("MySqlQueries")
	@Comment("Configuring queries to work with the MySQL database.\nChanging the settings can help in creating compatibility with other plugins.\nDo not remove the `written` column from queries. It is used for automatic data synchronization.")
	private Queries mySqlQueries = new Queries();
	@Setting("DateTimeFormat")
	@Comment("Don't change unnecessarily.")
	private String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

	public boolean isEnable() {
		return enable;
	}

	public StorageType getStorageType() {
		return StorageType.getType(storageType);
	}

	public Announce getAnnounce() {
		return announce;
	}

	public WarnsBefore getWarnsBefore() {
		return warnsBefore;
	}

	public Queries getMySqlQueries() {
		return mySqlQueries;
	}

	public DateFormat createDateTimeFormat() {
		return new SimpleDateFormat(dateTimeFormat, Locales.DEFAULT);
	}

	public String getDateTimeFormat() {
		return dateTimeFormat;
	}

}
