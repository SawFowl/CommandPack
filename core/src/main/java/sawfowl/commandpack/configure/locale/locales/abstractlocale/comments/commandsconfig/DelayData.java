package sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.commandsconfig;

public interface DelayData {

	interface CancelRules {

		String getAllowMoving();

		String getAllowOtherCommand();

	}

	CancelRules getCancelRules();

	String getSeconds();

}
