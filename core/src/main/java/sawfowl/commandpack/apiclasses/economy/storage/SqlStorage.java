package sawfowl.commandpack.apiclasses.economy.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.spongepowered.configurate.serialize.SerializationException;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.apiclasses.economy.CPAccount;
import sawfowl.commandpack.apiclasses.economy.CPUniqueAccount;
import sawfowl.commandpack.apiclasses.economy.EconomyServiceImpl;
import sawfowl.commandpack.configure.configs.economy.SerializedAccount;
import sawfowl.commandpack.configure.configs.economy.SerializedUniqueAccount;
import sawfowl.localeapi.api.ConfigTypes;
import sawfowl.localeapi.api.services.ConfigurationService;

public abstract class SqlStorage extends AbstractEconomyStorage {

	private Connection connection;
	private Statement statement;
	public SqlStorage(CommandPackInstance plugin, EconomyServiceImpl economyService) {
		super(plugin, economyService);
	}

	protected abstract Connection createConnection() throws SQLException;

	protected Connection getConnection() throws SQLException {
		return connection;
	}

	protected abstract void createTablesAndLoad() throws SQLException;

	@Override
	public void load() {
		try {
			createTablesAndLoad();
		} catch (SQLException e) {
			plugin.getLogger().error("Error loading accounts data " + e.getLocalizedMessage());
		}
	}

	protected boolean executeSQL(String sql) {
		try {
			if(connection == null || connection.isClosed()) connection = createConnection();
			if(statement == null || statement.isClosed()) statement = connection.createStatement();
			return statement.execute(sql);
		}
		catch (SQLException e) {
			plugin.getLogger().error("Error during SQL query execution '" + sql + "'...\n" + e.getLocalizedMessage());
			return false;
		}
	}

	protected ResultSet resultSet(String sql) throws SQLException {
		if(connection == null || connection.isClosed()) connection = createConnection();
		if(statement == null || statement.isClosed()) statement = connection.createStatement();
		return statement.executeQuery(sql);
	}

	protected PreparedStatement createStatement(String sql, Object[] objects) throws SQLException {
		int i = 1;
		if(connection == null || connection.isClosed()) connection = createConnection();
		PreparedStatement statement = connection.prepareStatement(sql);
		for(Object object : objects) {
			statement.setString(i, object.toString());
			i++;
		}
		return statement;
	}

	protected CPUniqueAccount uniqueAccountFromString(String string) {
		var config = ConfigurationService.getInstance().createVirtualReferencedConfig(SerializedUniqueAccount.class).setType(ConfigTypes.HOCON).build();
		config.loadFromRaw(string);
		if(!config.getRootNode().node("Content").virtual()) {
			try {
				return CPUniqueAccount.deserealize(config.getRootNode().node("Content").get(SerializedUniqueAccount.class), this);
			} catch (SerializationException e) {
				e.printStackTrace();
				return null;
			}
		} else return config.get() == null || config.get().getUserId() == null ? null : CPUniqueAccount.deserealize(config.get(), this);
	}

	protected CPAccount accountFromString(String string) {
		var config = ConfigurationService.getInstance().createVirtualReferencedConfig(SerializedAccount.class).setType(ConfigTypes.HOCON).build();
		config.loadFromRaw(string);
		if(!config.getRootNode().node("Content").virtual()) {
			try {
				return CPAccount.deserealize(config.getRootNode().node("Content").get(SerializedAccount.class), this);
			} catch (SerializationException e) {
				e.printStackTrace();
				return null;
			}
		} else return config.get() == null || config.get().getName() == null || config.get().getName().equals("n/a") ? null : CPAccount.deserealize(config.get(), this);
	}

}
