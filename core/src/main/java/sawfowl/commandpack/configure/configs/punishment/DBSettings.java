package sawfowl.commandpack.configure.configs.punishment;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.configs.punishment.dbsettings.Columns;
import sawfowl.commandpack.configure.configs.punishment.dbsettings.SyncIntervals;
import sawfowl.commandpack.configure.configs.punishment.dbsettings.Tables;
import sawfowl.localeapi.api.LocalisedComment;

@ConfigSerializable
public class DBSettings {

	public DBSettings(){}

	@Setting("Columns")
	@LocalisedComment(path = {"Comments", "MainConfig", "Punishment", "DBSettings", "Columns"}, plugin = "commandpack")
	private Columns columns = new Columns();
	@Setting("Tables")
	@LocalisedComment(path = {"Comments", "MainConfig", "Punishment", "DBSettings", "Tables"}, plugin = "commandpack")
	private Tables tables = new Tables();
	@Setting("SyncIntervals")
	@LocalisedComment(path = {"Comments", "MainConfig", "Punishment", "DBSettings", "SyncIntervals"}, plugin = "commandpack")
	private SyncIntervals syncIntervals = new SyncIntervals();
	@Setting("CreateCombinedBansTable")
	@LocalisedComment(path = {"Comments", "MainConfig", "Punishment", "DBSettings", "CreateCombinedBansTable"}, plugin = "commandpack")
	private boolean createCombinedBansTable = false;
	@Setting("UnixTime")
	@LocalisedComment(path = {"Comments", "MainConfig", "Punishment", "DBSettings", "UnixTime"}, plugin = "commandpack")
	private boolean unixTime = false;

	public Columns getColumns() {
		return columns;
	}

	public Tables getTables() {
		return tables;
	}

	public SyncIntervals getSyncIntervals() {
		return syncIntervals;
	}

	public boolean isCreateCombinedBansTable() {
		return createCombinedBansTable;
	}

	public boolean isUnixTime() {
		return unixTime;
	}

}
