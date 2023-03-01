package sawfowl.commandpack;

public class Permissions {

	// User
	public static final String SUICIDE = "commandpack.commands.user.suicide";

	// Staff
	public static final String IGNORE_DELAY_TIMER = "commandpack.commands.staff.ignore.delay.timer";
	public static final String IGNORE_DELAY_MOVING = "commandpack.commands.staff.ignore.delay.moving";
	public static final String IGNORE_DELAY_OTHER_COMMAND = "commandpack.commands.staff.ignore.delay.othercommand";
	public static final String IGNORE_PRICE = "commandpack.commands.staff.ignore.price";

	public static String getIgnoreDelayTimer(String command) {
		return IGNORE_DELAY_TIMER + "." + command;
	}

	public static String getIgnoreDelayMoving(String command) {
		return IGNORE_DELAY_MOVING + "." + command;
	}

	public static String getIgnorePrice(String command) {
		return IGNORE_PRICE + "." + command;
	}

}
