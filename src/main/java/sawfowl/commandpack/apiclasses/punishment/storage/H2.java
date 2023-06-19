package sawfowl.commandpack.apiclasses.punishment.storage;

import java.io.File;
import java.io.IOException;

import sawfowl.commandpack.CommandPack;

public class H2 extends AbstractPunishmentStorage {

	private File file;
	public H2(CommandPack plugin) {
		super(plugin);
		file = plugin.getConfigDir().resolve("Punishments.h2").toFile();
	}

	void checkFile() {
			try {
				if(!file.exists()) file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}
