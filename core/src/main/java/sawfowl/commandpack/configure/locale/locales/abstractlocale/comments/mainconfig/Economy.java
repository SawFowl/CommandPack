package sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig;

public interface Economy {

	interface Currency {

		String getKey();

		String getDBCollumn();

		String getID();

		String getDefault();

		String getTransferPermission();

	}

	interface Tables {

		String getUniqueAccounts();

		String getAccounts();

	}

	Currency getCurrency();

	Tables getTables();

	String getStorageType();

	String getUpdateInterval();

	String getDBSettings();

	String getAdditionalChecks();

}
