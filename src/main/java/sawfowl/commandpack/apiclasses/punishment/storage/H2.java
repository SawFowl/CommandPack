package sawfowl.commandpack.apiclasses.punishment.storage;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.Ban.IP;
import org.spongepowered.api.service.ban.Ban.Profile;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.api.data.punishment.Warns;

public class H2 extends AbstractPunishmentStorage {

	private File file;
	public H2(CommandPack plugin) {
		super(plugin);
		file = plugin.getConfigDir().resolve("Punishments.h2").toFile();
		checkFile();
	}

	private void checkFile() {
		if(file.exists()) return;
		try {
			 file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveBan(Profile ban) {
	}

	@Override
	public boolean deleteBan(GameProfile profile) {
		return false;
	}

	@Override
	public void saveBan(IP ban) {
	}

	@Override
	public boolean deleteIPBan(InetAddress address) {
		return false;
	}

	@Override
	public void saveMute(Mute mute) {
	}

	@Override
	public void deleteMute(Mute mute) {
	}

	@Override
	public void saveWarns(Warns warns) {
	}

	@Override
	public void deleteWarns(Warns warns) {
	}

	@Override
	public void load() {
	}

}
