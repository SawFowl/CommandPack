package sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig;

public interface Afk {

	interface Titles {

		String getBeforeKick();

		String getUnlimit();

	}

	Titles getTitles();

	String getTurnOnDelay();

	String getKickDelay();

}
