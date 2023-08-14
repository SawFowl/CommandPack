package sawfowl.commandpack.apiclasses.economy.storage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.economy.Currency;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.apiclasses.economy.CPAccount;
import sawfowl.commandpack.apiclasses.economy.CPUniqueAccount;
import sawfowl.commandpack.apiclasses.economy.EconomyServiceImpl;
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
	private Map<CurrencyData, Currency> currenciesCollumns = new HashMap<CurrencyData, Currency>();
	public MySqlStorage(CommandPack plugin, EconomyServiceImpl economyService) {
		super(plugin, economyService);
		economyService.getCurrenciesMap().forEach((k, v) -> {
			plugin.getMainConfig().getEconomy().getCurrency(k).ifPresent(c -> {
				currenciesCollumns.put(new CurrencyData(c.getCollumn(), c.getStartingBalance()), v);
			});
		});
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
		for(int i = currenciesCollumns.size(); i > 0; i--) {
			args = args + ", ?";
		}
		insertUniqueAccount = "REPLACE INTO " + uniqueAccountsTable + " (" + uuidCollumn + ", " + identifierCollumn + ") VALUES(?, ? " + args + ")";
		insertAccount = "REPLACE INTO " + accountsTable + " (" + identifierCollumn + ") VALUES(? " + args + ")";
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
		executeSQL(createUniqueAccountsTable);
		executeSQL(createAccountsTable);
		if(resultSet("SHOW TABLES LIKE '" + uniqueAccountsTable + "'").next() && resultSet("SHOW TABLES LIKE '" + accountsTable + "'").next()) {
			currenciesCollumns.keySet().forEach(currency -> {
				executeSQL("ALTER TABLE " + uniqueAccountsTable + " ADD COLUMN IF NOT EXISTS " + currency.collumn + " DOUBLE DEFAULT " + currency.defaultValue + ";");
				executeSQL("ALTER TABLE " + accountsTable + " ADD COLUMN IF NOT EXISTS " + currency.collumn + " DOUBLE DEFAULT " + currency.defaultValue + ";");
			});
			executeSQL("ALTER TABLE " + uniqueAccountsTable + " ADD COLUMN IF NOT EXISTS " + writtenCollumn + " DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;");
			executeSQL("ALTER TABLE " + accountsTable + " ADD COLUMN IF NOT EXISTS " + writtenCollumn + " DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;");
		}
		ResultSet resultSetUniqueAccounts = resultSet(loadAllUniqueAccounts);
		while(resultSetUniqueAccounts.next()) {
			
		};
		ResultSet resultSetAccounts = resultSet(loadAllAccounts);
		while(resultSetAccounts.next()) {
			
		};
	}

	@Override
	public void saveUniqueAccount(CPUniqueAccount account) {
	}

	@Override
	public void saveAccount(CPAccount account) {
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
		}
	}

	private DBSettings getSettings() {
		return plugin.getMainConfig().getEconomy().getDBSettings();
	}

	class CurrencyData {

		final String collumn;
		final double defaultValue;
		CurrencyData(String collumn, double defaultValue) {
			this.collumn = collumn;
			this.defaultValue = defaultValue;
		}

		@Override
		public int hashCode() {
			return Objects.hash(collumn);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || getClass() != obj.getClass()) return false;
			return this == obj || Objects.equals(collumn, ((CurrencyData) obj).collumn);
		}

	}

}
