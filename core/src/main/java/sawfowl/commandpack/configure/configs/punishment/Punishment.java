package sawfowl.commandpack.configure.configs.punishment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.spongepowered.api.util.locale.Locales;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.utils.StorageType;
import sawfowl.localeapi.api.LocalisedComment;

@ConfigSerializable
public class Punishment {

	public Punishment(){}

	@Setting("Enable")
	private boolean enable = false;
	@Setting("StorageType")
	@LocalisedComment(path = {"Comments", "MainConfig", "Punishment", "StorageType"}, plugin = "commandpack")
	private String storageType = StorageType.FILE.typeName();
	@Setting("Announce")
	private Announce announce = new Announce();
	@Setting("WarnsBefore")
	@LocalisedComment(path = {"Comments", "MainConfig", "Punishment", "WarnsBefore", "Title"}, plugin = "commandpack")
	private WarnsBefore warnsBefore = new WarnsBefore();
	@Setting("DBSettings")
	@LocalisedComment(path = {"Comments", "MainConfig", "Punishment", "DBSettings", "Title"}, plugin = "commandpack")
	private DBSettings dbSettings = new DBSettings();
	@Setting("DateTimeFormat")
	@LocalisedComment(path = {"Comments", "MainConfig", "Punishment", "DateTimeFormat"}, plugin = "commandpack")
	private String dateTimeFormat = CommandPack.getInstance().getLocales().getSystemLocale().getTime().getFormat();
	@Setting("TimeZone")
	@LocalisedComment(path = {"Comments", "MainConfig", "Punishment", "TimeZone"}, plugin = "commandpack")
	private String timeZone = CommandPack.getInstance().getLocales().getSystemLocale().getTime().getTimeZone();

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

	public DBSettings getDBSettings() {
		return dbSettings;
	}

	public DateFormat createDateTimeFormat() {
		return new SimpleDateFormat(dateTimeFormat, Locales.DEFAULT);
	}

	public String getDateTimeFormat() {
		return dateTimeFormat;
	}

	public TimeZone getTimeZone() {
		return TimeZone.getTimeZone(timeZone);
	}

}
