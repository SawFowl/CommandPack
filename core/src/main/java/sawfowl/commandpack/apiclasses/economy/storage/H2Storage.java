package sawfowl.commandpack.apiclasses.economy.storage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.apiclasses.economy.EconomyServiceImpl;
import sawfowl.commandpack.configure.configs.economy.SerializedAccount;
import sawfowl.commandpack.configure.configs.economy.SerializedUniqueAccount;
import sawfowl.localeapi.api.ConfigTypes;
import sawfowl.localeapi.api.services.ConfigurationService;

public class H2Storage extends SqlStorage {

	private String createUniqueAccountsTable;
	private String createAccountsTable;
	private String loadAllUniqueAccounts;
	private String loadAllAccounts;
	private String removeUniqueAccount;
	private String removeAccount;
	private String insertUniqueAccount;
	private String insertAccount;
	private String uuidCollumn;
	private String identifierCollumn;
	private String dataCollumn;
	public H2Storage(CommandPackInstance plugin, EconomyServiceImpl economyService) {
		super(plugin, economyService);
	}

	@Override
	protected Connection createConnection() throws SQLException {
		if(!plugin.getConfigDir().resolve("Modules").toFile().exists()) plugin.getConfigDir().resolve("Modules").toFile().mkdir();
		return DriverManager.getConnection("jdbc:h2:" + plugin.getConfigDir().resolve("Modules" + File.separator + "Economy").toFile().getAbsolutePath(), "", "");
	}

	@Override
	public void saveUniqueAccount(UniqueAccount account) {
		try {
			createStatement(insertUniqueAccount, new Object[] {account.uniqueId().toString(), uniqueAccountToString(account)}).execute();
		} catch (SQLException e) {
			plugin.getLogger().warn("Error when saving UniqueAccount'" + account.uniqueId() + "'" + e.getLocalizedMessage());
		}
	}

	@Override
	public void saveAccount(Account account) {
		try {
			createStatement(insertAccount, new Object[] {account.identifier(), accountToString(account)}).execute();
		} catch (SQLException e) {
			plugin.getLogger().warn("Error when saving Account'" + account.identifier() + "'" + e.getLocalizedMessage());
		}
	}

	@Override
	public void removeUniqueAccount(UUID uuid) {
		uniqueAccounts.remove(uuid);
		try {
			createStatement(removeUniqueAccount, new Object[] {uuid}).execute();
		} catch (SQLException e) {
			plugin.getLogger().warn("Error when deleting UniqueAccount '" + uuid + "'" + e.getLocalizedMessage());
		}
	}

	@Override
	public void removeAccount(String identifier) {
		accounts.remove(identifier);
		try {
			createStatement(removeAccount, new Object[] {identifier}).execute();
		} catch (SQLException e) {
			plugin.getLogger().warn("Error when deleting Account '" + identifier + "'" + e.getLocalizedMessage());
		}
	}

	@Override
	protected void createTablesAndLoad() throws SQLException {

		createUniqueAccountsTable = "CREATE TABLE IF NOT EXISTS ECONOMY_UNIQUE_ACCOUNTS(UUID VARCHAR(128) NOT NULL UNIQUE, ACCOUNT_DATA TEXT, PRIMARY KEY(UUID))";
		createAccountsTable = "CREATE TABLE IF NOT EXISTS ECONOMY_ACCOUNTS(IDENTIFIER VARCHAR(128) NOT NULL UNIQUE, ACCOUNT_DATA TEXT, PRIMARY KEY(IDENTIFIER))";
		loadAllUniqueAccounts = "SELECT * FROM ECONOMY_UNIQUE_ACCOUNTS";
		loadAllAccounts = "SELECT * FROM ECONOMY_ACCOUNTS";
		removeUniqueAccount = "DELETE FROM ECONOMY_UNIQUE_ACCOUNTS WHERE UUID = ?";
		removeAccount = "DELETE FROM ECONOMY_ACCOUNTS WHERE IDENTIFIER = ?";
		insertUniqueAccount = "MERGE INTO ECONOMY_UNIQUE_ACCOUNTS (UUID, ACCOUNT_DATA) VALUES(?, ?)";
		insertAccount = "MERGE INTO ECONOMY_ACCOUNTS (IDENTIFIER, ACCOUNT_DATA) VALUES(?, ?)";
		uuidCollumn = "UUID";
		identifierCollumn = "IDENTIFIER";
		dataCollumn = "ACCOUNT_DATA";

		executeSQL(createUniqueAccountsTable);
		executeSQL(createAccountsTable);
		ResultSet resultSetUniqueAccounts = resultSet(loadAllUniqueAccounts);
		while(resultSetUniqueAccounts.next()) uniqueAccounts.put(UUID.fromString(resultSetUniqueAccounts.getString(uuidCollumn)), uniqueAccountFromString(resultSetUniqueAccounts.getString(dataCollumn)));
		ResultSet resultSetAccounts = resultSet(loadAllAccounts);
		while(resultSetAccounts.next()) accounts.put(resultSetAccounts.getString(identifierCollumn), accountFromString(resultSetAccounts.getString(dataCollumn)));
	}

	private String uniqueAccountToString(UniqueAccount account) {
		return ConfigurationService.getInstance().createVirtualReferencedConfig(new SerializedUniqueAccount(account)).setType(ConfigTypes.HOCON).build().getRawData();
	}

	private String accountToString(Account account) {
		return ConfigurationService.getInstance().createVirtualReferencedConfig(new SerializedAccount(account)).setType(ConfigTypes.HOCON).build().getRawData();
	}

}
