package sawfowl.commandpack.apiclasses.economy.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

public abstract class SqlStorage extends AbstractEconomyStorage {

	protected Connection connection;
	protected Statement statement;
	private String createUniqueAccountsTable;
	private String createAccountsTable;
	private String loadAllUniqueAccounts;
	private String loadAllAccounts;
	private String removeUniqueAccount;
	private String removeAccount;
	private String insertUniqueAccount;
	private String insertAccount;
	protected String uuidCollumn;
	protected String identifierCollumn;
	protected String dataCollumn;
	public SqlStorage(CommandPack plugin, EconomyServiceImpl economyService) {
		super(plugin, economyService);
	}

	protected abstract Connection getConnection() throws SQLException;
	protected abstract String createUniqueAccountsTable();
	protected abstract String createAccountsTable();
	protected abstract String loadAllUniqueAccounts();
	protected abstract String loadAllAccounts();
	protected abstract String removeUniqueAccount();
	protected abstract String removeAccount();
	protected abstract String insertUniqueAccount();
	protected abstract String insertAccount();
	protected abstract String uuidCollumn();
	protected abstract String identifierCollumn();
	protected abstract String dataCollumn();

	@Override
	public void load() throws ConfigurateException {
		createUniqueAccountsTable = createUniqueAccountsTable();
		createAccountsTable = createAccountsTable();
		loadAllUniqueAccounts = loadAllUniqueAccounts();
		loadAllAccounts = loadAllAccounts();
		removeUniqueAccount = removeUniqueAccount();
		removeAccount = removeAccount();
		insertUniqueAccount = insertUniqueAccount();
		insertAccount = insertAccount();
		uuidCollumn = uuidCollumn();
		identifierCollumn = identifierCollumn();
		dataCollumn = dataCollumn();
		executeSQL(createUniqueAccountsTable);
		executeSQL(createAccountsTable);
		try {
			ResultSet resultSetUniqueAccounts = resultSet(loadAllUniqueAccounts);
			while(resultSetUniqueAccounts.next()) uniqueAccounts.put(UUID.fromString(resultSetUniqueAccounts.getString(uuidCollumn)), uniqueAccountFromString(resultSetUniqueAccounts.getString(dataCollumn)));
			ResultSet resultSetAccounts = resultSet(loadAllAccounts);
			while(resultSetAccounts.next()) accounts.put(resultSetAccounts.getString(identifierCollumn), accountFromString(resultSetAccounts.getString(dataCollumn)));
		} catch (SQLException e) {
			plugin.getLogger().error("Error loading accounts data " + e.getLocalizedMessage());
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

	protected boolean executeSQL(String sql) {
		try {
			if(connection == null || connection.isClosed()) connection = getConnection();
			if(statement == null || statement.isClosed()) statement = connection.createStatement();
			return statement.execute(sql);
		}
		catch (SQLException e) {
			plugin.getLogger().error("Error during SQL query execution '" + sql + "'...\n" + e.getLocalizedMessage());
			return false;
		}
	}

	protected ResultSet resultSet(String sql) throws SQLException {
		if(connection == null || connection.isClosed()) connection = getConnection();
		if(statement == null || statement.isClosed()) statement = connection.createStatement();
		ResultSet result = statement.executeQuery(sql);
		return result;
	}

	protected PreparedStatement createStatement(String sql, Object[] objects) throws SQLException {
		int i = 1;
		if(connection == null || connection.isClosed()) connection = getConnection();
		PreparedStatement statement = connection.prepareStatement(sql);
		for(Object object : objects) {
			statement.setString(i, object.toString());
			i++;
		}
		return statement;
	}

	protected CPUniqueAccount uniqueAccountFromString(String string) {
		StringReader source = new StringReader(string);
		HoconConfigurationLoader loader = HoconConfigurationLoader.builder().defaultOptions(plugin.getLocales().getLocaleService().getConfigurationOptions()).source(() -> new BufferedReader(source)).build();
		try {
			ConfigurationNode node = loader.load().node("Content");
			return node.virtual() ? null : CPUniqueAccount.deserealize(node.get(SerializedUniqueAccount.class), this);
		} catch (ConfigurateException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
			return null;
		}
	}

	protected CPAccount accountFromString(String string) {
		StringReader source = new StringReader(string);
		HoconConfigurationLoader loader = HoconConfigurationLoader.builder().defaultOptions(plugin.getLocales().getLocaleService().getConfigurationOptions()).source(() -> new BufferedReader(source)).build();
		try {
			ConfigurationNode node = loader.load().node("Content");
			return node.virtual() ? null : CPAccount.deserealize(node.get(SerializedAccount.class), this);
		} catch (ConfigurateException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
			return null;
		}
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
		node.node("Content").set(CPAccount.class, new SerializedAccount(account));
		loader.save(node);
		return sink.toString();
	}

}
