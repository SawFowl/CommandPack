package sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig;

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
	private String storageType = "Допустимые значения: File, H2, MySql.";
	@Setting("Announce")
	private String announce = "Включение и отключение оповещения о наказаниях игроков.";
	@Setting("DateTimeFormat")
	private String dateTimeFormat = "Не меняйте без необходимости.";
	@Setting("TimeZone")
	private String timeZone = "Установить часовой пояс.\nДоступные варианты можно посмотреть по ссылке - https://gist.github.com/SawFowl/12dc8342e14bce41f95411f833d911f4";

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
