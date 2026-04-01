package sawfowl.commandpack.apiclasses.punishment.storage;

import java.io.File;
import java.net.InetAddress;
import java.nio.file.Path;
import java.util.UUID;

import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.Ban;
import org.spongepowered.api.service.ban.Ban.IP;
import org.spongepowered.api.service.ban.Ban.Profile;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.api.data.punishment.Warns;
import sawfowl.commandpack.configure.configs.punishment.BanData;
import sawfowl.commandpack.configure.configs.punishment.MuteData;
import sawfowl.commandpack.configure.configs.punishment.WarnsData;
import sawfowl.localeapi.api.ConfigTypes;
import sawfowl.localeapi.api.services.ConfigurationService;

public class FileStorage extends AbstractPunishmentStorage {

	private Path bansPath;
	private Path bansIPPath;
	private Path mutesPath;
	private Path warnsPath;
	public FileStorage(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public boolean isSupportCombined() {
		return false;
	}

	private void checkPaths() {
		if(!plugin.getConfigDir().resolve("Modules").toFile().exists()) plugin.getConfigDir().resolve("Modules").toFile().mkdir();
		if(!plugin.getConfigDir().resolve("Modules" + File.separator + "Punishment").toFile().exists()) plugin.getConfigDir().resolve("Modules" + File.separator + "Punishment").toFile().mkdir();
		bansPath = plugin.getConfigDir().resolve("Modules" + File.separator + "Punishment" + File.separator + "Bans");
		bansIPPath = plugin.getConfigDir().resolve("Modules" + File.separator + "Punishment" + File.separator + "BansIP");
		mutesPath = plugin.getConfigDir().resolve("Modules" + File.separator + "Punishment" + File.separator + "Mutes");
		warnsPath = plugin.getConfigDir().resolve("Modules" + File.separator + "Punishment" + File.separator + "Warns");
		if(!bansPath.toFile().exists()) bansPath.toFile().mkdir();
		if(!bansIPPath.toFile().exists()) bansIPPath.toFile().mkdir();
		if(!mutesPath.toFile().exists()) mutesPath.toFile().mkdir();
		if(!warnsPath.toFile().exists()) warnsPath.toFile().mkdir();
	}

	@Override
	public void saveBans(Profile profile, IP ip) {
		saveBan(profile);
		saveBan(ip);
	}

	@Override
	public void saveBan(Ban.Profile ban) {
		if(bans.containsKey(ban.profile().uniqueId())) return;
		bans.put(ban.profile().uniqueId(), ban);
		if(!bansPath.toFile().exists()) bansPath.toFile().mkdir();
		ConfigurationService.getInstance().createReferencedConfig(new BanData(ban)).setPath(bansPath).setType(ConfigTypes.HOCON).setName(ban.profile().uniqueId().toString()).build();
	}

	@Override
	public boolean deleteBan(GameProfile profile) {
		File file = bansPath.resolve(profile.uniqueId().toString() + ".conf").toFile();
		if(!bans.containsKey(profile.uniqueId()) && !file.exists()) return false;
		if(bans.containsKey(profile.uniqueId())) bans.remove(profile.uniqueId());
		if(file.exists()) file.delete();
		return true;
	}

	@Override
	public void saveBan(IP ban) {
		if(bansIP.containsKey(ban.address())) return;
		if(!bansIPPath.toFile().exists()) bansIPPath.toFile().mkdir();
		ConfigurationService.getInstance().createReferencedConfig(new BanData(ban)).setPath(bansIPPath).setType(ConfigTypes.HOCON).setName(ban.address().getHostAddress().toString()).build();
	}

	@Override
	public boolean deleteIPBan(InetAddress address) {
		File file = bansIPPath.resolve(address.getHostAddress() + ".conf").toFile();
		if(!bansIP.containsKey(address) && !file.exists()) return false;
		if(bansIP.containsKey(address)) bansIP.remove(address);
		if(file.exists()) file.delete();
		return true;
	}

	@Override
	public void saveMute(Mute mute) {
		if(mutes.containsKey(mute.getUniqueId())) return;
		mutes.put(mute.getUniqueId(), mute);
		if(!mutesPath.toFile().exists()) mutesPath.toFile().mkdir();
		ConfigurationService.getInstance().createReferencedConfig((MuteData) (mute instanceof MuteData ? mute : Mute.builder().from(mute))).setPath(mutesPath).setType(ConfigTypes.HOCON).setName(mute.getUniqueId().toString()).build();
	}

	@Override
	public boolean deleteMute(Mute mute) {
		if(mutes.containsKey(mute.getUniqueId())) mutes.remove(mute.getUniqueId());
		File file = mutesPath.resolve(mute.getUniqueId().toString() + ".conf").toFile();
		return file.exists() && file.delete();
	}

	@Override
	public void saveWarns(Warns warns) {
		if(super.warns.containsKey(warns.getUniqueId())) super.warns.remove(warns.getUniqueId());
		super.warns.put(warns.getUniqueId(), warns);
		if(!warnsPath.toFile().exists()) warnsPath.toFile().mkdir();
		ConfigurationService.getInstance().createReferencedConfig((WarnsData) (warns instanceof WarnsData ? warns : Warns.builder().from(warns))).setPath(warnsPath).setType(ConfigTypes.HOCON).setName(warns.getUniqueId().toString()).build();
	}

	@Override
	public boolean deleteWarns(UUID player) {
		if(warns.containsKey(player)) warns.remove(player);
		File file = warnsPath.resolve(player.toString() + ".conf").toFile();
		return file.exists() && file.delete();
	}

	@Override
	public void load() {
		checkPaths();
		if(bansPath.toFile().exists() && bansPath.toFile().listFiles().length > 0) for(File file : bansPath.toFile().listFiles()) if(isValidFile(file)) loadBanData(file);
		if(bansIPPath.toFile().exists() && bansIPPath.toFile().listFiles().length > 0) for(File file : bansIPPath.toFile().listFiles()) if(isValidFile(file)) loadBanIPData(file);
		if(mutesPath.toFile().exists() && mutesPath.toFile().listFiles().length > 0) for(File file : mutesPath.toFile().listFiles()) if(isValidFile(file)) loadMuteData(file);
		if(warnsPath.toFile().exists() && warnsPath.toFile().listFiles().length > 0) for(File file : warnsPath.toFile().listFiles()) if(isValidFile(file)) loadWarnsData(file);
	}

	private void loadBanData(File file) {
		var ban = ConfigurationService.getInstance().createReferencedConfig(BanData.class).fromFile(file).build().get();
		if(ban.getUniqueId().isPresent()) bans.put(ban.getUniqueId().get(), (Profile) ban.getBan());
	}

	private void loadBanIPData(File file) {
		var ban = ConfigurationService.getInstance().createReferencedConfig(BanData.class).fromFile(file).build().get();
		if(ban.getInetAddress().isPresent()) bansIP.put(ban.getInetAddress().get(), (IP) ban.getBan());
	}

	private void loadMuteData(File file) {
		var mute = ConfigurationService.getInstance().createReferencedConfig(MuteData.class).fromFile(file).build().get();
		mutes.put(mute.getUniqueId(), mute);
	}

	private void loadWarnsData(File file) {
		var warns = ConfigurationService.getInstance().createReferencedConfig(WarnsData.class).fromFile(file).build().get();
		super.warns.put(warns.getUniqueId(), warns);
	}

	private boolean isValidFile(File file) {
		return ConfigTypes.isValidExtension(ConfigTypes.getExtension(file.getName()));
	}

}
