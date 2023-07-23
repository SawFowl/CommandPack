package sawfowl.commandpack.apiclasses.punishment.storage;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.Ban;
import org.spongepowered.api.service.ban.Ban.IP;
import org.spongepowered.api.service.ban.Ban.Profile;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.api.data.punishment.Warns;
import sawfowl.commandpack.configure.configs.punishment.BanData;
import sawfowl.commandpack.configure.configs.punishment.MuteData;
import sawfowl.commandpack.configure.configs.punishment.WarnsData;

public abstract class SqlStorage extends AbstractPunishmentStorage {

	protected Connection connection;
	protected Statement statement;
	private String createProfileBansTableSql;
	private String createIPBansTableSql;
	private String createMutesTableSql;
	private String createWarnsTableSql;
	private String selectAllProfileBansSql;
	private String selectAllIPBansSql;
	private String selectAllMutesSql;
	private String selectAllWarnsSql;
	private String insertProfileBanSql;
	private String insertIPBanSql;
	private String insertMuteSql;
	private String insertWarnsSql;
	private String deleteProfileBanSql;
	private String deleteIPBanSql;
	private String deleteMuteSql;
	private String deleteWarnsSql;
	public SqlStorage(CommandPack plugin) {
		super(plugin);
	}

	public abstract boolean removeBeforeAdd();

	public abstract Connection getConnection() throws SQLException;

	public abstract String createProfileBansTableSql();

	public abstract String createIPBansTableSql();

	public abstract String createMutesTableSql();

	public abstract String createWarnsTableSql();

	public abstract String selectAllProfileBansSql();

	public abstract String selectAllIPBansSql();

	public abstract String selectAllMutesSql();

	public abstract String selectAllWarnsSql();

	public abstract String insertProfileBanSql();

	public abstract String insertIPBanSql();

	public abstract String insertMuteSql();

	public abstract String insertWarnsSql();

	public abstract String deleteProfileBanSql();

	public abstract String deleteIPBanSql();

	public abstract String deleteMuteSql();

	public abstract String deleteWarnsSql();

	public abstract void loadBanProfile(ResultSet results) throws SQLException;

	public abstract void loadBanIP(ResultSet results) throws SQLException;

	public abstract void loadMute(ResultSet results) throws SQLException;

	public abstract void loadWarns(ResultSet results) throws SQLException;

	public abstract Object[] insertProfileBanObjects(Profile ban) throws ConfigurateException;

	public abstract Object[] insertIPBanObjects(IP ban) throws ConfigurateException;

	public abstract Object[] insertMuteObjects(Mute mute) throws ConfigurateException;

	public abstract Object[] insertWarnsObjects(Warns warns) throws ConfigurateException;

	@Override
	public void saveBan(Profile ban) {
		if(bans.containsKey(ban.profile().uniqueId())) return;
		bans.put(ban.profile().uniqueId(), ban);
		try {
			if(removeBeforeAdd()) createStatement(deleteProfileBanSql, new Object[] {ban.profile().uniqueId()}).execute();;
			createStatement(insertProfileBanSql, insertProfileBanObjects(ban)).execute();
		} catch (ConfigurateException | SQLException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
		}
	}

	@Override
	public boolean deleteBan(GameProfile profile) {
		try {
			bans.remove(profile.uniqueId());
			return createStatement(deleteProfileBanSql, new Object[] {profile.uniqueId()}).execute();
		} catch (SQLException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
		}
		return false;
	}

	@Override
	public void saveBan(IP ban) {
		if(bansIP.containsKey(ban.address())) return;
		bansIP.put(ban.address(), ban);
		try {
			if(removeBeforeAdd()) createStatement(deleteIPBanSql, new Object[] {ban.address().getHostAddress()}).execute();
			createStatement(insertIPBanSql, insertIPBanObjects(ban)).execute();
		} catch (ConfigurateException | SQLException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
		}
	}

	@Override
	public boolean deleteIPBan(InetAddress address) {
		try {
			bansIP.remove(address);
			return createStatement(deleteIPBanSql, new Object[] {address.getHostAddress()}).execute();
		} catch (SQLException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
		}
		return false;
	}

	@Override
	public void saveMute(Mute mute) {
		if(mutes.containsKey(mute.getUniqueId())) return;
		mutes.put(mute.getUniqueId(), mute);
		try {
			if(removeBeforeAdd()) createStatement(deleteMuteSql, new Object[] {mute.getUniqueId()}).execute();
			createStatement(insertMuteSql, insertMuteObjects(mute)).execute();
		} catch (ConfigurateException | SQLException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
		}
	}

	@Override
	public boolean deleteMute(Mute mute) {
		try {
			mutes.remove(mute.getUniqueId());
			return createStatement(deleteMuteSql, new Object[] {mute.getUniqueId()}).execute();
		} catch (SQLException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
		}
		return false;
	}

	@Override
	public void saveWarns(Warns warns) {
		if(super.warns.containsKey(warns.getUniqueId())) return;
		super.warns.put(warns.getUniqueId(), warns);
		try {
			if(removeBeforeAdd()) createStatement(deleteWarnsSql, new Object[] {warns.getUniqueId()}).execute();
			createStatement(insertWarnsSql, insertWarnsObjects(warns)).execute();
		} catch (ConfigurateException | SQLException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
		}
	}

	@Override
	public boolean deleteWarns(UUID player) {
		try {
			if(warns.containsKey(player)) warns.remove(player);
			return createStatement(deleteWarnsSql, new Object[] {player}).execute();
		} catch (SQLException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
		}
		return false;
	}

	@Override
	public void load() {
		try {
			createProfileBansTableSql = createProfileBansTableSql();
			createIPBansTableSql = createIPBansTableSql();
			createMutesTableSql = createMutesTableSql();
			createWarnsTableSql = createWarnsTableSql();
			selectAllProfileBansSql = selectAllProfileBansSql();
			selectAllIPBansSql = selectAllIPBansSql();
			selectAllMutesSql = selectAllMutesSql();
			selectAllWarnsSql = selectAllWarnsSql();
			insertProfileBanSql = insertProfileBanSql();
			insertIPBanSql = insertIPBanSql();
			insertMuteSql = insertMuteSql();
			insertWarnsSql = insertWarnsSql();
			deleteProfileBanSql = deleteProfileBanSql();
			deleteIPBanSql = deleteIPBanSql();
			deleteMuteSql = deleteMuteSql();
			deleteWarnsSql = deleteWarnsSql();
			closeConnection();
			openConnection();
			createTables();
			ResultSet results = resultSet(selectAllProfileBansSql);
			while(!results.isClosed() && results.next()) loadBanProfile(results);
			results = resultSet(selectAllIPBansSql);
			while(!results.isClosed() && results.next()) loadBanIP(results);
			results = resultSet(selectAllMutesSql);
			while(!results.isClosed() && results.next()) loadMute(results);
			results = resultSet(selectAllWarnsSql);
			while(!results.isClosed() && results.next()) loadWarns(results);
		} catch (SQLException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
		}
	}

	protected Ban.Profile profileFromString(String banData) {
		StringReader source = new StringReader(banData);
		HoconConfigurationLoader loader = HoconConfigurationLoader.builder().defaultOptions(plugin.getLocales().getLocaleService().getConfigurationOptions()).source(() -> new BufferedReader(source)).build();
		try {
			ConfigurationNode node = loader.load().node("Content");
			return node.virtual() ? null : (Profile) node.get(BanData.class).getBan();
		} catch (ConfigurateException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
			return null;
		}
	}

	protected IP ipFromString(String banData) {
		StringReader source = new StringReader(banData);
		HoconConfigurationLoader loader = HoconConfigurationLoader.builder().defaultOptions(plugin.getLocales().getLocaleService().getConfigurationOptions()).source(() -> new BufferedReader(source)).build();
		try {
			ConfigurationNode node = loader.load().node("Content");
			return node.virtual() ? null : (IP) node.get(BanData.class).getBan();
		} catch (ConfigurateException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
			return null;
		}
	}

	protected Mute muteFromString(String muteData) {
		StringReader source = new StringReader(muteData);
		HoconConfigurationLoader loader = HoconConfigurationLoader.builder().defaultOptions(plugin.getLocales().getLocaleService().getConfigurationOptions()).source(() -> new BufferedReader(source)).build();
		try {
			ConfigurationNode node = loader.load().node("Content");
			return node.virtual() ? null : node.get(MuteData.class);
		} catch (ConfigurateException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
			return null;
		}
	}

	protected Warns warnsFromString(String warnsData) {
		StringReader source = new StringReader(warnsData);
		HoconConfigurationLoader loader = HoconConfigurationLoader.builder().defaultOptions(plugin.getLocales().getLocaleService().getConfigurationOptions()).source(() -> new BufferedReader(source)).build();
		try {
			ConfigurationNode node = loader.load().node("Content");
			return node.virtual() ? null : node.get(WarnsData.class);
		} catch (ConfigurateException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
			return null;
		}
	}

	protected void createTables() {
		executeSQL(createProfileBansTableSql);
		executeSQL(createIPBansTableSql);
		executeSQL(createMutesTableSql);
		executeSQL(createWarnsTableSql);
	}

	protected void openConnection() throws SQLException {
		connection = getConnection();
	}

	private void closeConnection() {
		try {
			if(connection != null && !connection.isClosed()) connection.close();
			connection = null;
		} catch (SQLException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
		}
	}

	protected boolean executeSQL(String sql) {
		try {
			if(connection == null || connection.isClosed()) openConnection();
			if(statement == null || statement.isClosed()) statement = connection.createStatement();
			return statement.execute(sql);
		}
		catch (SQLException e) {
			plugin.getLogger().error("Error during H2SQL query execution '" + sql + "'...\n" + e.getLocalizedMessage());
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
