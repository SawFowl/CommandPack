package sawfowl.commandpack.apiclasses.economy.storage;

import java.sql.Connection;
import java.sql.SQLException;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.apiclasses.economy.EconomyServiceImpl;

public class MySqlStorage extends SqlStorage {

	public MySqlStorage(CommandPack plugin, EconomyServiceImpl cp_economyService) {
		super(plugin, cp_economyService);
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

}
