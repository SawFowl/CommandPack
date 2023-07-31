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
	public MySqlStorage(CommandPack plugin) {
		super(plugin);
		selectBans = "SELECT * FROM " + plugin.getMainConfig().getPunishment().getMySqlQueries().getTables().get("bans");
		selectBansIP = "SELECT * FROM " + plugin.getMainConfig().getPunishment().getMySqlQueries().getTables().get("bans_ip");
		selectMutes = "SELECT * FROM " + plugin.getMainConfig().getPunishment().getMySqlQueries().getTables().get("mutes");
		selectWarns = "SELECT * FROM " + plugin.getMainConfig().getPunishment().getMySqlQueries().getTables().get("warns");
		try {
			selectConnection = plugin.getMariaDB().get().createNewConnection();
			deleteConnection = plugin.getMariaDB().get().createNewConnection();
		} catch (SQLException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
		sync();
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
		return plugin.getMainConfig().getPunishment().getMySqlQueries().createProfileBansTableSql();
	}

	@Override
	public String createIPBansTableSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().createIPBansTableSql();
	}

	@Override
	public String createMutesTableSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().createMutesTableSql();
	}

	@Override
	public String createWarnsTableSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().createWarnsTableSql();
	}

	@Override
	public String selectAllProfileBansSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().selectAllProfileBansSql();
	}

	@Override
	public String selectAllIPBansSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().selectAllIPBansSql();
	}

	@Override
	public String selectAllMutesSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().selectAllMutesSql();
	}

	@Override
	public String selectAllWarnsSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().selectAllWarnsSql();
	}

	@Override
	public String insertProfileBanSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().insertProfileBanSql();
	}

	@Override
	public String insertIPBanSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().insertIPBanSql();
	}

	@Override
	public String insertMuteSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().insertMuteSql();
	}

	@Override
	public String insertWarnsSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().insertWarnsSql();
	}

	@Override
	public String deleteProfileBanSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().deleteProfileBanSql();
	}

	@Override
	public String deleteIPBanSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().deleteIPBanSql();
	}

	@Override
	public String deleteMuteSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().deleteMuteSql();
	}

	@Override
	public String deleteWarnsSql() {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().deleteWarnsSql();
	}

	@Override
	public void loadBanProfile(ResultSet results) throws SQLException {
		UUID uuid = UUID.fromString(results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getUniqueId()));
		String name = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getName());
		String source = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getSource());
		Long creation = results.getLong(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getCreated());
		Long expiration = results.getLong(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getExpiration());
		String reason = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getReason());
		Profile.Builder builder = Ban.builder().type(BanTypes.PROFILE).profile(GameProfile.of(uuid, name));
		if(source != null) builder = builder.source(text(source));
		if(creation != null) builder = builder.startDate(Instant.ofEpochMilli(creation));
		if(expiration != null && expiration > 0) builder = builder.expirationDate(Instant.ofEpochMilli(expiration));
		if(reason != null) builder = builder.reason(text(reason));
		if(bans.containsKey(uuid)) bans.remove(uuid);
		Profile ban = (Profile) builder.build();
		bans.put(uuid, ban);
		Optional<ServerPlayer> optPlayer = Sponge.server().player(uuid);
		if(!optPlayer.isPresent()) return;
		ServerPlayer player = optPlayer.get();
		kick(player, TextUtils.replaceToComponents(getText(player.locale(), ban.expirationDate().isPresent() ? LocalesPaths.COMMANDS_BAN_DISCONNECT : LocalesPaths.COMMANDS_BAN_DISCONNECT_PERMANENT), new String[] {Placeholders.TIME, Placeholders.SOURCE, Placeholders.VALUE}, new Component[] {(ban.expirationDate().isPresent() ? expire(player.locale(), ban) : text("")), ban.banSource().orElse(TextUtils.deserializeLegacy("&cServer")), ban.reason().orElse(TextUtils.deserializeLegacy("-"))}));
	}

	@Override
	public void loadBanIP(ResultSet results) throws SQLException {
		try {
			InetAddress ip = InetAddress.getByName(results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getIp()));
			String source = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getSource());
			Long creation = results.getLong(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getCreated());
			Long expiration = results.getLong(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getExpiration());
			String reason = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getReason());
			IP.Builder builder = Ban.builder().type(BanTypes.IP).address(ip);
			if(source != null) builder = builder.source(text(source));
			if(creation != null) builder = builder.startDate(Instant.ofEpochMilli(creation));
			if(expiration != null && expiration > 0) builder = builder.expirationDate(Instant.ofEpochMilli(expiration));
			if(reason != null) builder = builder.reason(text(reason));
			if(bansIP.containsKey(ip)) bansIP.remove(ip);
			IP ban = (IP) builder.build();
			bansIP.put(ip, (IP) builder.build());
			Optional<ServerPlayer> optPlayer = Sponge.server().onlinePlayers().stream().filter(player -> player.connection().address().getAddress().getHostAddress().equals(ip.getHostAddress())).findFirst();
			if(!optPlayer.isPresent()) return;
			ServerPlayer player = optPlayer.get();
			kick(player, TextUtils.replaceToComponents(getText(player.locale(), ban.expirationDate().isPresent() ? LocalesPaths.COMMANDS_BAN_DISCONNECT : LocalesPaths.COMMANDS_BAN_DISCONNECT_PERMANENT), new String[] {Placeholders.TIME, Placeholders.SOURCE, Placeholders.VALUE}, new Component[] {(ban.expirationDate().isPresent() ? expire(player.locale(), ban) : text("")), ban.banSource().orElse(TextUtils.deserializeLegacy("&cServer")), ban.reason().orElse(TextUtils.deserializeLegacy("-"))}));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadMute(ResultSet results) throws SQLException {
		UUID uuid = UUID.fromString(results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getUniqueId()));
		String name = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getName());
		String source = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getSource());
		Long creation = results.getLong(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getCreated());
		Long expiration = results.getLong(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getExpiration());
		String reason = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getReason());
		Mute.Builder builder = Mute.builder().target(uuid, name);
		if(source != null) builder = builder.source(TextUtils.deserialize(source));
		if(creation != null) builder = builder.creationDate(Instant.ofEpochMilli(creation));
		if(expiration != null && expiration > 0) builder = builder.expirationDate(Instant.ofEpochMilli(expiration));
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
		return plugin.getMainConfig().getPunishment().getMySqlQueries().getPatterns().getBan()
				.replace("%uuid%", ban.profile().uniqueId().toString())
				.replace("%name%", ban.profile().name().orElse(ban.profile().examinableName()))
				.replace("%source%", ban.banSource().map(TextUtils::serializeLegacy).orElse("n/a"))
				.replace("%creation%", String.valueOf(ban.creationDate().toEpochMilli()))
				.replace("%expiration%", ban.expirationDate().map(Instant::toEpochMilli).map(Object::toString).orElse("0"))
				.replace("%reason%", ban.reason().map(TextUtils::serializeLegacy).orElse("n/a")).split("><");
	}

	@Override
	public Object[] insertIPBanObjects(IP ban) throws ConfigurateException {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().getPatterns().getBan()
				.replace("%ip%", ban.address().getHostAddress())
				.replace("%source%", ban.banSource().map(TextUtils::serializeLegacy).orElse("n/a"))
				.replace("%creation%", String.valueOf(ban.creationDate().toEpochMilli()))
				.replace("%expiration%", ban.expirationDate().map(Instant::toEpochMilli).map(Object::toString).orElse("0"))
				.replace("%reason%", ban.reason().map(TextUtils::serializeLegacy).orElse("n/a")).split("><");
	}

	@Override
	public Object[] insertMuteObjects(Mute mute) throws ConfigurateException {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().getPatterns().getBan()
				.replace("%uuid%", mute.getUniqueId().toString())
				.replace("%name%", mute.getName())
				.replace("%source%", mute.getSource().map(TextUtils::serializeLegacy).orElse("n/a"))
				.replace("%creation%", String.valueOf(mute.getCreationDate().toEpochMilli()))
				.replace("%expiration%", mute.getExpirationDate().map(Instant::toEpochMilli).map(Object::toString).orElse("0"))
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

	private void sync() {
		for(String string : plugin.getMainConfig().getPunishment().getMySqlQueries().getTables().values()) {
			executeSQL("ALTER TABLE " + string + " ADD COLUMN IF NOT EXISTS written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;");
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
				syncBansIP();
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
