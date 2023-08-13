package sawfowl.commandpack.apiclasses.economy.storage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.apiclasses.economy.CPAccount;
import sawfowl.commandpack.apiclasses.economy.CPUniqueAccount;
import sawfowl.commandpack.apiclasses.economy.EconomyServiceImpl;

public class MySqlStorage extends SqlStorage {

	private String lastUpdateUniqueAccounts;
	private String lastUpdateAccounts;
	private String writtenCollumn = "written";
	public MySqlStorage(CommandPack plugin, EconomyServiceImpl cp_economyService) {
		super(plugin, cp_economyService);
		Sponge.asyncScheduler().submit(Task.builder().plugin(plugin.getPluginContainer()).interval(plugin.getMainConfig().getEconomy().getUpdateInterval(), TimeUnit.MILLISECONDS).execute(() -> {
			sync();
		}).build());
	}

	@Override
	protected Connection getConnection() throws SQLException {
		return plugin.getMariaDB().get().createNewConnection();
	}

	@Override
	protected String createUniqueAccountsTable() {
		return "CREATE TABLE IF NOT EXISTS cp_economy_unique_accounts(uuid VARCHAR(128) UNIQUE, account_data LONGTEXT, written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY(uuid))";
	}

	@Override
	protected String createAccountsTable() {
		return "CREATE TABLE IF NOT EXISTS cp_economy_accounts(identifier VARCHAR(128) UNIQUE, account_data LONGTEXT, written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY(identifier))";
	}

	@Override
	protected String loadAllUniqueAccounts() {
		return "SELECT * FROM cp_economy_unique_accounts";
	}

	@Override
	protected String loadAllAccounts() {
		return "SELECT * FROM cp_economy_accounts";
	}

	@Override
	protected String removeUniqueAccount() {
		return "DELETE FROM cp_economy_unique_accounts WHERE uuid = ?";
	}

	@Override
	protected String removeAccount() {
		return "DELETE FROM cp_economy_accounts WHERE identifier = ?";
	}

	@Override
	protected String insertUniqueAccount() {
		return "REPLACE INTO cp_economy_unique_accounts (uuid, account_data) VALUES(?, ?)";
	}

	@Override
	protected String insertAccount() {
		return "REPLACE INTO cp_economy_accounts (identifier, account_data) VALUES(?, ?)";
	}

	@Override
	protected String uuidCollumn() {
		return "uuid";
	}

	@Override
	protected String identifierCollumn() {
		return "identifier";
	}

	@Override
	protected String dataCollumn() {
		return "account_data";
	}

	private void sync() {
		try {
			syncUniqueAccounts(resultSet(lastUpdateUniqueAccounts == null ? "SELECT * FROM cp_economy_unique_accounts ORDER BY written DESC LIMIT 1" : "SELECT * FROM cp_economy_unique_accounts WHERE written > '" + lastUpdateUniqueAccounts + "' ORDER BY written;"));
			syncAccounts(resultSet(lastUpdateAccounts == null ? "SELECT * FROM cp_economy_accounts ORDER BY written DESC LIMIT 1" : "SELECT * FROM cp_economy_accounts WHERE written > '" + lastUpdateAccounts + "' ORDER BY written;"));
		} catch (SQLException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
		}
	}

	private void syncUniqueAccounts(ResultSet resultSet) throws SQLException {
		boolean timeUpdate = true;
		while(resultSet.next()) {
			if(timeUpdate) {
				lastUpdateUniqueAccounts = resultSet.getString(writtenCollumn);
				timeUpdate = false;
			}
			UUID uuid = UUID.fromString(resultSet.getString(uuidCollumn));
			CPUniqueAccount account = uniqueAccountFromString(resultSet.getString(dataCollumn));
			if(uniqueAccounts.containsKey(uuid)) {
				uniqueAccounts.replace(uuid, account);
			} else uniqueAccounts.put(uuid, account);
		}
	}

	private void syncAccounts(ResultSet resultSet) throws SQLException {
		boolean timeUpdate = true;
		while(resultSet.next()) {
			if(timeUpdate) {
				lastUpdateAccounts = resultSet.getString(writtenCollumn);
				timeUpdate = false;
			}
			String identifier = resultSet.getString(identifierCollumn);
			CPAccount account = accountFromString(resultSet.getString(dataCollumn));
			if(accounts.containsKey(identifier)) {
				accounts.replace(identifier, account);
			} else accounts.put(identifier, account);
		}
	}

}
