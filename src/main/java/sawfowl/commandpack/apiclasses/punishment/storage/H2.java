package sawfowl.commandpack.apiclasses.punishment.storage;

import java.io.File;
import java.io.IOException;

import sawfowl.commandpack.CommandPack;

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

}
