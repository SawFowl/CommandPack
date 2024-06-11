package sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Punishment.DBSettings;

@ConfigSerializable
public class ImplementDBSettings implements DBSettings {

	public ImplementDBSettings() {}

	@Setting("Title")
	private String title = "Настройка работы с базой данных MySql";
	@Setting("Columns")
	private String columns = "Имена столбцов для получения данных.\nЕсли вы измените имена, вы должны удалить таблицы в существующей базе данных или вручную переименовать столбцы в таблицах `ban` и `mute`.";
	@Setting("Tables")
	private String tables = "Имена таблиц для автоматической синхронизации данных.";
	@Setting("SyncIntervals")
	private String syncIntervals = "Интервалы между обновлениями данных плагина.\\nВремя указывается в секундах.";
	@Setting("CreateCombinedBansTable")
	private String createCombinedBansTable = "Создание комбинированной таблицы с данными о банах.\nНе рекомендуется.\nИспользуйте, если вам нужно работать с существующей базой данных другого плагина. Совместимость не гарантируется.\nПри использовании комбинированной таблицы, удаление бана игрока, который был забанен по IP, удалит и бан по IP-адресу. Аналогично с удалением бана по IP.";
	@Setting("UnixTime")
	private String unixTime = "Использование времени unix в запросах к базе данных.\nЕсли вы измените формат времени, вам, возможно, придется удалить таблицы.\nИспользуйте только для обеспечения совместимости с другими плагинами.";

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getColumns() {
		return columns;
	}

	@Override
	public String getTables() {
		return tables;
	}

	@Override
	public String getSyncIntervals() {
		return syncIntervals;
	}

	@Override
	public String getCreateCombinedBansTable() {
		return createCombinedBansTable;
	}

	@Override
	public String getUnixTime() {
		return unixTime;
	}

}
