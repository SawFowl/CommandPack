package sawfowl.commandpack.apiclasses.punishment.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.Ban;
import org.spongepowered.api.service.ban.Ban.IP;
import org.spongepowered.api.service.ban.Ban.Profile;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.api.data.punishment.Warns;
import sawfowl.commandpack.configure.configs.punishment.BanData;
import sawfowl.commandpack.configure.configs.punishment.MuteData;
import sawfowl.commandpack.configure.configs.punishment.WarnsData;
import sawfowl.commandpack.utils.StorageType;
import sawfowl.localeapi.api.serializetools.SerializeOptions;

public class H2Storage extends SqlStorage {

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
	public H2Storage(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public boolean isSupportCombined() {
		return false;
	}

	@Override
	public boolean removeBeforeAdd() {
		return true;
	}

	@Override
	public Connection getConnection() throws SQLException {
		if(!plugin.getConfigDir().resolve("Modules").toFile().exists()) plugin.getConfigDir().resolve("Modules").toFile().mkdir();
		return DriverManager.getConnection("jdbc:h2:" + plugin.getConfigDir().resolve("Modules" + File.separator + "Punishments").toFile().getAbsolutePath(), "", "");
	}

	@Override
	protected void createTables() throws SQLException {

		createProfileBansTableSql = "CREATE TABLE IF NOT EXISTS BANS(UUID VARCHAR(128) UNIQUE, BAN_DATA LONGTEXT, PRIMARY KEY(UUID))";
		createIPBansTableSql = "CREATE TABLE IF NOT EXISTS BANS_IP(IP VARCHAR(128) UNIQUE, BAN_DATA LONGTEXT, PRIMARY KEY(IP))";
		createMutesTableSql = "CREATE TABLE IF NOT EXISTS MUTES(UUID VARCHAR(128) UNIQUE, MUTE_DATA LONGTEXT, PRIMARY KEY(UUID))";
		createWarnsTableSql = "CREATE TABLE IF NOT EXISTS WARNS(UUID VARCHAR(128) UNIQUE, WARNS_DATA LONGTEXT, PRIMARY KEY(UUID))";
		selectAllProfileBansSql = "SELECT * FROM BANS";
		selectAllIPBansSql = "SELECT * FROM BANS_IP";
		selectAllMutesSql = "SELECT * FROM MUTES";
		selectAllWarnsSql = "SELECT * FROM WARNS";
		insertProfileBanSql = "MERGE INTO BANS (UUID, BAN_DATA) VALUES(?, ?)";
		insertIPBanSql = "MERGE INTO BANS_IP (IP, BAN_DATA) VALUES(?, ?)";
		insertMuteSql = "MERGE INTO MUTES (UUID, MUTE_DATA) VALUES(?, ?)";
		insertWarnsSql = "MERGE INTO WARNS (UUID, WARNS_DATA) VALUES(?, ?)";
		deleteProfileBanSql = "DELETE FROM BANS WHERE UUID = ?";
		deleteIPBanSql = "DELETE FROM BANS_IP WHERE IP = ?";
		deleteMuteSql = "DELETE FROM MUTES WHERE UUID = ?";
		deleteWarnsSql = "DELETE FROM WARNS WHERE UUID = ?";

		executeSQL(createProfileBansTableSql);
		executeSQL(createIPBansTableSql);
		executeSQL(createMutesTableSql);
		executeSQL(createWarnsTableSql);
		ResultSet results = resultSet(selectAllProfileBansSql);
		while(!results.isClosed() && results.next()) loadBanProfile(results);
		if(plugin.getMainConfig().getPunishment().getStorageType() != StorageType.MYSQL || !plugin.getMainConfig().getPunishment().getDBSettings().isCreateCombinedBansTable()) {
			results = resultSet(selectAllIPBansSql);
			while(!results.isClosed() && results.next()) loadBanIP(results);
		}
		results = resultSet(selectAllMutesSql);
		while(!results.isClosed() && results.next()) loadMute(results);
		results = resultSet(selectAllWarnsSql);
		while(!results.isClosed() && results.next()) loadWarns(results);
	}

	@Override
	public void saveBans(Profile profile, IP ip) {
		saveBan(profile);
		saveBan(ip);
	}

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

	public void loadBanProfile(ResultSet results) throws SQLException {
		UUID uuid = UUID.fromString(results.getString("UUID"));
		String banData = results.getString("BAN_DATA");
		Ban.Profile ban = profileFromString(banData);
		if(ban != null) bans.put(uuid, ban);
	}

	public void loadBanIP(ResultSet results) throws SQLException {
		String banData = results.getString("BAN_DATA");
		Ban.IP ban = ipFromString(banData);
		if(ban != null) bansIP.put(ban.address(), ban);
	}

	public void loadMute(ResultSet results) throws SQLException {
		UUID uuid = UUID.fromString(results.getString("UUID"));
		String muteData = results.getString("MUTE_DATA");
		Mute mute = muteFromString(muteData);
		if(mute != null) mutes.put(uuid, mute);
	}

	public void loadWarns(ResultSet results) throws SQLException {
		UUID uuid = UUID.fromString(results.getString("UUID"));
		String warnsData = results.getString("WARNS_DATA");
		Warns warns = warnsFromString(warnsData);
		if(warns != null) super.warns.put(uuid, warns);
	}

	public Object[] insertProfileBanObjects(Profile ban) throws ConfigurateException {
		StringWriter sink = new StringWriter();
		HoconConfigurationLoader loader = HoconConfigurationLoader.builder().defaultOptions(options).sink(() -> new BufferedWriter(sink)).build();
		ConfigurationNode node = loader.createNode();
		node.node("Content").set(BanData.class, new BanData(ban));
		loader.save(node);
		return new Object[] {ban.profile().uniqueId(), sink};
	}

	public Object[] insertIPBanObjects(IP ban) throws ConfigurateException {
		StringWriter sink = new StringWriter();
		HoconConfigurationLoader loader = HoconConfigurationLoader.builder().defaultOptions(options).sink(() -> new BufferedWriter(sink)).build();
		ConfigurationNode node = loader.createNode();
		node.node("Content").set(BanData.class, new BanData(ban));
		loader.save(node);
		return new Object[] {ban.address().getHostAddress(), sink};
	}

	public Object[] insertMuteObjects(Mute mute) throws ConfigurateException {
		StringWriter sink = new StringWriter();
		HoconConfigurationLoader loader = HoconConfigurationLoader.builder().defaultOptions(options).sink(() -> new BufferedWriter(sink)).build();
		ConfigurationNode node = loader.createNode();
		node.node("Content").set(MuteData.class, (MuteData) (mute instanceof MuteData ? mute : Mute.builder().from(mute)));
		loader.save(node);
		return new Object[] {mute.getUniqueId(), sink};
	}

	public Object[] insertWarnsObjects(Warns warns) throws ConfigurateException {
		StringWriter sink = new StringWriter();
		HoconConfigurationLoader loader = HoconConfigurationLoader.builder().defaultOptions(options).sink(() -> new BufferedWriter(sink)).build();
		ConfigurationNode node = loader.createNode();
		node.node("Content").set(WarnsData.class, (WarnsData) (warns instanceof WarnsData ? warns : Warns.builder().from(warns)));
		loader.save(node);
		return new Object[] {warns.getUniqueId(), sink};
	}

	protected Ban.Profile profileFromString(String banData) {
		StringReader source = new StringReader(banData);
		HoconConfigurationLoader loader = SerializeOptions.createHoconConfigurationLoader(plugin.getMainConfig().getItemSerializer()).source(() -> new BufferedReader(source)).build();
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
		HoconConfigurationLoader loader = SerializeOptions.createHoconConfigurationLoader(plugin.getMainConfig().getItemSerializer()).source(() -> new BufferedReader(source)).build();
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
		HoconConfigurationLoader loader = SerializeOptions.createHoconConfigurationLoader(plugin.getMainConfig().getItemSerializer()).source(() -> new BufferedReader(source)).build();
		try {
			ConfigurationNode node = loader.load().node("Content");
			return node.virtual() ? null : node.get(MuteData.class);
		} catch (ConfigurateException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
			return null;
		}
	}
}
