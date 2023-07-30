package sawfowl.commandpack.apiclasses.punishment.storage;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;

import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.Ban;
import org.spongepowered.api.service.ban.Ban.IP;
import org.spongepowered.api.service.ban.Ban.Profile;
import org.spongepowered.api.service.ban.BanTypes;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.api.data.punishment.Warns;
import sawfowl.commandpack.configure.configs.punishment.WarnsData;
import sawfowl.localeapi.api.TextUtils;

public class MySqlStorage extends SqlStorage {

	public MySqlStorage(CommandPack plugin) {
		super(plugin);
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
		Long creation = results.getLong(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getCreationDate());
		Long expiration = results.getLong(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getExpirationDate());
		String reason = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getReason());
		Profile.Builder builder = Ban.builder().type(BanTypes.PROFILE).profile(GameProfile.of(uuid, name));
		if(source != null) builder = builder.source(TextUtils.deserialize(source));
		if(creation != null) builder = builder.startDate(Instant.ofEpochMilli(creation));
		if(expiration != null) builder = builder.expirationDate(Instant.ofEpochMilli(expiration));
		if(reason != null) builder = builder.reason(TextUtils.deserialize(reason));
		bans.put(uuid, (Profile) builder.build());
	}

	@Override
	public void loadBanIP(ResultSet results) throws SQLException {
		InetAddress ip = null;
		try {
			ip = InetAddress.getByName(results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getIp()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		String source = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getSource());
		Long creation = results.getLong(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getCreationDate());
		Long expiration = results.getLong(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getExpirationDate());
		String reason = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getReason());
		IP.Builder builder = Ban.builder().type(BanTypes.IP).address(ip);
		if(source != null) builder = builder.source(TextUtils.deserialize(source));
		if(creation != null) builder = builder.startDate(Instant.ofEpochMilli(creation));
		if(expiration != null) builder = builder.expirationDate(Instant.ofEpochMilli(expiration));
		if(reason != null) builder = builder.reason(TextUtils.deserialize(reason));
		bansIP.put(ip, (IP) builder.build());
	}

	@Override
	public void loadMute(ResultSet results) throws SQLException {
		UUID uuid = UUID.fromString(results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getUniqueId()));
		String name = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getName());
		String source = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getSource());
		Long creation = results.getLong(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getCreationDate());
		Long expiration = results.getLong(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getExpirationDate());
		String reason = results.getString(plugin.getMainConfig().getPunishment().getMySqlQueries().getColumns().getReason());
		Mute.Builder builder = Mute.builder().target(uuid, name);
		if(source != null) builder = builder.source(TextUtils.deserialize(source));
		if(creation != null) builder = builder.creationDate(Instant.ofEpochMilli(creation));
		if(expiration != null) builder = builder.expirationDate(Instant.ofEpochMilli(expiration));
		if(reason != null) builder = builder.reason(TextUtils.deserialize(reason));
		bans.put(uuid, (Profile) builder.build());
	}

	@Override
	public void loadWarns(ResultSet results) throws SQLException {
		UUID uuid = UUID.fromString(results.getString("uuid"));
		String warnsData = results.getString("warns_data");
		Warns warns = warnsFromString(warnsData);
		if(warns != null) super.warns.put(uuid, warns);}

	@Override
	public Object[] insertProfileBanObjects(Profile ban) throws ConfigurateException {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().getPatterns().getBan()
				.replace("%uuid%", ban.profile().uniqueId().toString())
				.replace("%name%", ban.profile().name().orElse(ban.profile().examinableName()))
				.replace("%source%", ban.banSource().map(TextUtils::serializeLegacy).orElse("n/a"))
				.replace("%creation%", String.valueOf(ban.creationDate().toEpochMilli()))
				.replace("%expiration%", ban.expirationDate().map(Instant::toEpochMilli).map(Object::toString).orElse("empty"))
				.replace("%reason%", ban.reason().map(TextUtils::serializeLegacy).orElse("n/a")).split("><");
	}

	@Override
	public Object[] insertIPBanObjects(IP ban) throws ConfigurateException {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().getPatterns().getBan()
				.replace("%ip%", ban.address().getHostAddress())
				.replace("%source%", ban.banSource().map(TextUtils::serializeLegacy).orElse("n/a"))
				.replace("%creation%", String.valueOf(ban.creationDate().toEpochMilli()))
				.replace("%expiration%", ban.expirationDate().map(Instant::toEpochMilli).map(Object::toString).orElse("empty"))
				.replace("%reason%", ban.reason().map(TextUtils::serializeLegacy).orElse("n/a")).split("><");
	}

	@Override
	public Object[] insertMuteObjects(Mute mute) throws ConfigurateException {
		return plugin.getMainConfig().getPunishment().getMySqlQueries().getPatterns().getBan()
				.replace("%uuid%", mute.getUniqueId().toString())
				.replace("%name%", mute.getName())
				.replace("%source%", mute.getSource().map(TextUtils::serializeLegacy).orElse("n/a"))
				.replace("%creation%", String.valueOf(mute.getCreationDate().toEpochMilli()))
				.replace("%expiration%", mute.getExpirationDate().map(Instant::toEpochMilli).map(Object::toString).orElse("empty"))
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

}
