package sawfowl.commandpack.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import sawfowl.commandpack.CommandPack;

public class MariaDB {

	private final CommandPack plugin;
	private final String user;
	private final String database;
	private final String password;
	private final String port;
	private final String hostname;
	private final String ssl;
	private Connection connection;

	public MariaDB(CommandPack instance) {
		this.plugin = instance;
		this.hostname = plugin.getMainConfig().getMySqlConfig().getHost();
		this.port = plugin.getMainConfig().getMySqlConfig().getPort();
		this.database = plugin.getMainConfig().getMySqlConfig().getDatabase();
		this.user = plugin.getMainConfig().getMySqlConfig().getUser();
		this.password = plugin.getMainConfig().getMySqlConfig().getPassword();
		this.ssl = plugin.getMainConfig().getMySqlConfig().getSSL();
		this.connection = null;
	}

	public Connection openConnection() {
		String url = "jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database;
		Properties properties = new Properties();
		properties.setProperty("user", this.user);
		properties.setProperty("password", this.password);
		properties.setProperty("useSSL", this.ssl);
		try {
			return this.connection = DriverManager.getConnection(url, properties);
		} catch (SQLException e) {
			return null;
		}
	}
	
	public boolean checkConnection() {
		return this.connection != null;
	}

	public Connection getConnection() {
		return this.connection;
	}

	public Connection getOrOpenConnection() {
		if(!checkConnection()) return openConnection();
		try {
			if(connection.isClosed()) return openConnection();
		} catch (SQLException e) {
			plugin.getLogger().error(e.getMessage());
		}
		return null;
	}

	public void closeConnection() {
		if (this.connection != null) {
			try {
				this.connection.close();
			} catch (SQLException e) {
				plugin.getLogger().error("Error closing the MySQL Connection!");
				plugin.getLogger().error(e.getMessage());
			}
		}
	}
}
