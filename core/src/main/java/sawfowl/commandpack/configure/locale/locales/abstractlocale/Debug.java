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

	Economy getEconomy();

	Commands getCommands();

	String getModsList(String player, String mods);

}
