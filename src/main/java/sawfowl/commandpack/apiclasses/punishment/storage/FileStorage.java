package sawfowl.commandpack.apiclasses.punishment.storage;

import java.nio.file.Path;

import sawfowl.commandpack.CommandPack;

public class FileStorage extends AbstractPunishmentStorage {

	private Path bansPath;
	private Path mutesPath;
	private Path warnsPath;
	public FileStorage(CommandPack plugin) {
		super(plugin);
		bansPath = plugin.getConfigDir().resolve("Modules").resolve("Punishment").resolve("Bans");
		mutesPath = plugin.getConfigDir().resolve("Modules").resolve("Punishment").resolve("Mutes");
		warnsPath = plugin.getConfigDir().resolve("Modules").resolve("Punishment").resolve("Warns");
		checkPaths();
	}

	private void checkPaths() {
		if(!bansPath.toFile().exists()) bansPath.toFile().mkdir();
		if(!mutesPath.toFile().exists()) mutesPath.toFile().mkdir();
		if(!warnsPath.toFile().exists()) warnsPath.toFile().mkdir();
	}

}
