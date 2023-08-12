package sawfowl.commandpack.apiclasses.economy.storage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.apiclasses.economy.EconomyServiceImpl;

public class H2Storage extends SqlStorage {

	public H2Storage(CommandPack plugin, EconomyServiceImpl economyService) {
		super(plugin, economyService);
	}

	@Override
	protected Connection getConnection() throws SQLException {
		if(!plugin.getConfigDir().resolve("Modules").toFile().exists()) plugin.getConfigDir().resolve("Modules").toFile().mkdir();
		return DriverManager.getConnection("jdbc:h2:" + plugin.getConfigDir().resolve("Modules" + File.separator + "Economy").toFile().getAbsolutePath(), "", "");
	}

	@Override
	protected String createUniqueAccountsTable() {
		return "CREATE TABLE IF NOT EXISTS ECONOMY_UNIQUE_ACCOUNTS(UUID VARCHAR(128) NOT NULL UNIQUE, ACCOUNT_DATA LONGTEXT, PRIMARY KEY(UUID))";
	}

	@Override
	protected String createAccountsTable() {
		return "CREATE TABLE IF NOT EXISTS ECONOMY_ACCOUNTS(IDENTIFIER VARCHAR(128) NOT NULL UNIQUE, ACCOUNT_DATA LONGTEXT, PRIMARY KEY(IDENTIFIER))";
	}

	@Override
	protected String loadAllUniqueAccounts() {
		return "SELECT * FROM ECONOMY_UNIQUE_ACCOUNTS";
	}

	@Override
	protected String loadAllAccounts() {
		return "SELECT * FROM ECONOMY_ACCOUNTS";
	}

	@Override
	protected String removeUniqueAccount() {
		return "DELETE FROM ECONOMY_UNIQUE_ACCOUNTS WHERE UUID = ?";
	}

	@Override
	protected String removeAccount() {
		return "DELETE FROM ECONOMY_ACCOUNTS WHERE IDENTIFIER = ?";
	}

	@Override
	protected String insertUniqueAccount() {
		return "MERGE INTO ECONOMY_UNIQUE_ACCOUNTS (UUID, ACCOUNT_DATA) VALUES(?, ?)";
	}

	@Override
	protected String insertAccount() {
		return "MERGE INTO ECONOMY_ACCOUNTS (IDENTIFIER, ACCOUNT_DATA) VALUES(?, ?)";
	}

	@Override
	protected String uuidCollumn() {
		return "UUID";
	}

	@Override
	protected String identifierCollumn() {
		return "IDENTIFIER";
	}

	@Override
	protected String dataCollumn() {
		return "ACCOUNT_DATA";
	}

}
