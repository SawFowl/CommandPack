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
	private static final Object COMMAND_WARPS = "Warps";
	private static final Object COMMAND_TPTOGGLE = "TpToggle";
	private static final Object COMMAND_TPA = "Tpa";
	private static final Object COMMAND_CLEAR = "Clear";
	private static final Object COMMAND_REPAIR = "Repair";

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
	public static final Object[] COMMANDS_EXCEPTION_TARGET_SELF = {PATH_COMMANDS, PATH_EXCEPTIONS, PATH_INCOMPLETE, "TargetSelf"};
	public static final Object[] COMMANDS_EXCEPTION_BOOLEAN_NOT_PRESENT = {PATH_COMMANDS, PATH_EXCEPTIONS, PATH_INCOMPLETE, "BooleanNotPresent"};
	public static final Object[] COMMANDS_EXCEPTION_LOCATION_NOT_PRESENT = {PATH_COMMANDS, PATH_EXCEPTIONS, PATH_INCOMPLETE, "LocationNotPresent"};
	public static final Object[] COMMANDS_EXCEPTION_NAME_NOT_PRESENT = {PATH_COMMANDS, PATH_EXCEPTIONS, PATH_INCOMPLETE, "NameNotPresent"};
	public static final Object[] COMMANDS_EXCEPTION_TYPE_NOT_PRESENT = {PATH_COMMANDS, PATH_EXCEPTIONS, PATH_INCOMPLETE, "TypeNotPresent"};
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
	public static final Object[] COMMANDS_WARP_SUCCESS = {PATH_COMMANDS, COMMAND_WARP, "Success"};
	public static final Object[] COMMANDS_WARP_SUCCESS_STAFF = {PATH_COMMANDS, COMMAND_WARP, "SuccessStaff"};
	public static final Object[] COMMANDS_WARP_SUCCESS_OTHER = {PATH_COMMANDS, COMMAND_WARP, "SuccessOther"};
	public static final Object[] COMMANDS_SETWARP_EXIST = {PATH_COMMANDS, COMMAND_SETWARP, PATH_EXCEPTIONS, "AllreadyExist"};
	public static final Object[] COMMANDS_SETWARP_LIMIT = {PATH_COMMANDS, COMMAND_SETWARP, PATH_EXCEPTIONS, "Limit"};
	public static final Object[] COMMANDS_SETWARP_SUCCESS = {PATH_COMMANDS, COMMAND_SETWARP, "Success"};
	public static final Object[] COMMANDS_SETWARP_SUCCESS_ADMIN = {PATH_COMMANDS, COMMAND_SETWARP, "SuccessAdmin"};
	public static final Object[] COMMANDS_WARPS_EMPTY = {PATH_COMMANDS, COMMAND_WARPS, PATH_EXCEPTIONS, "Empty"};
	public static final Object[] COMMANDS_WARPS_WAIT = {PATH_COMMANDS, COMMAND_WARPS, "Wait"};
	public static final Object[] COMMANDS_WARPS_LIST = {PATH_COMMANDS, COMMAND_WARPS, "List"};
	public static final Object[] COMMANDS_WARPS_HEADER = {PATH_COMMANDS, COMMAND_WARPS, "Header"};
	public static final Object[] COMMANDS_WARPS_SERVER_GROUP = {PATH_COMMANDS, COMMAND_WARPS, "ServerGroup"};
	public static final Object[] COMMANDS_WARPS_PLAYERS_GROUP = {PATH_COMMANDS, COMMAND_WARPS, "PlayersGroup"};
	public static final Object[] COMMANDS_TPTOGGLE_ENABLE = {PATH_COMMANDS, COMMAND_TPTOGGLE, "Enable"};
	public static final Object[] COMMANDS_TPTOGGLE_DISABLE = {PATH_COMMANDS, COMMAND_TPTOGGLE, "Disable"};
	public static final Object[] COMMANDS_TPA_DISABLE_TP_REQUESTS = {PATH_COMMANDS, COMMAND_TPA, PATH_EXCEPTIONS, "DisableTpRequests"};
	public static final Object[] COMMANDS_TPA_SOURCE_OFFLINE = {PATH_COMMANDS, COMMAND_TPA, PATH_EXCEPTIONS, "SourceOffline"};
	public static final Object[] COMMANDS_TPA_SUCCESS = {PATH_COMMANDS, COMMAND_TPA, "Success"};
	public static final Object[] COMMANDS_TPA_ACCEPTED = {PATH_COMMANDS, COMMAND_TPA, "Accepted"};
	public static final Object[] COMMANDS_TPA_REQUEST_MESSAGE = {PATH_COMMANDS, COMMAND_TPA, "RequestMessage"};
	public static final Object[] COMMANDS_TPA_REQUEST_HERE_MESSAGE = {PATH_COMMANDS, COMMAND_TPA, "RequestHereMessage"};
	public static final Object[] COMMANDS_TPPOS_INVALID_LOCATION = {PATH_COMMANDS, "Tppos", PATH_EXCEPTIONS, "InvalidLocation"};
	public static final Object[] COMMANDS_CLEAR_SUCCES = {PATH_COMMANDS, COMMAND_CLEAR, PATH_EXCEPTIONS, "Success"};
	public static final Object[] COMMANDS_CLEAR_SUCCES_OTHER = {PATH_COMMANDS, COMMAND_CLEAR, PATH_EXCEPTIONS, "SuccessOther"};
	public static final Object[] COMMANDS_CLEAR_SUCCES_STAFF = {PATH_COMMANDS, COMMAND_CLEAR, PATH_EXCEPTIONS, "SuccessStaff"};
	public static final Object[] COMMANDS_REPAIR_SUCCES = {PATH_COMMANDS, COMMAND_REPAIR, "Success"};
	public static final Object[] COMMANDS_REPAIR_SUCCES_STAFF = {PATH_COMMANDS, COMMAND_REPAIR, "SuccessStaff"};
	public static final Object[] COMMANDS_REPAIR_SUCCES_OTHER = {PATH_COMMANDS, COMMAND_REPAIR, "SuccessOther"};

}
