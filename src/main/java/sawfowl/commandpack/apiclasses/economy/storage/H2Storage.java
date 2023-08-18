package sawfowl.commandpack.apiclasses.economy.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.apiclasses.economy.CPAccount;
import sawfowl.commandpack.apiclasses.economy.CPUniqueAccount;
import sawfowl.commandpack.apiclasses.economy.EconomyServiceImpl;
import sawfowl.commandpack.configure.configs.economy.SerializedAccount;
import sawfowl.commandpack.configure.configs.economy.SerializedUniqueAccount;

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
	public H2Storage(CommandPack plugin, EconomyServiceImpl economyService) {
		super(plugin, economyService);
	}

	@Override
	protected Connection createConnection() throws SQLException {
		if(!plugin.getConfigDir().resolve("Modules").toFile().exists()) plugin.getConfigDir().resolve("Modules").toFile().mkdir();
		return DriverManager.getConnection("jdbc:h2:" + plugin.getConfigDir().resolve("Modules" + File.separator + "Economy").toFile().getAbsolutePath(), "", "");
	}

	@Override
	public void saveUniqueAccount(CPUniqueAccount account) {
		try {
			createStatement(insertUniqueAccount, new Object[] {account.uniqueId().toString(), uniqueAccountToString(account)}).execute();
		} catch (SQLException | ConfigurateException e) {
			plugin.getLogger().warn("Error when saving UniqueAccount'" + account.uniqueId() + "'" + e.getLocalizedMessage());
		}
	}

	@Override
	public void saveAccount(CPAccount account) {
		try {
			createStatement(insertAccount, new Object[] {account.identifier(), accountToString(account)}).execute();
		} catch (SQLException | ConfigurateException e) {
			plugin.getLogger().warn("Error when saving Account'" + account.identifier() + "'" + e.getLocalizedMessage());
		}
	}

	@Override
	public void removeUniqueAccount(UUID uuid) {
		try {
			createStatement(removeUniqueAccount, new Object[] {uniqueAccounts.remove(uuid).uniqueId()}).execute();
		} catch (SQLException e) {
			plugin.getLogger().warn("Error when deleting UniqueAccount '" + uuid + "'" + e.getLocalizedMessage());
		}
	}

	@Override
	public void removeAccount(String identifier) {
		try {
			createStatement(removeAccount, new Object[] {accounts.remove(identifier).identifier()}).execute();
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

	private String uniqueAccountToString(CPUniqueAccount account) throws ConfigurateException {
		StringWriter sink = new StringWriter();
		HoconConfigurationLoader loader = HoconConfigurationLoader.builder().defaultOptions(options).sink(() -> new BufferedWriter(sink)).build();
		ConfigurationNode node = loader.createNode();
		node.node("Content").set(SerializedUniqueAccount.class, new SerializedUniqueAccount(account));
		loader.save(node);
		return sink.toString();
	}

	private String accountToString(CPAccount account) throws ConfigurateException {
		StringWriter sink = new StringWriter();
		HoconConfigurationLoader loader = HoconConfigurationLoader.builder().defaultOptions(options).sink(() -> new BufferedWriter(sink)).build();
		ConfigurationNode node = loader.createNode();
		node.node("Content").set(SerializedAccount.class, new SerializedAccount(account));
		loader.save(node);
		return sink.toString();
	}

}
