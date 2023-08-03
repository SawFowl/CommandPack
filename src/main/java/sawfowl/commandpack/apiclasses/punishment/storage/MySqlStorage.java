package sawfowl.commandpack.apiclasses.punishment.storage;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;
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
	private TimeZone timeZone;
	private DateTimeFormatter formatter;
	private DateFormat dateFormat;
	public MySqlStorage(CommandPack plugin) {
		super(plugin);
		selectBans = "SELECT * FROM " + plugin.getMainConfig().getPunishment().getMySqlQueries().getTables().get("bans");
		selectBansIP = "SELECT * FROM " + plugin.getMainConfig().getPunishment().getMySqlQueries().getTables().get(plugin.getMainConfig().getPunishment().getMySqlQueries().isCreateCombinedBansTable() ? "bans" : "bans_ip");
		selectMutes = "SELECT * FROM " + plugin.getMainConfig().getPunishment().getMySqlQueries().getTables().get("mutes");
		selectWarns = "SELECT * FROM " + plugin.getMainConfig().getPunishment().getMySqlQueries().getTables().get("warns");
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
	public void setFormatter() {
		timeZone = plugin.getMainConfig().getPunishment().getTimeZone();
		dateFormat = plugin.getMainConfig().getPunishment().createDateTimeFormat();
		dateFormat.setTimeZone(timeZone);
		formatter = DateTimeFormatter.ofPattern(plugin.getMainConfig().getPunishment().getDateTimeFormat()).withZone(timeZone.toZoneId());
	}

	@Override
	public boolean removeBeforeAdd() {
		return false;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return plugin.getMariaDB().get().getOrOpenConnection();
	}

	@Override
	public String createProfileBansTableSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().isCreateCombinedBansTable() ? plugin.getMainConfig().getPunishment().getMySqlQueries().getCreateTables().createCombinedBansSql() : plugin.getMainConfig().getPunishment().getMySqlQueries().getCreateTables().createProfileBansTableSql();
	}

	@Override
	public String createIPBansTableSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().isCreateCombinedBansTable() ? plugin.getMainConfig().getPunishment().getMySqlQueries().getCreateTables().createCombinedBansSql() : plugin.getMainConfig().getPunishment().getMySqlQueries().getCreateTables().createIPBansTableSql();
	}

	@Override
	public String createMutesTableSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().getCreateTables().createMutesTableSql();
	}

	@Override
	public String createWarnsTableSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().getCreateTables().createWarnsTableSql();
	}

	@Override
	public String selectAllProfileBansSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().getSelect().selectAllProfileBansSql();
	}

	@Override
	public String selectAllIPBansSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().getSelect().selectAllIPBansSql();
	}

	@Override
	public String selectAllMutesSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().getSelect().selectAllMutesSql();
	}

	@Override
	public String selectAllWarnsSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().getSelect().selectAllWarnsSql();
	}

	@Override
	public String insertProfileBanSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().isCreateCombinedBansTable() ? plugin.getMainConfig().getPunishment().getMySqlQueries().getInsert().getInsertCombitedBanSql() : plugin.getMainConfig().getPunishment().getMySqlQueries().getInsert().insertProfileBanSql();
	}

	@Override
	public String insertIPBanSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().isCreateCombinedBansTable() ? plugin.getMainConfig().getPunishment().getMySqlQueries().getInsert().getInsertCombitedBanSql() : plugin.getMainConfig().getPunishment().getMySqlQueries().getInsert().insertIPBanSql();
	}

	@Override
	public String insertMuteSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().getInsert().insertMuteSql();
	}

	@Override
	public String insertWarnsSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().getInsert().insertWarnsSql();
	}

	@Override
	public String deleteProfileBanSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().getDelete().deleteProfileBanSql();
	}

	@Override
	public String deleteIPBanSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().getDelete().deleteIPBanSql();
	}

	@Override
	public String deleteMuteSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().getDelete().deleteMuteSql();
	}

	@Override
	public String deleteWarnsSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().getDelete().deleteWarnsSql();
	}

	public void saveBans(Profile profile, IP ip) {
		if(plugin.getMainConfig().getPunishment().getMySqlQueries().isCreateCombinedBansTable()) {
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

	@Override
	public void loadBanProfile(ResultSet results) throws SQLException {
		String uuidString = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getUniqueId());
		String name = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getName());
		Ban.Builder builder = loadTexts(loadTimes(Ban.builder(), results), results);
		if(plugin.getMainConfig().getPunishment().getMySqlQueries().isCreateCombinedBansTable()) {
			if(results.getBoolean(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getIpBan())) try {
				Ban.Builder ipBuilder = loadTexts(loadTimes(Ban.builder(), results), results);
				InetAddress ip = InetAddress.getByName(results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getIp()));
				ipBuilder = ipBuilder.type(BanTypes.IP).address(ip);
				if(bansIP.containsKey(ip)) bansIP.remove(ip);
				IP ban = (IP) ipBuilder.build();
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
		bans.put(uuid, ban);
		findProfileKick(uuid, ban);
	}

	private void findProfileKick(UUID uuid, Profile ban) {
		Optional<ServerPlayer> optPlayer = Sponge.server().player(uuid);
		if(!optPlayer.isPresent()) return;
		ServerPlayer player = optPlayer.get();
		kick(player, TextUtils.replaceToComponents(getText(player.locale(), ban.expirationDate().isPresent() ? LocalesPaths.COMMANDS_BAN_DISCONNECT : LocalesPaths.COMMANDS_BAN_DISCONNECT_PERMANENT), new String[] {Placeholders.TIME, Placeholders.SOURCE, Placeholders.VALUE}, new Component[] {(ban.expirationDate().isPresent() ? expire(player.locale(), ban) : text("")), ban.banSource().orElse(TextUtils.deserializeLegacy("&cServer")), ban.reason().orElse(TextUtils.deserializeLegacy("-"))}));
	}

	@Override
	public void loadBanIP(ResultSet results) throws SQLException {
		if(plugin.getMainConfig().getPunishment().getMySqlQueries().isCreateCombinedBansTable()) return;
		try {
			InetAddress ip = InetAddress.getByName(results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getIp()));
			IP.Builder builder = loadTexts(loadTimes(Ban.builder().type(BanTypes.IP).address(ip), results), results);
			if(bansIP.containsKey(ip)) bansIP.remove(ip);
			IP ban = (IP) builder.build();
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
		if(plugin.getMainConfig().getPunishment().getMySqlQueries().getCreateTables().isUnixTime()) {
			Long creation = results.getLong(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getCreated());
			if(creation != null) builder = builder.startDate(Instant.ofEpochMilli(creation));
			Long expiration = results.getLong(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getExpiration());
			if(expiration != null && expiration > 0) builder = builder.expirationDate(Instant.ofEpochMilli(expiration));
		} else {
			String creation = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getCreated());
			if(creation != null) builder = builder.startDate(LocalDateTime.parse(creation, formatter).atZone(timeZone.toZoneId()).toInstant());
			String expirationString = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getExpiration());
			if(expirationString != null && !expirationString.equals(creation)) builder = builder.expirationDate(LocalDateTime.parse(expirationString, formatter).atZone(timeZone.toZoneId()).toInstant());
		}
		return builder;
	}

	private Ban.Builder loadTexts(Ban.Builder builder, ResultSet results) throws SQLException {
		String source = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getSource());
		String reason = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getReason());
		if(source != null) builder = builder.source(text(source));
		if(reason != null) builder = builder.reason(text(reason));
		return builder;
	}

	@Override
	public void loadMute(ResultSet results) throws SQLException {
		UUID uuid = UUID.fromString(results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getUniqueId()));
		String name = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getName());
		String source = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getSource());
		String reason = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getReason());
		Mute.Builder builder = Mute.builder().target(uuid, name);
		if(source != null) builder = builder.source(TextUtils.deserialize(source));
		if(plugin.getMainConfig().getPunishment().getMySqlQueries().getCreateTables().isUnixTime()) {
			Long creation = results.getLong(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getCreated());
			if(creation != null) builder = builder.created(Instant.ofEpochMilli(creation));
			Long expiration = results.getLong(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getExpiration());
			if(expiration != null && expiration > 0) builder = builder.expiration(Instant.ofEpochMilli(expiration));
		} else {
			String creation = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getCreated());
			if(creation != null) builder = builder.created(LocalDateTime.parse(creation, formatter).atZone(timeZone.toZoneId()).toInstant());
			String expirationString = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getExpiration());
			if(expirationString != null && !expirationString.equals(creation)) builder = builder.expiration(LocalDateTime.parse(expirationString, formatter).atZone(timeZone.toZoneId()).toInstant());
		}
		if(reason != null) builder = builder.reason(TextUtils.deserialize(reason));
		if(mutes.containsKey(uuid)) mutes.remove(uuid);
		mutes.put(uuid, builder.build());
	}

	@Override
	public void loadWarns(ResultSet results) throws SQLException {
		UUID uuid = UUID.fromString(results.getString("uuid"));
		String warnsData = results.getString("warns_data");
		Warns warns = warnsFromString(warnsData);
		if(warns != null) {
			if(super.warns.containsKey(uuid)) super.warns.remove(uuid);
			super.warns.put(uuid, warns);}
		}

	@Override
	public Object[] insertProfileBanObjects(Profile ban) throws ConfigurateException {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().isCreateCombinedBansTable() ? 
				plugin.getMainConfig().getPunishment().getMySqlQueries().getPatterns().getCombinetBan()
				.replace("%uuid%", ban.profile().uniqueId().toString())
				.replace("%name%", ban.profile().name().orElse(ban.profile().examinableName()))
				.replace("%ip%", "NULL")
				.replace("%source%", ban.banSource().map(TextUtils::serializeLegacy).orElse("n/a"))
				.replace("%creation%", plugin.getMainConfig().getPunishment().getMySqlQueries().getCreateTables().isUnixTime() ? String.valueOf(ban.creationDate().toEpochMilli()) : dateFormat.format(Timestamp.from(ban.creationDate())))
				.replace("%expiration%", plugin.getMainConfig().getPunishment().getMySqlQueries().getCreateTables().isUnixTime() ? ban.expirationDate().map(Instant::toEpochMilli).map(Object::toString).orElse("0") : ban.expirationDate().map(instant -> dateFormat.format(Timestamp.from(instant))).orElse(dateFormat.format(Timestamp.from(ban.creationDate()))))
				.replace("%reason%", ban.reason().map(TextUtils::serializeLegacy).orElse("n/a"))
				.replace("%ipban%", "0").split("><")
				:
				plugin.getMainConfig().getPunishment().getMySqlQueries().getPatterns().getBan()
				.replace("%uuid%", ban.profile().uniqueId().toString())
				.replace("%name%", ban.profile().name().orElse(ban.profile().examinableName()))
				.replace("%source%", ban.banSource().map(TextUtils::serializeLegacy).orElse("n/a"))
				.replace("%creation%", plugin.getMainConfig().getPunishment().getMySqlQueries().getCreateTables().isUnixTime() ? String.valueOf(ban.creationDate().toEpochMilli()) : dateFormat.format(Timestamp.from(ban.creationDate())))
				.replace("%expiration%", plugin.getMainConfig().getPunishment().getMySqlQueries().getCreateTables().isUnixTime() ? ban.expirationDate().map(Instant::toEpochMilli).map(Object::toString).orElse("0") : ban.expirationDate().map(instant -> dateFormat.format(Timestamp.from(instant))).orElse(dateFormat.format(Timestamp.from(ban.creationDate()))))
				.replace("%reason%", ban.reason().map(TextUtils::serializeLegacy).orElse("n/a")).split("><");
	}

	@Override
	public Object[] insertIPBanObjects(IP ban) throws ConfigurateException {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().isCreateCombinedBansTable() ? 
				plugin.getMainConfig().getPunishment().getMySqlQueries().getPatterns().getCombinetBan()
				.replace("%uuid%", "NULL")
				.replace("%name%", "NULL")
				.replace("%ip%", ban.address().getHostAddress())
				.replace("%source%", ban.banSource().map(TextUtils::serializeLegacy).orElse("n/a"))
				.replace("%creation%", plugin.getMainConfig().getPunishment().getMySqlQueries().getCreateTables().isUnixTime() ? String.valueOf(ban.creationDate().toEpochMilli()) : dateFormat.format(Timestamp.from(ban.creationDate())))
				.replace("%expiration%", plugin.getMainConfig().getPunishment().getMySqlQueries().getCreateTables().isUnixTime() ? ban.expirationDate().map(Instant::toEpochMilli).map(Object::toString).orElse("0") : ban.expirationDate().map(instant -> dateFormat.format(Timestamp.from(instant))).orElse(dateFormat.format(Timestamp.from(ban.creationDate()))))
				.replace("%reason%", ban.reason().map(TextUtils::serializeLegacy).orElse("n/a"))
				.replace("%ipban%", "1").split("><")
				:
				plugin.getMainConfig().getPunishment().getMySqlQueries().getPatterns().getBanIP()
				.replace("%ip%", ban.address().getHostAddress())
				.replace("%source%", ban.banSource().map(TextUtils::serializeLegacy).orElse("n/a"))
				.replace("%creation%", plugin.getMainConfig().getPunishment().getMySqlQueries().getCreateTables().isUnixTime() ? String.valueOf(ban.creationDate().toEpochMilli()) : dateFormat.format(Timestamp.from(ban.creationDate())))
				.replace("%expiration%", plugin.getMainConfig().getPunishment().getMySqlQueries().getCreateTables().isUnixTime() ? ban.expirationDate().map(Instant::toEpochMilli).map(Object::toString).orElse("0") : ban.expirationDate().map(instant -> dateFormat.format(Timestamp.from(instant))).orElse(dateFormat.format(Timestamp.from(ban.creationDate()))))
				.replace("%reason%", ban.reason().map(TextUtils::serializeLegacy).orElse("n/a")).split("><");
	}

	private Object[] insertBothBanObjects(Profile profile, IP ip) {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().getPatterns().getCombinetBan()
				.replace("%uuid%", profile.profile().uuid().toString())
				.replace("%name%", profile.profile().name().orElse(profile.profile().examinableName()))
				.replace("%ip%", ip.address().getHostAddress())
				.replace("%source%", ip.banSource().map(TextUtils::serializeLegacy).orElse("n/a"))
				.replace("%creation%", plugin.getMainConfig().getPunishment().getMySqlQueries().getCreateTables().isUnixTime() ? String.valueOf(ip.creationDate().toEpochMilli()) : dateFormat.format(Timestamp.from(ip.creationDate())))
				.replace("%expiration%", plugin.getMainConfig().getPunishment().getMySqlQueries().getCreateTables().isUnixTime() ? ip.expirationDate().map(Instant::toEpochMilli).map(Object::toString).orElse("0") : ip.expirationDate().map(instant -> dateFormat.format(Timestamp.from(instant))).orElse(dateFormat.format(Timestamp.from(profile.creationDate()))))
				.replace("%reason%", ip.reason().map(TextUtils::serializeLegacy).orElse("n/a"))
				.replace("%ipban%", "1").split("><");
	}

	@Override
	public Object[] insertMuteObjects(Mute mute) throws ConfigurateException {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().getPatterns().getBan()
				.replace("%uuid%", mute.getUniqueId().toString())
				.replace("%name%", mute.getName())
				.replace("%source%", mute.getSource().map(TextUtils::serializeLegacy).orElse("n/a"))
				.replace("%creation%", plugin.getMainConfig().getPunishment().getMySqlQueries().getCreateTables().isUnixTime() ? String.valueOf(mute.getCreated().toEpochMilli()) : mute.getCreatedTimeString())
				.replace("%expiration%", plugin.getMainConfig().getPunishment().getMySqlQueries().getCreateTables().isUnixTime() ? mute.getExpiration().map(Instant::toEpochMilli).map(Object::toString).orElse("0") : mute.getExpirationTimeString().orElse(mute.getCreatedTimeString()))
				.replace("%reason%", mute.getReason().map(TextUtils::serializeLegacy).orElse("n/a")).split("><");
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

	private void sync() throws SQLException {
		for(String string : plugin.getMainConfig().getPunishment().getMySqlQueries().getTables().values()) {
			if(resultSet("SHOW TABLES LIKE '" + string + "'").next()) executeSQL("ALTER TABLE " + string + " ADD COLUMN IF NOT EXISTS written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;");
		}
		Sponge.asyncScheduler().submit(Task.builder().plugin(plugin.getPluginContainer()).interval(plugin.getMainConfig().getPunishment().getMySqlQueries().getSyncIntervals().getDelete(), TimeUnit.SECONDS).execute(() -> {
			try {
				checkBans();
				checkBansIP();
				checkMutes();
				checkWarns();
			} catch (SQLException e) {
				plugin.getLogger().error(e.getLocalizedMessage());
			}
		}).build());
		Sponge.asyncScheduler().submit(Task.builder().plugin(plugin.getPluginContainer()).interval(plugin.getMainConfig().getPunishment().getMySqlQueries().getSyncIntervals().getFindNew(), TimeUnit.SECONDS).execute(() -> {
			try {
				syncBans();
				if(!plugin.getMainConfig().getPunishment().getMySqlQueries().isCreateCombinedBansTable()) syncBansIP();
				syncMutes();
				syncWarns();
			} catch (SQLException e) {
				plugin.getLogger().error(e.getLocalizedMessage());
			}
		}).build());
	}

	private void checkBans() throws SQLException {
		for(UUID uuid : new ArrayList<>(bans.keySet())) {
			ResultSet results = resultSetDelete(selectBans + " WHERE " + plugin.getMainConfig().getPunishment().getMySqlQueries().getIndexes().get("uuid") + " = '" + uuid.toString() + "'");
			if(!results.isClosed() && !results.next()) bans.remove(uuid);
		}
	}

	private void checkBansIP() throws SQLException {
		for(InetAddress address : new ArrayList<>(bansIP.keySet())) {
			ResultSet results = resultSetDelete(selectBansIP + " WHERE " + plugin.getMainConfig().getPunishment().getMySqlQueries().getIndexes().get("ip") + " = '" + address.getHostAddress() + "'");
			if(!results.isClosed() && !results.next()) bansIP.remove(address);
		}
	}

	private void checkMutes() throws SQLException {
		for(UUID uuid : new ArrayList<>(mutes.keySet())) {
			ResultSet results = resultSetDelete(selectMutes + " WHERE " + plugin.getMainConfig().getPunishment().getMySqlQueries().getIndexes().get("uuid") + " = '" + uuid.toString() + "'");
			if(!results.isClosed() && !results.next()) mutes.remove(uuid);
		}
	}

	private void checkWarns() throws SQLException {
		for(UUID uuid : new ArrayList<>(warns.keySet())) {
			ResultSet results = resultSetDelete(selectWarns + " WHERE " + plugin.getMainConfig().getPunishment().getMySqlQueries().getIndexes().get("uuid") + " = '" + uuid.toString() + "'");
			if(!results.isClosed() && !results.next()) warns.remove(uuid);
		}
	}

	private void syncBans() throws SQLException {
		if(lastBan == null) {
			ResultSet getBanLastTime = resultSetSelect(selectBans + " ORDER BY written DESC LIMIT 1;");
			if(getBanLastTime.isClosed() || !getBanLastTime.next()) return;
			lastBan = getBanLastTime.getString("written");
			getBanLastTime = null;
		}
		ResultSet results = resultSetSelect(bans.isEmpty() || lastBan == null ? selectBans : selectBans + " WHERE written > '" + lastBan + "';");
		boolean update = false;
		while(!results.isClosed() && results.next()) {
			update = true;
			loadBanProfile(results);
		}
		if(bans.isEmpty() || !update) return;
		ResultSet getBanLastTime = resultSetSelect(selectBans + " ORDER BY written DESC LIMIT 1;");
		if(getBanLastTime.isClosed() || !getBanLastTime.next()) return;
		lastBan = getBanLastTime.getString("written");
		getBanLastTime = null;
	}

	private void syncBansIP() throws SQLException {
		if(lastBanIP == null) {
			ResultSet getBanLastTime = resultSetSelect(selectBansIP + " ORDER BY written DESC LIMIT 1;");
			if(getBanLastTime.isClosed() || !getBanLastTime.next()) return;
			lastBanIP = getBanLastTime.getString("written");
			getBanLastTime = null;
		}
		ResultSet results = resultSetSelect(bansIP.isEmpty() || lastBanIP == null ? selectBansIP : selectBansIP + " WHERE written > '" + lastBanIP + "';");
		boolean update = false;
		while(!results.isClosed() && results.next()) {
			update = true;
			loadBanIP(results);
		}
		if(bansIP.isEmpty() || !update) return;
		ResultSet getBanLastTime = resultSetSelect(selectBansIP + " ORDER BY written DESC LIMIT 1;");
		if(getBanLastTime.isClosed() || !getBanLastTime.next()) return;
		lastBanIP = getBanLastTime.getString("written");
		getBanLastTime = null;
	}

	private void syncMutes() throws SQLException {
		if(lastMute == null) {
			ResultSet getMuteLastTime = resultSetSelect(selectMutes + " ORDER BY written DESC LIMIT 1;");
			if(getMuteLastTime.isClosed() || !getMuteLastTime.next()) return;
			lastMute = getMuteLastTime.getString("written");
			getMuteLastTime = null;
		}
		ResultSet results = resultSetSelect(mutes.isEmpty() || lastMute == null ? selectMutes : selectMutes + " WHERE written > '" + lastMute + "';");
		boolean update = false;
		while(!results.isClosed() && results.next()) {
			update = true;
			loadMute(results);
		}
		if(mutes.isEmpty() || !update) return;
		ResultSet getMuteLastTime = resultSetSelect(selectMutes + " ORDER BY written DESC LIMIT 1;");
		if(getMuteLastTime.isClosed() || !getMuteLastTime.next()) return;
		lastMute = getMuteLastTime.getString("written");
		getMuteLastTime = null;
	}

	private void syncWarns() throws SQLException {
		if(lastWarns == null) {
			ResultSet getWarnsLastTime = resultSetSelect(selectWarns + " ORDER BY written DESC LIMIT 1;");
			if(getWarnsLastTime.isClosed() || !getWarnsLastTime.next()) return;
			lastWarns = getWarnsLastTime.getString("written");
			getWarnsLastTime = null;
		}
		ResultSet results = resultSetSelect(warns.isEmpty() || lastWarns == null ? selectWarns : selectWarns + " WHERE written > '" + lastWarns + "';");
		boolean update = false;
		while(!results.isClosed() && results.next()) {
			update = true;
			loadWarns(results);
		}
		if(warns.isEmpty() || !update) return;
		ResultSet getBanLastTime = resultSetSelect(selectWarns + " ORDER BY written DESC LIMIT 1;");
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
		return CommandPack.getInstance().getLocales().getText(locale, path);
	}

	private void kick(ServerPlayer player, Component message) {
		Sponge.server().scheduler().executor(plugin.getPluginContainer()).execute(() -> {
			player.kick(message);
		});
	}

}
