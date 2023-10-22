package sawfowl.commandpack.apiclasses.economy.storage;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.apiclasses.economy.CPAccount;
import sawfowl.commandpack.apiclasses.economy.CPUniqueAccount;
import sawfowl.commandpack.apiclasses.economy.EconomyServiceImpl;
import sawfowl.commandpack.configure.configs.economy.SerializedAccount;
import sawfowl.commandpack.configure.configs.economy.SerializedUniqueAccount;
import sawfowl.localeapi.api.serializetools.SerializeOptions;

public abstract class SqlStorage extends AbstractEconomyStorage {

	private Connection connection;
	private Statement statement;
	public SqlStorage(CommandPack plugin, EconomyServiceImpl economyService) {
		super(plugin, economyService);
	}

	protected abstract Connection createConnection() throws SQLException;

	protected Connection getConnection() throws SQLException {
		return connection;
	}

	protected abstract void createTablesAndLoad() throws SQLException;

	@Override
	public void load() throws ConfigurateException {
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
		StringReader source = new StringReader(string);
		HoconConfigurationLoader loader = SerializeOptions.createHoconConfigurationLoader(plugin.getMainConfig().getItemSerializer()).source(() -> new BufferedReader(source)).build();
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
		HoconConfigurationLoader loader = SerializeOptions.createHoconConfigurationLoader(plugin.getMainConfig().getItemSerializer()).source(() -> new BufferedReader(source)).build();
		try {
			ConfigurationNode node = loader.load().node("Content");
			return node.virtual() ? null : CPAccount.deserealize(node.get(SerializedAccount.class), this);
		} catch (ConfigurateException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
			return null;
		}
	}

}
