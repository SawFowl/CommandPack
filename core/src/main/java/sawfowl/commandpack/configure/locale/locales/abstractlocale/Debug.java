package sawfowl.commandpack.configure.locale.locales.abstractlocale;

public interface Debug {

	interface Economy {

		String getErrorGiveMoney(String player);

		String getErrorTakeMoney(String player);

		String getNotFound();
		
	}

	interface Commands {

		Executors getExecutors();

		String getLog(String source, String command, String args);

		String getNotTracking(String command);

		interface Executors {

			String getServer();

			String getCommandBlock(String cords);

			String getCommandBlockMinecart(String cords);

			String getUnknown();

		}

	}

	interface DebugPlayerData {

		String getMods(String player, String mods);

		String getPackets(String player, String packet, String data);

	}

	Economy getEconomy();

	Commands getCommands();

	DebugPlayerData getDebugPlayerData();

}
