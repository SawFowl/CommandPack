package sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig;

public interface Economy {

	interface Currency {

		String getKey();

		String getDBCollumn();

		String getID();

		String getDefault();

		String getTransferPermission();

	}

	Currency getCurrency();

	String getStorageType();

	String getUpdateInterval();

	String getDBSettings();

	String getAadditionalChecks();

}
