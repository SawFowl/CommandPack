package sawfowl.commandpack.configure.locale;

public class LocalesPaths {

	private static final String PATH_ECONOMY = "Economy";

	private static final String PATH_COMMANDS = "CommandsConfig";

	//private static final String PATH_EVENTS = "Events";

	//private static final String PATH_HOVER = "HoverText";

	private static final Object PATH_EXCEPTIONS = "Exceptions";
	private static final Object PATH_TIME = "Time";
	private static final Object PATH_TRACKING = "Time";
	private static final Object COMMAND_HAT = "Hat";

	public static final Object[] TIME_HOUR = {PATH_TIME, "Hour"};
	public static final Object[] TIME_MINUTE = {PATH_TIME, "Minute"};
	public static final Object[] TIME_SECOND = {PATH_TIME, "Second"};

	public static final Object[] ECONOMY_NOT_FOUND = {PATH_ECONOMY, PATH_EXCEPTIONS, "EconomyNotFound"};
	public static final Object[] ECONOMY_ERROR_GIVE_MONEY = {PATH_ECONOMY, PATH_EXCEPTIONS, "ErrorGiveMoney"};
	public static final Object[] ECONOMY_ERROR_TAKE_MONEY = {PATH_ECONOMY, PATH_EXCEPTIONS, "ErrorTakeMoney"};

	public static final Object[] COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT = {PATH_COMMANDS, PATH_EXCEPTIONS, "PlayerNotPresent"};
	public static final Object[] COMMANDS_NOT_TRACKING = {PATH_COMMANDS, PATH_EXCEPTIONS, PATH_TRACKING, "NotTracking"};
	public static final Object[] COMMANDS_STOP_TRACKING_COMMAND = {PATH_COMMANDS, PATH_EXCEPTIONS, PATH_TRACKING, "StopByCommand"};
	public static final Object[] COMMANDS_STOP_TRACKING_MOVING = {PATH_COMMANDS, PATH_EXCEPTIONS, PATH_TRACKING, "StopByMoving"};
	public static final Object[] COMMANDS_ERROR_TAKE_MONEY = {PATH_COMMANDS, PATH_EXCEPTIONS, "ErrorTakeMoney"};
	public static final Object[] COMMANDS_WAIT = {PATH_COMMANDS, "Wait"};
	public static final Object[] COMMANDS_COOLDOWN = {PATH_COMMANDS, "Cooldown"};
	public static final Object[] COMMANDS_LOG = {PATH_COMMANDS, "Log"};
	public static final Object[] COMMANDS_TAKE_MONEY = {PATH_COMMANDS, "TakeMoney"};
	public static final Object[] COMMANDS_HAT_NO_ITEM = {PATH_COMMANDS, COMMAND_HAT, PATH_EXCEPTIONS, "Empty"};
	public static final Object[] COMMANDS_HAT_BLACKLIST_ITEM = {PATH_COMMANDS, COMMAND_HAT, PATH_EXCEPTIONS, "Blacklist"};
	public static final Object[] COMMANDS_HAT_FULL_INVENTORY = {PATH_COMMANDS, COMMAND_HAT, PATH_EXCEPTIONS, "FullInventory"};
	public static final Object[] COMMANDS_HAT_SUCCESS_OTHER = {PATH_COMMANDS, COMMAND_HAT, PATH_EXCEPTIONS, "SuccessOther"};

}
