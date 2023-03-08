package sawfowl.commandpack.configure.locale;

public class LocalesPaths {

	private static final String PATH_ECONOMY = "Economy";

	private static final String PATH_COMMANDS = "CommandsConfig";

	//private static final String PATH_EVENTS = "Events";

	//private static final String PATH_HOVER = "HoverText";

	private static final Object PATH_EXCEPTIONS = "Exceptions";
	private static final Object PATH_INCOMPLETE = "Incomplete";
	private static final Object PATH_TIME = "Time";
	private static final Object PATH_TRACKING = "Tracking";
	private static final Object NAMES = "Names";
	private static final Object OTHER = "Other";
	private static final Object COMMAND_HAT = "Hat";
	private static final Object COMMAND_SPAWN = "Spawn";
	private static final Object COMMAND_SETHOME = "SetHome";
	private static final Object COMMAND_HOME = "Home";
	private static final Object COMMAND_WARP = "Warp";
	private static final Object COMMAND_SETWARP = "SetWarp";

	public static final Object[] TIME_HOUR = {PATH_TIME, "Hour"};
	public static final Object[] TIME_MINUTE = {PATH_TIME, "Minute"};
	public static final Object[] TIME_SECOND = {PATH_TIME, "Second"};

	public static final Object[] REMOVE = {OTHER, "Remove"};
	public static final Object[] TELEPORT = {OTHER, "Remove"};
	public static final Object[] TELEPORTCLICKABLE = {OTHER, "RemoveClickable"};

	public static final Object[] ECONOMY_NOT_FOUND = {PATH_ECONOMY, PATH_EXCEPTIONS, "EconomyNotFound"};
	public static final Object[] ECONOMY_ERROR_GIVE_MONEY = {PATH_ECONOMY, PATH_EXCEPTIONS, "ErrorGiveMoney"};
	public static final Object[] ECONOMY_ERROR_TAKE_MONEY = {PATH_ECONOMY, PATH_EXCEPTIONS, "ErrorTakeMoney"};

	public static final Object[] NAME_SYSTEM = {NAMES, "SystemSubject"};
	public static final Object[] NAME_COMMANDBLOCK = {NAMES, "CommandBlock"};
	public static final Object[] NAME_COMMANDBLOCK_MINECART = {NAMES, "CommandBlockMineCart"};
	public static final Object[] NAME_UNKNOWN = {NAMES, "UnknownSource"};

	public static final Object[] COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT = {PATH_COMMANDS, PATH_EXCEPTIONS, PATH_INCOMPLETE, "PlayerNotPresent"};
	public static final Object[] COMMANDS_EXCEPTION_BOOLEAN_NOT_PRESENT = {PATH_COMMANDS, PATH_EXCEPTIONS, PATH_INCOMPLETE, "BooleanNotPresent"};
	public static final Object[] COMMANDS_EXCEPTION_NAME_NOT_PRESENT = {PATH_COMMANDS, PATH_EXCEPTIONS, PATH_INCOMPLETE, "NameNotPresent"};
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
	public static final Object[] COMMANDS_SETSPAWN_SUCCESS = {PATH_COMMANDS, "SetSpawn", "Success"};
	public static final Object[] COMMANDS_SPAWN_SUCCESS = {PATH_COMMANDS, COMMAND_SPAWN, "Success"};
	public static final Object[] COMMANDS_SPAWN_SUCCESS_OTHER = {PATH_COMMANDS, COMMAND_SPAWN, "SuccessOther"};
	public static final Object[] COMMANDS_SETHOME_LIMIT = {PATH_COMMANDS, COMMAND_SETHOME, PATH_EXCEPTIONS, "Limit"};
	public static final Object[] COMMANDS_SETHOME_SUCCESS= {PATH_COMMANDS, COMMAND_SETHOME, "Success "};
	public static final Object[] COMMANDS_HOME_NOT_FOUND = {PATH_COMMANDS, COMMAND_HOME, PATH_EXCEPTIONS, "NotFound"};
	public static final Object[] COMMANDS_HOME_NOT_SET = {PATH_COMMANDS, COMMAND_HOME, PATH_EXCEPTIONS, "NotSet"};
	public static final Object[] COMMANDS_HOME_TELEPORT_ERROR = {PATH_COMMANDS, COMMAND_HOME, PATH_EXCEPTIONS, "TeleportError"};
	public static final Object[] COMMANDS_HOME_SUCCESS = {PATH_COMMANDS, COMMAND_HOME, "Success "};
	public static final Object[] COMMANDS_HOME_LIST = {PATH_COMMANDS, COMMAND_HOME, "ListTitle "};
	public static final Object[] COMMANDS_WARP_NOT_FOUND = {PATH_COMMANDS, COMMAND_WARP, PATH_EXCEPTIONS, "NotFound"};
	public static final Object[] COMMANDS_SETWARP_EXIST = {PATH_COMMANDS, COMMAND_SETWARP, PATH_EXCEPTIONS, "AllreadyExist"};

}
