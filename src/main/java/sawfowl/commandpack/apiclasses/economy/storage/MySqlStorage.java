package sawfowl.commandpack.apiclasses.economy.storage;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.economy.Currency;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.apiclasses.economy.CPAccount;
import sawfowl.commandpack.apiclasses.economy.CPUniqueAccount;
import sawfowl.commandpack.apiclasses.economy.EconomyServiceImpl;
import sawfowl.commandpack.configure.configs.economy.CurrencyConfig;
import sawfowl.commandpack.configure.configs.economy.dbsettings.DBSettings;

public class MySqlStorage extends SqlStorage {

	private String lastUpdateUniqueAccounts;
	private String lastUpdateAccounts;
	private String writtenCollumn;	
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
	private String uniqueAccountsTable;
	private String accountsTable;
	private LinkedHashMap<CurrencyConfig, Currency> currenciesCollumns;
	private Connection syncConnection;
	private Statement syncStatement;
	public MySqlStorage(CommandPack plugin, EconomyServiceImpl economyService) {
		super(plugin, economyService);
		Sponge.asyncScheduler().submit(Task.builder().plugin(plugin.getPluginContainer()).interval(plugin.getMainConfig().getEconomy().getUpdateInterval(), TimeUnit.MILLISECONDS).execute(() -> {
			sync();
		}).build());
	}

	@Override
	protected Connection createConnection() throws SQLException {
		return plugin.getMariaDB().get().createNewConnection();
	}

	@Override
	protected void createTablesAndLoad() throws SQLException {

		currenciesCollumns = new LinkedHashMap<CurrencyConfig, Currency>();
		economyService.getCurrenciesMap().forEach((k, v) -> {
			plugin.getMainConfig().getEconomy().getCurrency(k).ifPresent(c -> {
				currenciesCollumns.put(c, v);
			});
		});
		currenciesCollumns = currenciesCollumns.entrySet().stream() 
				.sorted(Comparator.comparingInt(e -> e.getKey().getID())) 
				.collect(Collectors.toMap( 
				Map.Entry::getKey, 
				Map.Entry::getValue, 
				(a, b) -> { throw new AssertionError(); }, 
				LinkedHashMap<CurrencyConfig, Currency>::new 
				));
		uniqueAccountsTable = getSettings().getTables().getUniqueAccounts();
		accountsTable = getSettings().getTables().getAccounts();
		writtenCollumn = "written";
		uuidCollumn = getSettings().getCollumns().getUuid();
		identifierCollumn = getSettings().getCollumns().getIdentifier();
		createUniqueAccountsTable = "CREATE TABLE IF NOT EXISTS " + uniqueAccountsTable + "(" + uuidCollumn + " VARCHAR(128) UNIQUE, " + identifierCollumn + " VARCHAR(128), PRIMARY KEY(" + uuidCollumn + "))";
		createAccountsTable = "CREATE TABLE IF NOT EXISTS " + accountsTable + "(" + identifierCollumn + " VARCHAR(128) UNIQUE, PRIMARY KEY(" + identifierCollumn + "))";
		loadAllUniqueAccounts = "SELECT * FROM " + uniqueAccountsTable;
		loadAllAccounts = "SELECT * FROM " + accountsTable;
		removeUniqueAccount = "DELETE FROM " + uniqueAccountsTable + " WHERE " + uuidCollumn + " = ?";
		removeAccount = "DELETE FROM " + accountsTable + " WHERE " + identifierCollumn + " = ?";
		String args = "";
		insertUniqueAccount = "REPLACE INTO " + uniqueAccountsTable + "(" + uuidCollumn + ", " + identifierCollumn;
		insertAccount = "REPLACE INTO " + accountsTable + "(" + identifierCollumn;
		for(CurrencyConfig currency : currenciesCollumns.keySet()) {
			insertUniqueAccount = insertUniqueAccount + ", " + currency.getCollumn();
			insertAccount = insertAccount + ", " + currency.getCollumn();
			args = args + ", ?";
		}
		insertUniqueAccount = insertUniqueAccount + ") VALUES(?, ?" + args + ")";
		insertAccount = insertAccount + ") VALUES(?" + args + ")";

		executeSQL(createUniqueAccountsTable);
		executeSQL(createAccountsTable);
		if(resultSet("SHOW TABLES LIKE '" + uniqueAccountsTable + "'").next() && resultSet("SHOW TABLES LIKE '" + accountsTable + "'").next()) {
			currenciesCollumns.keySet().forEach(currency -> {
				executeSQL("ALTER TABLE " + uniqueAccountsTable + " ADD COLUMN IF NOT EXISTS " + currency.getCollumn() + " DOUBLE DEFAULT " + currency.getStartingBalance() + ";");
				executeSQL("ALTER TABLE " + accountsTable + " ADD COLUMN IF NOT EXISTS " + currency.getCollumn() + " DOUBLE DEFAULT " + currency.getStartingBalance() + ";");
			});
			executeSQL("ALTER TABLE " + uniqueAccountsTable + " ADD COLUMN IF NOT EXISTS " + writtenCollumn + " DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;");
			executeSQL("ALTER TABLE " + accountsTable + " ADD COLUMN IF NOT EXISTS " + writtenCollumn + " DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;");
		}
		ResultSet resultSetUniqueAccounts = resultSet(loadAllUniqueAccounts);
		while(resultSetUniqueAccounts.next()) loadUniqueAccount(resultSetUniqueAccounts);
		ResultSet resultSetAccounts = resultSet(loadAllAccounts);
		while(resultSetAccounts.next()) loadAccount(resultSetAccounts);
	}

	@Override
	public void saveUniqueAccount(CPUniqueAccount account) {
		try {
			createStatement(insertUniqueAccount, uniqueAccountSqlArgs(account)).execute();
		} catch (SQLException e) {
			plugin.getLogger().warn("Error when saving UniqueAccount (" + account.uniqueId() + " - " + account.identifier() + ") " + e.getLocalizedMessage());
		}
	}

	@Override
	public void saveAccount(CPAccount account) {
		try {
			createStatement(insertAccount, accountSqlArgs(account)).execute();
		} catch (SQLException e) {
			plugin.getLogger().warn("Error when saving Account " + account.identifier() + "" + e.getLocalizedMessage());
		}
	}

	private Object[] uniqueAccountSqlArgs(CPUniqueAccount account) {
		List<Object> args = new ArrayList<Object>();
		args.add(account.uniqueId());
		args.add(account.identifier());
		currenciesCollumns.forEach((k, v) -> {
			args.add(account.balance(v).doubleValue());
		});
		return args.toArray();
	}

	private Object[] accountSqlArgs(CPAccount account) {
		List<Object> args = new ArrayList<Object>();
		args.add(account.identifier());
		currenciesCollumns.forEach((k, v) -> {
			args.add(account.balance(v).doubleValue());
		});
		return args.toArray();
	}

	@Override
	public void removeUniqueAccount(UUID uuid) {
		try {
			createStatement(removeUniqueAccount, new Object[] {uniqueAccounts.remove(uuid).uniqueId()}).execute();
		} catch (SQLException e) {
			plugin.getLogger().warn("Error when deleting UniqueAccount " + uuid + "" + e.getLocalizedMessage());
		}
	}

	@Override
	public void removeAccount(String identifier) {
		try {
			createStatement(removeAccount, new Object[] {accounts.remove(identifier).identifier()}).execute();
		} catch (SQLException e) {
			plugin.getLogger().warn("Error when deleting Account " + identifier + "" + e.getLocalizedMessage());
		}
	}

	private void loadUniqueAccount(ResultSet resultSet) throws SQLException {
		Map<Currency, BigDecimal> balances = new HashMap<Currency, BigDecimal>();
		UUID uuid = UUID.fromString(resultSet.getString(uuidCollumn));
		String identifier = resultSet.getString(identifierCollumn);
		for(Entry<CurrencyConfig, Currency> entry : currenciesCollumns.entrySet()) balances.put(entry.getValue(), BigDecimal.valueOf(resultSet.getDouble(entry.getKey().getCollumn())));
		CPUniqueAccount account = CPUniqueAccount.create(uuid, identifier, balances, this);
		if(uniqueAccounts.containsKey(uuid)) {
			uniqueAccounts.replace(uuid, account);
		} else uniqueAccounts.put(uuid, account);
	}

	private void loadAccount(ResultSet resultSet) throws SQLException {
		Map<Currency, BigDecimal> balances = new HashMap<Currency, BigDecimal>();
		String identifier = resultSet.getString(identifierCollumn);
		for(Entry<CurrencyConfig, Currency> entry : currenciesCollumns.entrySet()) balances.put(entry.getValue(), BigDecimal.valueOf(resultSet.getDouble(entry.getKey().getCollumn())));
		CPAccount account = CPAccount.create(identifier, balances, this);
		if(accounts.containsKey(identifier)) {
			accounts.replace(identifier, account);
		} else accounts.put(identifier, account);
	}

	private void sync() {
		try {
			syncUniqueAccounts(syncResultSet(lastUpdateUniqueAccounts == null ? loadAllUniqueAccounts + " ORDER BY written DESC LIMIT 1" : loadAllUniqueAccounts + " WHERE written > '" + lastUpdateUniqueAccounts + "' ORDER BY written;"));
			syncAccounts(syncResultSet(lastUpdateAccounts == null ? loadAllAccounts + " ORDER BY written DESC LIMIT 1" : loadAllAccounts + " WHERE written > '" + lastUpdateAccounts + "' ORDER BY written;"));
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
			loadUniqueAccount(resultSet);
		}
	}

	private void syncAccounts(ResultSet resultSet) throws SQLException {
		boolean timeUpdate = true;
		while(resultSet.next()) {
			if(timeUpdate) {
				lastUpdateAccounts = resultSet.getString(writtenCollumn);
				timeUpdate = false;
			}
			loadAccount(resultSet);
		}
	}

	private DBSettings getSettings() {
		return plugin.getMainConfig().getEconomy().getDBSettings();
	}

	private ResultSet syncResultSet(String sql) throws SQLException {
		checkSyncConnection();
		if(syncStatement == null || syncStatement.isClosed()) syncStatement = syncConnection.createStatement();
		return syncStatement.executeQuery(sql);
	}

	private void checkSyncConnection() throws SQLException {
		if(syncConnection == null || syncConnection.isClosed()) syncConnection = plugin.getMariaDB().get().createNewConnection();
	}

}
