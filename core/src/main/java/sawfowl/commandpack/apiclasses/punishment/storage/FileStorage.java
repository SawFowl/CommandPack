package sawfowl.commandpack.apiclasses.punishment.storage;

import java.io.File;
import java.net.InetAddress;
import java.nio.file.Path;
import java.util.UUID;

import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.Ban;
import org.spongepowered.api.service.ban.Ban.IP;
import org.spongepowered.api.service.ban.Ban.Profile;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.reference.ConfigurationReference;
import org.spongepowered.configurate.reference.ValueReference;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.api.data.punishment.Warns;
import sawfowl.commandpack.configure.configs.punishment.BanData;
import sawfowl.commandpack.configure.configs.punishment.MuteData;
import sawfowl.commandpack.configure.configs.punishment.WarnsData;
import sawfowl.commandpack.utils.StorageType;

public class FileStorage extends AbstractPunishmentStorage {

	private Path bansPath;
	private Path bansIPPath;
	private Path mutesPath;
	private Path warnsPath;
	public FileStorage(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public StorageType getStorageType() {
		return StorageType.FILE;
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
	public void saveBan(Ban.Profile ban) {
		if(bans.containsKey(ban.profile().uniqueId())) return;
		bans.put(ban.profile().uniqueId(), ban);
		if(!bansPath.toFile().exists()) bansPath.toFile().mkdir();
		try {
			ConfigurationReference<CommentedConfigurationNode> configReference = HoconConfigurationLoader.builder().defaultOptions(options).path(bansPath.resolve(ban.profile().uniqueId().toString() + ".conf")).build().loadToReference();
			ValueReference<BanData, CommentedConfigurationNode> config = configReference.referenceTo(BanData.class);
			config.setAndSave(new BanData(ban));
		} catch (ConfigurateException e) {
			e.printStackTrace();
		}
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
		try {
			ConfigurationReference<CommentedConfigurationNode> configReference = HoconConfigurationLoader.builder().defaultOptions(options).path(bansIPPath.resolve(ban.address().getHostAddress().toString() + ".conf")).build().loadToReference();
			ValueReference<BanData, CommentedConfigurationNode> config = configReference.referenceTo(BanData.class);
			config.setAndSave(new BanData(ban));
		} catch (ConfigurateException e) {
			e.printStackTrace();
		}
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
		try {
			ConfigurationReference<CommentedConfigurationNode> configReference = HoconConfigurationLoader.builder().defaultOptions(options).path(mutesPath.resolve(mute.getUniqueId().toString() + ".conf")).build().loadToReference();
			ValueReference<MuteData, CommentedConfigurationNode> config = configReference.referenceTo(MuteData.class);
			config.setAndSave((MuteData) (mute instanceof MuteData ? mute : Mute.builder().from(mute)));
		} catch (ConfigurateException e) {
			e.printStackTrace();
		}
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
		try {
			ConfigurationReference<CommentedConfigurationNode> configReference = HoconConfigurationLoader.builder().defaultOptions(options).path(warnsPath.resolve(warns.getUniqueId().toString() + ".conf")).build().loadToReference();
			ValueReference<WarnsData, CommentedConfigurationNode> config = configReference.referenceTo(WarnsData.class);
			config.setAndSave((WarnsData) (warns instanceof WarnsData ? warns : Warns.builder().from(warns)));
		} catch (ConfigurateException e) {
			e.printStackTrace();
		}
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
		if(bansPath.toFile().exists() && bansPath.toFile().listFiles().length > 0) for(File file : bansPath.toFile().listFiles()) if(file.getName().endsWith(".conf")) loadBanData(file);
		if(bansIPPath.toFile().exists() && bansIPPath.toFile().listFiles().length > 0) for(File file : bansIPPath.toFile().listFiles()) if(file.getName().endsWith(".conf")) loadBanIPData(file);
		if(mutesPath.toFile().exists() && mutesPath.toFile().listFiles().length > 0) for(File file : mutesPath.toFile().listFiles()) if(file.getName().endsWith(".conf")) loadMuteData(file);
		if(warnsPath.toFile().exists() && warnsPath.toFile().listFiles().length > 0) for(File file : warnsPath.toFile().listFiles()) if(file.getName().endsWith(".conf")) loadWarnsData(file);
	}

	private void loadBanData(File file) {
		try {
			ConfigurationReference<CommentedConfigurationNode> configReference = HoconConfigurationLoader.builder().defaultOptions(options).path(file.toPath()).build().loadToReference();
			ValueReference<BanData, CommentedConfigurationNode> config = configReference.referenceTo(BanData.class);
			BanData ban = config.get();
			if(ban.getUniqueId().isPresent()) bans.put(ban.getUniqueId().get(), (Profile) ban.getBan());
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

	private void loadBanIPData(File file) {
		try {
			ConfigurationReference<CommentedConfigurationNode> configReference = HoconConfigurationLoader.builder().defaultOptions(options).path(file.toPath()).build().loadToReference();
			ValueReference<BanData, CommentedConfigurationNode> config = configReference.referenceTo(BanData.class);
			BanData ban = config.get();
			if(ban.getInetAddress().isPresent()) bansIP.put(ban.getInetAddress().get(), (IP) ban.getBan());
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

	private void loadMuteData(File file) {
		try {
			ConfigurationReference<CommentedConfigurationNode> configReference = HoconConfigurationLoader.builder().defaultOptions(options).path(file.toPath()).build().loadToReference();
			ValueReference<MuteData, CommentedConfigurationNode> config = configReference.referenceTo(MuteData.class);
			MuteData mute = config.get();
			mutes.put(mute.getUniqueId(), mute);
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

	private void loadWarnsData(File file) {
		try {
			ConfigurationReference<CommentedConfigurationNode> configReference = HoconConfigurationLoader.builder().defaultOptions(options).path(file.toPath()).build().loadToReference();
			ValueReference<WarnsData, CommentedConfigurationNode> config = configReference.referenceTo(WarnsData.class);
			WarnsData warns = config.get();
			super.warns.put(warns.getUniqueId(), warns);
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

}
