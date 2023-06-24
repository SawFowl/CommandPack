package sawfowl.commandpack.apiclasses.punishment.storage;

import java.net.InetAddress;

import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.Ban.IP;
import org.spongepowered.api.service.ban.Ban.Profile;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.api.data.punishment.Warns;

public class MySql extends AbstractPunishmentStorage {

	public MySql(CommandPack plugin) {
		super(plugin);
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
	public boolean deleteMute(Mute mute) {
		return false;
	}

	@Override
	public void saveWarns(Warns warns) {
	}

	@Override
	public boolean deleteWarns(Warns warns) {
		return false;
	}

	@Override
	public void load() {
	}

}
