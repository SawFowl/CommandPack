package sawfowl.commandpack.apiclasses.punishment.storage;

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
import sawfowl.commandpack.api.data.punishment.Warns;
import sawfowl.commandpack.configure.configs.punishment.WarnsData;
import sawfowl.localeapi.api.serializetools.SerializeOptions;

public abstract class SqlStorage extends AbstractPunishmentStorage {

	protected Connection connection;
	protected Statement statement;
	public SqlStorage(CommandPack plugin) {
		super(plugin);
	}

	public abstract boolean removeBeforeAdd();

	public abstract Connection getConnection() throws SQLException;

	protected abstract void createTables() throws SQLException;

	@Override
	public void load() {
		try {
			openConnection();
			createTables();
		} catch (SQLException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
		}
	}

	protected Warns warnsFromString(String warnsData) {
		StringReader source = new StringReader(warnsData);
		HoconConfigurationLoader loader = SerializeOptions.createHoconConfigurationLoader(plugin.getMainConfig().getItemSerializer()).source(() -> new BufferedReader(source)).build();
		try {
			ConfigurationNode node = loader.load().node("Content");
			return node.virtual() ? null : node.get(WarnsData.class);
		} catch (ConfigurateException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
			return null;
		}
	}

	protected void openConnection() throws SQLException {
		connection = getConnection();
	}

	protected boolean executeSQL(String sql) {
		try {
			if(connection == null || connection.isClosed()) openConnection();
			if(statement == null || statement.isClosed()) statement = connection.createStatement();
			return statement.execute(sql);
		}
		catch (SQLException e) {
			plugin.getLogger().error("Error during SQL query execution '" + sql + "'...\n" + e.getLocalizedMessage());
			return false;
		}
	}

	protected ResultSet resultSet(String sql) throws SQLException {
		if(connection == null || connection.isClosed()) openConnection();
		if(statement == null || statement.isClosed()) statement = connection.createStatement();
		ResultSet result = statement.executeQuery(sql);
		return result;
	}

	protected PreparedStatement createStatement(String sql, Object[] objects) throws SQLException {
		int i = 1;
		if(connection == null || connection.isClosed()) openConnection();
		PreparedStatement statement = connection.prepareStatement(sql);
		for(Object object : objects) {
			statement.setString(i, object.toString());
			i++;
		}
		return statement;
	}

}
