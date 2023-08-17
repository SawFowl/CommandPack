package sawfowl.commandpack.apiclasses.punishment.storage;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.ban.Ban;
import org.spongepowered.api.service.ban.Ban.IP;
import org.spongepowered.api.service.ban.Ban.Profile;
import org.spongepowered.api.service.ban.BanTypes;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.api.data.punishment.Warns;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.configs.punishment.WarnsData;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.commandpack.utils.StorageType;
import sawfowl.commandpack.utils.TimeConverter;
import sawfowl.localeapi.api.TextUtils;

public class MySqlStorage extends SqlStorage {

	private String lastBan;
	private String lastBanIP;
	private String lastMute;
	private String lastWarns;
	private String selectBans;
	private String selectBansIP;
	private String selectMutes;
	private String selectWarns;
	private Connection selectConnection;
	private Connection deleteConnection;
	private Statement selectStatement;
	private Statement deleteStatement;
	private boolean isCombined = false;
	private boolean isUnixTime = false;
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
	public MySqlStorage(CommandPack plugin) {
		super(plugin);
		selectBans = selectAllProfileBansSql + " WHERE " + plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getUniqueId() + " = '";
		selectBansIP = isCombined ? selectBans : selectAllIPBansSql + " WHERE " + plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getIp() + " = '";
		selectMutes = selectAllMutesSql + " WHERE " + plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getUniqueId() + " = '";
		selectWarns = selectAllWarnsSql + " WHERE " + plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getUniqueId() + " = '";
		try {
			selectConnection = plugin.getMariaDB().get().createNewConnection();
			deleteConnection = plugin.getMariaDB().get().createNewConnection();
			sync();
		} catch (SQLException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

	@Override
	public StorageType getStorageType() {
		return StorageType.MYSQL;
	}

	@Override
	public boolean removeBeforeAdd() {
		return false;
	}

	public Connection getConnection() throws SQLException {
		return plugin.getMariaDB().get().getOrOpenConnection();
	}

	public String createProfileBansTableSql() {
		return isCombined ? "CREATE TABLE IF NOT EXISTS %bans%(%id% BIGINT UNSIGNED AUTO_INCREMENT UNIQUE, %uuid% VARCHAR(128) UNIQUE, %name% TEXT, %ip% VARCHAR(128), %source% TEXT, %created% %time%, %expiration% %time%, %reason% TEXT, %ipban% TINYINT(1) DEFAULT 0, written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY(%id%))" : "CREATE TABLE IF NOT EXISTS %bans%(%uuid% VARCHAR(128) UNIQUE, %name% TEXT, %source% TEXT, %created% %time%, expiration %time%, %reason% TEXT, written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY(%uuid%))";
	}

	public String createIPBansTableSql() {
		return isCombined ? replaceBansTableName("CREATE TABLE IF NOT EXISTS %bans%(%id% BIGINT UNSIGNED AUTO_INCREMENT UNIQUE, %uuid% VARCHAR(128) UNIQUE, %name% TEXT, %ip% VARCHAR(128), %source% TEXT, %created% %time%, %expiration% %time%, %reason% TEXT, %ipban% TINYINT(1) DEFAULT 0, written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY(%id%))") : replaceBansIpTableName("CREATE TABLE IF NOT EXISTS %bans_ip%(%ip% VARCHAR(128) UNIQUE, %source% TEXT, %created% %time%, %expiration% %time%, %reason% TEXT, written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY(%ip%))");
	}

	public String insertProfileBanSql() {
		return isCombined ? "REPLACE INTO %bans% (%uuid%, %name%, %ip%, %source%, %created%, %expiration%, %reason%, %ipban%) VALUES(?, ?, ?, ?, ?, ?, ?, ?)" : "REPLACE INTO %bans% (%uuid%, %name%, %source%, %created%, %expiration%, %reason%) VALUES(?, ?, ?, ?, ?, ?)";
	}

	public String insertIPBanSql() {
		return isCombined ? replaceBansTableName("REPLACE INTO %bans% (%uuid%, %name%, %ip%, %source%, %created%, %expiration%, %reason%, %ipban%) VALUES(?, ?, ?, ?, ?, ?, ?, ?)") : replaceBansIpTableName("REPLACE INTO %bans_ip% (%ip%, %source%, %created%, %expiration%, %reason%) VALUES(?, ?, ?, ?, ?)");
	}

	public void saveBans(Profile profile, IP ip) {
		if(plugin.getMainConfig().getPunishment().getDBSettings().isCreateCombinedBansTable()) {
			bans.put(profile.profile().uniqueId(), profile);
			try {
				if(removeBeforeAdd()) createStatement(deleteProfileBanSql, new Object[] {profile.profile().uniqueId()}).execute();;
				createStatement(insertProfileBanSql, insertBothBanObjects(profile, ip)).execute();
			} catch (SQLException e) {
				plugin.getLogger().error(e.getLocalizedMessage());
			}
		} else {
			if(profile != null) saveBan(profile);
			if(ip != null) saveBan(ip);
		}
	}

	protected void createTables() throws SQLException {

		String unixTimeFormatPlaceholder = "BIGINT";
		String dateTimeFormatPlaceholder = "DATETIME";
		isCombined = plugin.getMainConfig().getPunishment().getDBSettings().isCreateCombinedBansTable();
		isUnixTime = plugin.getMainConfig().getPunishment().getDBSettings().isUnixTime();
		createProfileBansTableSql = replaceCollumns(replaceBansTableName(createProfileBansTableSql().replace("%time%", isUnixTime ? unixTimeFormatPlaceholder : dateTimeFormatPlaceholder)));
		createIPBansTableSql = replaceCollumns(createIPBansTableSql().replace("%time%", isUnixTime ? unixTimeFormatPlaceholder : dateTimeFormatPlaceholder));
		createMutesTableSql = replaceCollumns(replaceMutesTableName("CREATE TABLE IF NOT EXISTS %mutes%(%uuid% VARCHAR(128) UNIQUE, %name% TEXT, %source% TEXT, %created% %time%, %expiration% %time%, %reason% TEXT, written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY(%uuid%))".replace("%time%", isUnixTime ? unixTimeFormatPlaceholder : dateTimeFormatPlaceholder)));
		createWarnsTableSql = replaceWarnsTableName("CREATE TABLE IF NOT EXISTS %warns%(uuid VARCHAR(128) UNIQUE, warns_data LONGTEXT, written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY(uuid))");
		selectAllProfileBansSql = replacePrimaryCollumns(replaceBansTableName("SELECT * FROM %bans%"));
		selectAllIPBansSql = replacePrimaryCollumns(replaceBansIpTableName("SELECT * FROM %bans_ip%"));
		selectAllMutesSql = replacePrimaryCollumns(replaceMutesTableName("SELECT * FROM %mutes%"));
		selectAllWarnsSql = replacePrimaryCollumns(replaceWarnsTableName("SELECT * FROM %warns%"));
		insertProfileBanSql = replaceCollumns(replaceBansTableName(insertProfileBanSql()));
		insertIPBanSql = replaceCollumns(insertIPBanSql());
		insertMuteSql = replaceCollumns(replaceMutesTableName("REPLACE INTO %mutes% (%uuid%, %name%, %source%, %created%, %expiration%, %reason%) VALUES(?, ?, ?, ?, ?, ?)"));
		insertWarnsSql = replaceWarnsTableName("REPLACE INTO %warns% (uuid, warns_data) VALUES(?, ?)");
		deleteProfileBanSql = replacePrimaryCollumns(replaceBansTableName("DELETE FROM %bans% WHERE %uuid% = ?"));
		deleteIPBanSql = replacePrimaryCollumns(replaceBansIpTableName("DELETE FROM %bans_ip% WHERE %ip% = ?"));
		deleteMuteSql = replacePrimaryCollumns(replaceMutesTableName("DELETE FROM %mutes% WHERE %uuid% = ?"));
		deleteWarnsSql = replaceWarnsTableName("DELETE FROM %warns% WHERE uuid = ?");

		executeSQL(createProfileBansTableSql);
		if(!plugin.getMainConfig().getPunishment().getDBSettings().isCreateCombinedBansTable()) executeSQL(createIPBansTableSql);
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
		String uuidString = results.getString(plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getUniqueId());
		String name = results.getString(plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getName());
		Ban.Builder builder = loadTexts(loadTimes(Ban.builder(), results), results);
		if(plugin.getMainConfig().getPunishment().getDBSettings().isCreateCombinedBansTable()) {
			if(results.getBoolean(plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getIpBan())) try {
				Ban.Builder ipBuilder = loadTexts(loadTimes(Ban.builder(), results), results);
				InetAddress ip = InetAddress.getByName(results.getString(plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getIp()));
				ipBuilder = ipBuilder.type(BanTypes.IP).address(ip);
				if(bansIP.containsKey(ip)) bansIP.remove(ip);
				IP ban = (IP) ipBuilder.build();
				if(ban.expirationDate().isPresent() && ban.expirationDate().get().toEpochMilli() < ban.creationDate().toEpochMilli()) return;
				bansIP.put(ip, (IP) ipBuilder.build());
				findIpKick(ip, ban);
			} catch (UnknownHostException | SQLException e) {
				plugin.getLogger().error(e.getLocalizedMessage());
			}
			if(uuidString == null || name == null || uuidString.equalsIgnoreCase("NULL") || name.equalsIgnoreCase("NULL")) return;
		}
		UUID uuid = UUID.fromString(uuidString);
		builder = builder.type(BanTypes.PROFILE).profile(GameProfile.of(uuid, name));
		if(bans.containsKey(uuid)) bans.remove(uuid);
		Profile ban = (Profile) builder.build();
		if(ban.expirationDate().isPresent() && ban.expirationDate().get().toEpochMilli() < ban.creationDate().toEpochMilli()) return;
		bans.put(uuid, ban);
		findProfileKick(uuid, ban);
	}

	private void findProfileKick(UUID uuid, Profile ban) {
		Optional<ServerPlayer> optPlayer = Sponge.server().player(uuid);
		if(!optPlayer.isPresent()) return;
		ServerPlayer player = optPlayer.get();
		kick(player, TextUtils.replaceToComponents(getText(player.locale(), ban.expirationDate().isPresent() ? LocalesPaths.COMMANDS_BAN_DISCONNECT : LocalesPaths.COMMANDS_BAN_DISCONNECT_PERMANENT), new String[] {Placeholders.TIME, Placeholders.SOURCE, Placeholders.VALUE}, new Component[] {(ban.expirationDate().isPresent() ? expire(player.locale(), ban) : text("")), ban.banSource().orElse(TextUtils.deserializeLegacy("&cServer")), ban.reason().orElse(TextUtils.deserializeLegacy("-"))}));
	}

	public void loadBanIP(ResultSet results) throws SQLException {
		if(plugin.getMainConfig().getPunishment().getDBSettings().isCreateCombinedBansTable()) return;
		try {
			InetAddress ip = InetAddress.getByName(results.getString(plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getIp()));
			IP.Builder builder = loadTexts(loadTimes(Ban.builder().type(BanTypes.IP).address(ip), results), results);
			if(bansIP.containsKey(ip)) bansIP.remove(ip);
			IP ban = (IP) builder.build();
			if(ban.expirationDate().isPresent() && ban.expirationDate().get().toEpochMilli() < ban.creationDate().toEpochMilli()) return;
			bansIP.put(ip, (IP) builder.build());
			findIpKick(ip, ban);
		} catch (UnknownHostException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
		}
	}

	private void findIpKick(InetAddress ip, IP ban) {
		Optional<ServerPlayer> optPlayer = Sponge.server().onlinePlayers().stream().filter(player -> player.connection().address().getAddress().getHostAddress().equals(ip.getHostAddress())).findFirst();
		if(!optPlayer.isPresent()) return;
		ServerPlayer player = optPlayer.get();
		kick(player, TextUtils.replaceToComponents(getText(player.locale(), ban.expirationDate().isPresent() ? LocalesPaths.COMMANDS_BAN_DISCONNECT : LocalesPaths.COMMANDS_BAN_DISCONNECT_PERMANENT), new String[] {Placeholders.TIME, Placeholders.SOURCE, Placeholders.VALUE}, new Component[] {(ban.expirationDate().isPresent() ? expire(player.locale(), ban) : text("")), ban.banSource().orElse(TextUtils.deserializeLegacy("&cServer")), ban.reason().orElse(TextUtils.deserializeLegacy("-"))}));
	}

	private Ban.Builder loadTimes(Ban.Builder builder, ResultSet results) throws SQLException {
		if(isUnixTime) {
			Long creation = results.getLong(plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getCreated());
			if(creation != null) builder = builder.startDate(Instant.ofEpochMilli(creation));
			Long expiration = results.getLong(plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getExpiration());
			if(expiration != null && expiration > 0) builder = builder.expirationDate(Instant.ofEpochMilli(expiration));
		} else {
			String creation = results.getString(plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getCreated());
			if(creation != null) builder = builder.startDate(TimeConverter.fromString(creation));
			String expirationString = results.getString(plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getExpiration());
			if(expirationString != null && !expirationString.equals(creation)) builder = builder.expirationDate(TimeConverter.fromString(expirationString));
		}
		return builder;
	}

	private Ban.Builder loadTexts(Ban.Builder builder, ResultSet results) throws SQLException {
		String source = results.getString(plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getSource());
		String reason = results.getString(plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getReason());
		if(source != null) builder = builder.source(text(source));
		if(reason != null) builder = builder.reason(text(reason));
		return builder;
	}

	public void loadMute(ResultSet results) throws SQLException {
		UUID uuid = UUID.fromString(results.getString(plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getUniqueId()));
		String name = results.getString(plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getName());
		String source = results.getString(plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getSource());
		String reason = results.getString(plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getReason());
		Mute.Builder builder = Mute.builder().target(uuid, name);
		if(source != null) builder = builder.source(TextUtils.deserialize(source));
		if(isUnixTime) {
			Long creation = results.getLong(plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getCreated());
			if(creation != null) builder = builder.created(Instant.ofEpochMilli(creation));
			Long expiration = results.getLong(plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getExpiration());
			if(expiration != null && expiration > 0) builder = builder.expiration(Instant.ofEpochMilli(expiration));
		} else {
			String creation = results.getString(plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getCreated());
			if(creation != null) builder = builder.created(TimeConverter.fromString(creation));
			String expirationString = results.getString(plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getExpiration());
			if(expirationString != null && !expirationString.equals(creation)) builder = builder.expiration(TimeConverter.fromString(expirationString));
		}
		if(reason != null) builder = builder.reason(TextUtils.deserialize(reason));
		if(mutes.containsKey(uuid)) mutes.remove(uuid);
		Mute mute = builder.build();
		if(mute.getExpiration().isPresent() && mute.getExpiration().get().toEpochMilli() < mute.getCreated().toEpochMilli()) return;
		mutes.put(uuid, mute);
	}

	public void loadWarns(ResultSet results) throws SQLException {
		UUID uuid = UUID.fromString(results.getString("uuid"));
		String warnsData = results.getString("warns_data");
		Warns warns = warnsFromString(warnsData);
		if(warns != null) {
			if(super.warns.containsKey(uuid)) super.warns.remove(uuid);
			super.warns.put(uuid, warns);}
		}

	public Object[] insertProfileBanObjects(Profile ban) throws ConfigurateException {
		return isCombined ? 
				new Object[] {
							ban.profile().uniqueId().toString(),
							ban.profile().name().orElse(ban.profile().examinableName()),
							"NULL",
							ban.banSource().map(TextUtils::serializeLegacy).orElse("n/a"),
							(isUnixTime ? String.valueOf(ban.creationDate().toEpochMilli()) : TimeConverter.toString(ban.creationDate())),
							(isUnixTime ? ban.expirationDate().map(Instant::toEpochMilli).map(Object::toString).orElse("0") : ban.expirationDate().map(instant -> TimeConverter.toString(instant)).orElse(TimeConverter.toString(ban.creationDate()))),
							ban.reason().map(TextUtils::serializeLegacy).orElse("n/a"),
							0
						}
				:
				new Object[] {
							ban.profile().uniqueId().toString(),
							ban.profile().name().orElse(ban.profile().examinableName()),
							ban.banSource().map(TextUtils::serializeLegacy).orElse("n/a"),
							(isUnixTime ? String.valueOf(ban.creationDate().toEpochMilli()) : TimeConverter.toString(ban.creationDate())),
							(isUnixTime ? ban.expirationDate().map(Instant::toEpochMilli).map(Object::toString).orElse("0") : ban.expirationDate().map(instant -> TimeConverter.toString(instant)).orElse(TimeConverter.toString(ban.creationDate()))),
							ban.reason().map(TextUtils::serializeLegacy).orElse("n/a")
						};
	}

	public Object[] insertIPBanObjects(IP ban) throws ConfigurateException {
		return isUnixTime ? new Object[] {
					"NULL",
					"NULL",
					ban.address().getHostAddress(),
					ban.banSource().map(TextUtils::serializeLegacy).orElse("n/a"),
					(isUnixTime ? String.valueOf(ban.creationDate().toEpochMilli()) : TimeConverter.toString(ban.creationDate())),
					(isUnixTime ? ban.expirationDate().map(Instant::toEpochMilli).map(Object::toString).orElse("0") : ban.expirationDate().map(instant -> TimeConverter.toString(instant)).orElse(TimeConverter.toString(ban.creationDate()))),
					ban.reason().map(TextUtils::serializeLegacy).orElse("n/a")
				}
				:
				new Object[] {
					ban.address().getHostAddress(),
					ban.banSource().map(TextUtils::serializeLegacy).orElse("n/a"),
					(isUnixTime ? String.valueOf(ban.creationDate().toEpochMilli()) : TimeConverter.toString(ban.creationDate())),
					(isUnixTime ? ban.expirationDate().map(Instant::toEpochMilli).map(Object::toString).orElse("0") : ban.expirationDate().map(instant -> TimeConverter.toString(instant)).orElse(TimeConverter.toString(ban.creationDate()))),
					ban.reason().map(TextUtils::serializeLegacy).orElse("n/a")
				};
	}

	private Object[] insertBothBanObjects(Profile profile, IP ip) {
		return new Object[] {
				profile.profile().uuid().toString(),
				profile.profile().name().orElse(profile.profile().examinableName()),
				ip.address().getHostAddress(),
				ip.banSource().map(TextUtils::serializeLegacy).orElse("n/a"),
				(isUnixTime ? String.valueOf(ip.creationDate().toEpochMilli()) : TimeConverter.toString(ip.creationDate())),
				(isUnixTime ? ip.expirationDate().map(Instant::toEpochMilli).map(Object::toString).orElse("0") : ip.expirationDate().map(instant -> TimeConverter.toString(instant)).orElse(TimeConverter.toString(ip.creationDate()))),
				ip.reason().map(TextUtils::serializeLegacy).orElse("n/a"),
				1
			};
	}

	public Object[] insertMuteObjects(Mute mute) throws ConfigurateException {
		return new Object[] {
				mute.getUniqueId().toString(),
				mute.getName(),
				mute.getSource().map(TextUtils::serializeLegacy).orElse("n/a"),
				(isUnixTime ? String.valueOf(mute.getCreated().toEpochMilli()) : mute.getCreatedTimeString()),
				(isUnixTime ? mute.getExpiration().map(Instant::toEpochMilli).map(Object::toString).orElse("0") : mute.getExpirationTimeString().orElse(mute.getCreatedTimeString())),
				mute.getReason().map(TextUtils::serializeLegacy).orElse("n/a")
				};
	}

	public Object[] insertWarnsObjects(Warns warns) throws ConfigurateException {
		StringWriter sink = new StringWriter();
		HoconConfigurationLoader loader = HoconConfigurationLoader.builder().defaultOptions(options).sink(() -> new BufferedWriter(sink)).build();
		ConfigurationNode node = loader.createNode();
		node.node("Content").set(WarnsData.class, (WarnsData) (warns instanceof WarnsData ? warns : Warns.builder().from(warns)));
		loader.save(node);
		return new Object[] {warns.getUniqueId(), sink};
	}

	private void sync() throws SQLException {
		for(String string : plugin.getMainConfig().getPunishment().getDBSettings().getTables().values()) {
			if(resultSet("SHOW TABLES LIKE '" + string + "'").next()) executeSQL("ALTER TABLE " + string + " ADD COLUMN IF NOT EXISTS written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;");
		}
		Sponge.asyncScheduler().submit(Task.builder().plugin(plugin.getPluginContainer()).interval(plugin.getMainConfig().getPunishment().getDBSettings().getSyncIntervals().getDelete(), TimeUnit.SECONDS).execute(() -> {
			try {
				checkBans();
				checkBansIP();
				checkMutes();
				checkWarns();
			} catch (SQLException e) {
				plugin.getLogger().error(e.getLocalizedMessage());
			}
		}).build());
		Sponge.asyncScheduler().submit(Task.builder().plugin(plugin.getPluginContainer()).interval(plugin.getMainConfig().getPunishment().getDBSettings().getSyncIntervals().getFindNew(), TimeUnit.SECONDS).execute(() -> {
			try {
				syncBans();
				if(!plugin.getMainConfig().getPunishment().getDBSettings().isCreateCombinedBansTable()) syncBansIP();
				syncMutes();
				syncWarns();
			} catch (SQLException e) {
				plugin.getLogger().error(e.getLocalizedMessage());
			}
		}).build());
	}

	private void checkBans() throws SQLException {
		for(UUID uuid : new ArrayList<>(bans.keySet())) {
			ResultSet results = resultSetDelete(selectBans + uuid.toString() + "'");
			if(!results.isClosed() && !results.next()) bans.remove(uuid);
		}
	}

	private void checkBansIP() throws SQLException {
		for(InetAddress address : new ArrayList<>(bansIP.keySet())) {
			ResultSet results = resultSetDelete(selectBansIP + address.getHostAddress() + "'");
			if(!results.isClosed() && !results.next()) bansIP.remove(address);
		}
	}

	private void checkMutes() throws SQLException {
		for(UUID uuid : new ArrayList<>(mutes.keySet())) {
			ResultSet results = resultSetDelete(selectMutes + uuid.toString() + "'");
			if(!results.isClosed() && !results.next()) mutes.remove(uuid);
		}
	}

	private void checkWarns() throws SQLException {
		for(UUID uuid : new ArrayList<>(warns.keySet())) {
			ResultSet results = resultSetDelete(selectWarns + uuid.toString() + "'");
			if(!results.isClosed() && !results.next()) warns.remove(uuid);
		}
	}

	private void syncBans() throws SQLException {
		if(lastBan == null) {
			ResultSet getBanLastTime = resultSetSelect(selectAllProfileBansSql + " ORDER BY written DESC LIMIT 1;");
			if(getBanLastTime.isClosed() || !getBanLastTime.next()) return;
			lastBan = getBanLastTime.getString("written");
			getBanLastTime = null;
		}
		ResultSet results = resultSetSelect(bans.isEmpty() || lastBan == null ? selectAllProfileBansSql : selectAllProfileBansSql + " WHERE written > '" + lastBan + "' ORDER BY written;");
		boolean update = false;
		while(!results.isClosed() && results.next()) {
			update = true;
			loadBanProfile(results);
		}
		if(bans.isEmpty() || !update) return;
		ResultSet getBanLastTime = resultSetSelect(selectAllProfileBansSql + " ORDER BY written DESC LIMIT 1;");
		if(getBanLastTime.isClosed() || !getBanLastTime.next()) return;
		lastBan = getBanLastTime.getString("written");
		getBanLastTime = null;
	}

	private void syncBansIP() throws SQLException {
		if(lastBanIP == null) {
			ResultSet getBanLastTime = resultSetSelect(selectAllIPBansSql + " ORDER BY written DESC LIMIT 1;");
			if(getBanLastTime.isClosed() || !getBanLastTime.next()) return;
			lastBanIP = getBanLastTime.getString("written");
			getBanLastTime = null;
		}
		plugin.getLogger().warn(selectAllIPBansSql);
		ResultSet results = resultSetSelect(bansIP.isEmpty() || lastBanIP == null ? selectAllIPBansSql : selectAllIPBansSql + " WHERE written > '" + lastBanIP + "' ORDER BY written;");
		boolean update = false;
		while(!results.isClosed() && results.next()) {
			update = true;
			loadBanIP(results);
		}
		if(bansIP.isEmpty() || !update) return;
		ResultSet getBanLastTime = resultSetSelect(selectAllIPBansSql + " ORDER BY written DESC LIMIT 1;");
		if(getBanLastTime.isClosed() || !getBanLastTime.next()) return;
		lastBanIP = getBanLastTime.getString("written");
		getBanLastTime = null;
	}

	private void syncMutes() throws SQLException {
		if(lastMute == null) {
			ResultSet getMuteLastTime = resultSetSelect(selectAllMutesSql + " ORDER BY written DESC LIMIT 1;");
			if(getMuteLastTime.isClosed() || !getMuteLastTime.next()) return;
			lastMute = getMuteLastTime.getString("written");
			getMuteLastTime = null;
		}
		ResultSet results = resultSetSelect(mutes.isEmpty() || lastMute == null ? selectAllMutesSql : selectAllMutesSql + " WHERE written > '" + lastMute + "' ORDER BY written;");
		boolean update = false;
		while(!results.isClosed() && results.next()) {
			update = true;
			loadMute(results);
		}
		if(mutes.isEmpty() || !update) return;
		ResultSet getMuteLastTime = resultSetSelect(selectAllMutesSql + " ORDER BY written DESC LIMIT 1;");
		if(getMuteLastTime.isClosed() || !getMuteLastTime.next()) return;
		lastMute = getMuteLastTime.getString("written");
		getMuteLastTime = null;
	}

	private void syncWarns() throws SQLException {
		if(lastWarns == null) {
			ResultSet getWarnsLastTime = resultSetSelect(selectAllWarnsSql + " ORDER BY written DESC LIMIT 1;");
			if(getWarnsLastTime.isClosed() || !getWarnsLastTime.next()) return;
			lastWarns = getWarnsLastTime.getString("written");
			getWarnsLastTime = null;
		}
		ResultSet results = resultSetSelect(warns.isEmpty() || lastWarns == null ? selectAllWarnsSql : selectAllWarnsSql + " WHERE written > '" + lastWarns + "' ORDER BY written;");
		boolean update = false;
		while(!results.isClosed() && results.next()) {
			update = true;
			loadWarns(results);
		}
		if(warns.isEmpty() || !update) return;
		ResultSet getBanLastTime = resultSetSelect(selectAllWarnsSql + " ORDER BY written DESC LIMIT 1;");
		if(getBanLastTime.isClosed() || !getBanLastTime.next()) return;
		lastWarns = getBanLastTime.getString("written");
		getBanLastTime = null;
	}

	private ResultSet resultSetSelect(String sql) throws SQLException {
		if(selectConnection == null || selectConnection.isClosed()) selectConnection = plugin.getMariaDB().get().createNewConnection();
		if(selectStatement == null || selectStatement.isClosed()) selectStatement = selectConnection.createStatement();
		ResultSet result = selectStatement.executeQuery(sql);
		return result;
	}

	private ResultSet resultSetDelete(String sql) throws SQLException {
		if(deleteConnection == null || deleteConnection.isClosed()) deleteConnection = plugin.getMariaDB().get().createNewConnection();
		if(deleteStatement == null || deleteStatement.isClosed()) deleteStatement = deleteConnection.createStatement();
		ResultSet result = deleteStatement.executeQuery(sql);
		return result;
	}

	private Component expire(Locale locale, org.spongepowered.api.service.ban.Ban ban) {
		if(!ban.expirationDate().isPresent()) return Component.empty();
		SimpleDateFormat format = new SimpleDateFormat(getString(locale, LocalesPaths.COMMANDS_SERVERSTAT_TIMEFORMAT));
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(ban.expirationDate().get().toEpochMilli());
		return text(format.format(calendar.getTime()));
	}

	protected String getString(Locale locale, Object[] path) {
		return plugin.getLocales().getString(locale, path);
	}

	private Component text(String string) {
		if(isLegacyDecor(string)) {
			return TextUtils.deserializeLegacy(string);
		} else {
			return TextUtils.deserialize(string);
		}
	}

	private boolean isLegacyDecor(String string) {
		return string.indexOf('&') != -1 && !string.endsWith("&") && isStyleChar(string.charAt(string.indexOf("&") + 1));
	}

	private boolean isStyleChar(char ch) {
		return "0123456789abcdefklmnor".indexOf(ch) != -1;
	}

	private Component getText(Locale locale, Object... path) {
		return plugin.getLocales().getText(locale, path);
	}

	private void kick(ServerPlayer player, Component message) {
		Sponge.server().scheduler().executor(plugin.getPluginContainer()).execute(() -> {
			player.kick(message);
		});
	}

	private String replaceBansTableName(String banQuery) {
		return banQuery.replaceFirst("%bans%", plugin.getMainConfig().getPunishment().getDBSettings().getTables().getBans());
	}

	private String replaceBansIpTableName(String banQuery) {
		return banQuery.replaceFirst("%bans_ip%", isCombined ? plugin.getMainConfig().getPunishment().getDBSettings().getTables().getBans() : plugin.getMainConfig().getPunishment().getDBSettings().getTables().getIpbans());
	}

	private String replaceMutesTableName(String banQuery) {
		return banQuery.replaceFirst("%mutes%", plugin.getMainConfig().getPunishment().getDBSettings().getTables().getMutes());
	}

	private String replaceWarnsTableName(String banQuery) {
		return banQuery.replaceFirst("%warns%", plugin.getMainConfig().getPunishment().getDBSettings().getTables().getWarns());
	}

	private String replacePrimaryCollumns(String query) {
		return query.replace("%id%", plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getId())
				.replace("%uuid%", plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getUniqueId())
				.replace("%ip%", plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getIp());
	}

	private String replaceCollumns(String query) {
		return replacePrimaryCollumns(query)
				.replaceFirst("%name%", plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getName())
				.replaceFirst("%source%", plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getSource())
				.replaceFirst("%created%", plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getCreated())
				.replaceFirst("%expiration%", plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getExpiration())
				.replaceFirst("%reason%", plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getReason())
				.replaceFirst("%ipban%", plugin.getMainConfig().getPunishment().getDBSettings().getColumns().getIpBan());
	}

}
