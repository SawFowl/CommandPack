package sawfowl.commandpack.apiclasses.punishment.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

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

public class H2Storage extends SqlStorage {

	public H2Storage(CommandPack plugin) {
		super(plugin);
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
	public String createProfileBansTableSql() {
		return "CREATE TABLE IF NOT EXISTS BANS(UUID VARCHAR(128) UNIQUE, BAN_DATA LONGTEXT, PRIMARY KEY(UUID))";
	}

	@Override
	public String createIPBansTableSql() {
		return "CREATE TABLE IF NOT EXISTS BANS_IP(IP VARCHAR(128) UNIQUE, BAN_DATA LONGTEXT, PRIMARY KEY(IP))";
	}

	@Override
	public String createMutesTableSql() {
		return "CREATE TABLE IF NOT EXISTS MUTES(UUID VARCHAR(128) UNIQUE, MUTE_DATA LONGTEXT, PRIMARY KEY(UUID))";
	}

	@Override
	public String createWarnsTableSql() {
		return "CREATE TABLE IF NOT EXISTS WARNS(UUID VARCHAR(128) UNIQUE, WARNS_DATA LONGTEXT, PRIMARY KEY(UUID))";
	}

	@Override
	public String selectAllProfileBansSql() {
		return "SELECT * FROM BANS";
	}

	@Override
	public String selectAllIPBansSql() {
		return "SELECT * FROM BANS_IP";
	}

	@Override
	public String selectAllMutesSql() {
		return "SELECT * FROM MUTES";
	}

	@Override
	public String selectAllWarnsSql() {
		return "SELECT * FROM WARNS";
	}

	@Override
	public String insertProfileBanSql() {
		return "INSERT INTO BANS (UUID, BAN_DATA) VALUES(?, ?)";
	}

	@Override
	public String insertIPBanSql() {
		return "INSERT INTO BANS_IP (UUID, BAN_DATA) VALUES(?, ?)";
	}

	@Override
	public String insertMuteSql() {
		return "INSERT INTO MUTES (UUID, MUTE_DATA) VALUES(?, ?)";
	}

	@Override
	public String insertWarnsSql() {
		return "INSERT INTO WARNS (UUID, WARNS_DATA) VALUES(?, ?)";
	}

	@Override
	public String deleteProfileBanSql() {
		return "DELETE FROM BANS WHERE UUID = ?";
	}

	@Override
	public String deleteIPBanSql() {
		return "DELETE FROM BANS_IP WHERE IP = ?";
	}

	@Override
	public String deleteMuteSql() {
		return "DELETE FROM MUTES WHERE UUID = ?";
	}

	@Override
	public String deleteWarnsSql() {
		return "DELETE FROM WARNS WHERE UUID = ?";
	}

	@Override
	public void loadBanProfile(ResultSet results) throws SQLException {
		UUID uuid = UUID.fromString(results.getString("UUID"));
		String banData = results.getString("BAN_DATA");
		Ban.Profile ban = profileFromString(banData);
		if(ban != null) bans.put(uuid, ban);
	}

	@Override
	public void loadBanIP(ResultSet results) throws SQLException {
		String banData = results.getString("BAN_DATA");
		Ban.IP ban = ipFromString(banData);
		if(ban != null) bansIP.put(ban.address(), ban);
	}

	@Override
	public void loadMute(ResultSet results) throws SQLException {
		UUID uuid = UUID.fromString(results.getString("UUID"));
		String muteData = results.getString("MUTE_DATA");
		Mute mute = muteFromString(muteData);
		if(mute != null) mutes.put(uuid, mute);
	}

	@Override
	public void loadWarns(ResultSet results) throws SQLException {
		UUID uuid = UUID.fromString(results.getString("UUID"));
		String warnsData = results.getString("WARNS_DATA");
		Warns warns = warnsFromString(warnsData);
		if(warns != null) super.warns.put(uuid, warns);
	}

	@Override
	public Object[] insertProfileBanObjects(Profile ban) throws ConfigurateException {
		StringWriter sink = new StringWriter();
		HoconConfigurationLoader loader = HoconConfigurationLoader.builder().defaultOptions(options).sink(() -> new BufferedWriter(sink)).build();
		ConfigurationNode node = loader.createNode();
		node.node("Content").set(BanData.class, new BanData(ban));
		loader.save(node);
		return new Object[] {ban.profile().uniqueId(), sink};
	}

	@Override
	public Object[] insertIPBanObjects(IP ban) throws ConfigurateException {
		StringWriter sink = new StringWriter();
		HoconConfigurationLoader loader = HoconConfigurationLoader.builder().defaultOptions(options).sink(() -> new BufferedWriter(sink)).build();
		ConfigurationNode node = loader.createNode();
		node.node("Content").set(BanData.class, new BanData(ban));
		loader.save(node);
		return new Object[] {ban.address().getHostAddress(), sink};
	}

	@Override
	public Object[] insertMuteObjects(Mute mute) throws ConfigurateException {
		StringWriter sink = new StringWriter();
		HoconConfigurationLoader loader = HoconConfigurationLoader.builder().defaultOptions(options).sink(() -> new BufferedWriter(sink)).build();
		ConfigurationNode node = loader.createNode();
		node.node("Content").set(MuteData.class, (MuteData) (mute instanceof MuteData ? mute : Mute.builder().from(mute)));
		loader.save(node);
		return new Object[] {mute.getUniqueId(), sink};
	}

	@Override
	public Object[] insertWarnsObjects(Warns warns) throws ConfigurateException {
		StringWriter sink = new StringWriter();
		HoconConfigurationLoader loader = HoconConfigurationLoader.builder().defaultOptions(options).sink(() -> new BufferedWriter(sink)).build();
		ConfigurationNode node = loader.createNode();
		node.node("Content").set(WarnsData.class, (WarnsData) (warns instanceof WarnsData ? warns : Warns.builder().from(warns)));
		loader.save(node);
		return new Object[] {warns.getUniqueId(), sink};
	}

}
