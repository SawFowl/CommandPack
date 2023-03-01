package sawfowl.commandpack;

public class Permissions {

	// User
	public static final String SUICIDE = "commandpack.commands.user.suicide";

	// Staff
	public static final String DELAY_IGNORE_TIMER = "commandpack.commands.staff.delay.ignore.timer";
	public static final String DELAY_IGNORE_MOVING = "commandpack.commands.staff.delay.ignore.moving";
	public static final String DELAY_IGNORE_OTHER_COMMAND = "commandpack.commands.staff.delay.ignore.othercommand";

	public static String getIgnoreDelayTimer(String command) {
		return DELAY_IGNORE_TIMER + "." + command;
	}

	public static String getIgnoreDelayMoving(String command) {
		return DELAY_IGNORE_MOVING + "." + command;
	}

}
