package sawfowl.commandpack.configure.locale;

public class LocalesPaths {

	private static final String PATH_ECONOMY = "Economy";

	private static final String PATH_COMMANDS = "CommandsConfig";

	//private static final String PATH_EVENTS = "Events";

	//private static final String PATH_HOVER = "HoverText";

	private static final Object PATH_EXCEPTIONS = "Exceptions";
	private static final Object PATH_INCOMPLETE = "Incomplete";
	private static final Object PATH_TIME = "ServerTime";
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
	private static final Object COMMAND_RANDOM_TELEPORT = "RandomTeleport";
	private static final Object COMMAND_BACK = "Back";
	private static final Object COMMAND_CLEAR = "Clear";
	private static final Object COMMAND_REPAIR = "Repair";
	private static final Object COMMAND_FLY = "Fly";
	private static final Object COMMAND_GODMODE = "GodMode";
	private static final Object COMMAND_SPEED = "Speed";
	private static final Object COMMAND_DISPOSAL = "Disposal";
	private static final Object COMMAND_GAMEMODE = "GameMode";
	private static final Object COMMAND_WEATHER = "Weather";
	private static final Object COMMAND_TIME = "ServerTime";
	private static final Object COMMAND_ENCHANT = "Enchant";
	private static final Object COMMAND_ANVIL = "Anvil";
	private static final Object COMMAND_CRAFTING_TABLE = "CraftingTable";
	private static final Object COMMAND_ENCHANTMENT_TABLE = "EnchantmentTable";
	private static final Object COMMAND_FEED = "Feed";
	private static final Object COMMAND_HEAL = "Heal";
	private static final Object COMMAND_BROADCAST = "Broadcast";
	private static final Object COMMAND_SUDO = "Sudo";
	private static final Object COMMAND_VANISH = "Vanish";
	private static final Object COMMAND_NICK = "Nick";
	private static final Object COMMAND_ITEM = "Item";
	private static final Object COMMAND_SERVERSTAT = "ServerStat";
	private static final Object COMMAND_KITS = "Kits";
	private static final Object COMMAND_KIT = "Kit";
	private static final Object COMMAND_AFK = "Afk";
	private static final Object COMMAND_WORLD = "World";
	private static final Object COMMAND_COMMANDSPY = "CommandSpy";
	private static final Object COMMAND_PING = "Ping";
	private static final Object COMMAND_INVSEE = "InventorySee";
	private static final Object COMMAND_LIST = "List";
	private static final Object COMMAND_SEEN = "Seen";
	private static final Object COMMAND_HELP = "Help";
	private static final Object COMMAND_GLOW = "Glow";
	private static final Object COMMAND_FLAME = "Flame";
	private static final Object COMMAND_EXTINGUISH = "Extinguish";

	public static final Object[] TIME_DAYS = {PATH_TIME, "Days"};
	public static final Object[] TIME_HOUR = {PATH_TIME, "Hour"};
	public static final Object[] TIME_MINUTE = {PATH_TIME, "Minute"};
	public static final Object[] TIME_SECOND = {PATH_TIME, "Second"};

	public static final Object[] REMOVE = {OTHER, "Remove"};
	public static final Object[] TELEPORT = {OTHER, "Remove"};
	public static final Object[] TELEPORTCLICKABLE = {OTHER, "RemoveClickable"};
	public static final Object[] MOTD = {OTHER, "Motd"};
	public static final Object[] JOIN_MESSAGE = {OTHER, "JoinMessage"};
	public static final Object[] FIRST_JOIN_MESSAGE = {OTHER, "FirstJoinMessage"};
	public static final Object[] LEAVE_MESSAGE = {OTHER, "LeaveMessage"};
	public static final Object[] KEEP_INVENTORY = {OTHER, "KeepInventory"};
	public static final Object[] KEEP_EXP = {OTHER, "KeepExp"};
	public static final Object[] PLAYER_MODS_LIST = {OTHER, "PlayerModsList"};
	public static final Object[] ILLEGAL_MODS_LIST = {OTHER, "IllegalMods"};

	public static final Object[] ECONOMY_NOT_FOUND = {PATH_ECONOMY, PATH_EXCEPTIONS, "EconomyNotFound"};
	public static final Object[] ECONOMY_ERROR_GIVE_MONEY = {PATH_ECONOMY, PATH_EXCEPTIONS, "ErrorGiveMoney"};
	public static final Object[] ECONOMY_ERROR_TAKE_MONEY = {PATH_ECONOMY, PATH_EXCEPTIONS, "ErrorTakeMoney"};

	public static final Object[] NAME_SYSTEM = {NAMES, "SystemSubject"};
	public static final Object[] NAME_COMMANDBLOCK = {NAMES, "CommandBlock"};
	public static final Object[] NAME_COMMANDBLOCK_MINECART = {NAMES, "CommandBlockMineCart"};
	public static final Object[] NAME_UNKNOWN = {NAMES, "UnknownSource"};
	public static final Object[] NAME_BACKPACK = {NAMES, "Backpack"};

	public static final Object[] COMMANDS_EXCEPTION_PLAYER_IS_OFFLINE = {PATH_COMMANDS, PATH_EXCEPTIONS, PATH_INCOMPLETE, "PlayerIsOffline"};
	public static final Object[] COMMANDS_EXCEPTION_USER_NOT_PRESENT = {PATH_COMMANDS, PATH_EXCEPTIONS, PATH_INCOMPLETE, "UserNotPresent"};
	public static final Object[] COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT = {PATH_COMMANDS, PATH_EXCEPTIONS, PATH_INCOMPLETE, "PlayerNotPresent"};
	public static final Object[] COMMANDS_EXCEPTION_TARGET_SELF = {PATH_COMMANDS, PATH_EXCEPTIONS, PATH_INCOMPLETE, "TargetSelf"};
	public static final Object[] COMMANDS_EXCEPTION_BOOLEAN_NOT_PRESENT = {PATH_COMMANDS, PATH_EXCEPTIONS, PATH_INCOMPLETE, "BooleanNotPresent"};
	public static final Object[] COMMANDS_EXCEPTION_LOCATION_NOT_PRESENT = {PATH_COMMANDS, PATH_EXCEPTIONS, PATH_INCOMPLETE, "LocationNotPresent"};
	public static final Object[] COMMANDS_EXCEPTION_WORLD_NOT_PRESENT = {PATH_COMMANDS, PATH_EXCEPTIONS, PATH_INCOMPLETE, "WorldNotPresent"};
	public static final Object[] COMMANDS_EXCEPTION_NAME_NOT_PRESENT = {PATH_COMMANDS, PATH_EXCEPTIONS, PATH_INCOMPLETE, "NameNotPresent"};
	public static final Object[] COMMANDS_EXCEPTION_TYPE_NOT_PRESENT = {PATH_COMMANDS, PATH_EXCEPTIONS, PATH_INCOMPLETE, "TypeNotPresent"};
	public static final Object[] COMMANDS_EXCEPTION_VALUE_NOT_PRESENT = {PATH_COMMANDS, PATH_EXCEPTIONS, PATH_INCOMPLETE, "ValueNotPresent"};
	public static final Object[] COMMANDS_EXCEPTION_COOLDOWN_INCORRECT_TIME = {PATH_COMMANDS, PATH_EXCEPTIONS, "IncorrectTime"};
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
	public static final Object[] COMMANDS_RANDOM_TELEPORT_POSITION_SEARCH_ERROR_STAFF = {PATH_COMMANDS, COMMAND_RANDOM_TELEPORT, PATH_EXCEPTIONS, "PositionSearchErrorStaff"};
	public static final Object[] COMMANDS_RANDOM_TELEPORT_POSITION_SEARCH_ERROR = {PATH_COMMANDS, COMMAND_RANDOM_TELEPORT, PATH_EXCEPTIONS, "PositionSearchError"};
	public static final Object[] COMMANDS_RANDOM_TELEPORT_SUCCES_STAFF = {PATH_COMMANDS, COMMAND_RANDOM_TELEPORT, "SuccessStaff"};
	public static final Object[] COMMANDS_RANDOM_TELEPORT_SUCCES = {PATH_COMMANDS, COMMAND_RANDOM_TELEPORT, "Success"};
	public static final Object[] COMMANDS_RANDOM_TELEPORT_WAIT = {PATH_COMMANDS, COMMAND_RANDOM_TELEPORT, "PleaseWait"};
	public static final Object[] COMMANDS_RANDOM_TELEPORT_CANCELLED = {PATH_COMMANDS, COMMAND_RANDOM_TELEPORT, "Cancelled"};
	public static final Object[] COMMANDS_BACK_EMPTY = {PATH_COMMANDS, COMMAND_BACK, PATH_EXCEPTIONS, "Empty"};
	public static final Object[] COMMANDS_BACK_NOT_LOADED_WORLD = {PATH_COMMANDS, COMMAND_BACK, PATH_EXCEPTIONS, "NotLoadedWorld"};
	public static final Object[] COMMANDS_TPPOS_INVALID_LOCATION = {PATH_COMMANDS, "Tppos", PATH_EXCEPTIONS, "InvalidLocation"};
	public static final Object[] COMMANDS_CLEAR_SUCCES = {PATH_COMMANDS, COMMAND_CLEAR, PATH_EXCEPTIONS, "Success"};
	public static final Object[] COMMANDS_CLEAR_SUCCES_OTHER = {PATH_COMMANDS, COMMAND_CLEAR, PATH_EXCEPTIONS, "SuccessOther"};
	public static final Object[] COMMANDS_CLEAR_SUCCES_STAFF = {PATH_COMMANDS, COMMAND_CLEAR, PATH_EXCEPTIONS, "SuccessStaff"};
	public static final Object[] COMMANDS_REPAIR_SUCCES = {PATH_COMMANDS, COMMAND_REPAIR, "Success"};
	public static final Object[] COMMANDS_REPAIR_SUCCES_STAFF = {PATH_COMMANDS, COMMAND_REPAIR, "SuccessStaff"};
	public static final Object[] COMMANDS_REPAIR_SUCCES_OTHER = {PATH_COMMANDS, COMMAND_REPAIR, "SuccessOther"};
	public static final Object[] COMMANDS_JUMP_EXCEPTION = {PATH_COMMANDS, "Jump", PATH_EXCEPTIONS, "CantGetPosition"};
	public static final Object[] COMMANDS_FLY_ENABLE = {PATH_COMMANDS, COMMAND_FLY, "Enable"};
	public static final Object[] COMMANDS_FLY_ENABLE_STAFF = {PATH_COMMANDS, COMMAND_FLY, "EnableStaff"};
	public static final Object[] COMMANDS_FLY_DISABLE = {PATH_COMMANDS, COMMAND_FLY, "Disable"};
	public static final Object[] COMMANDS_FLY_DISABLE_STAFF = {PATH_COMMANDS, COMMAND_FLY, "DisableStaff"};
	public static final Object[] COMMANDS_GODMODE_ENABLE = {PATH_COMMANDS, COMMAND_GODMODE, "Enable"};
	public static final Object[] COMMANDS_GODMODE_ENABLE_STAFF = {PATH_COMMANDS, COMMAND_GODMODE, "EnableStaff"};
	public static final Object[] COMMANDS_GODMODE_DISABLE = {PATH_COMMANDS, COMMAND_GODMODE, "Disable"};
	public static final Object[] COMMANDS_GODMODE_DISABLE_STAFF = {PATH_COMMANDS, COMMAND_GODMODE, "DisableStaff"};
	public static final Object[] COMMANDS_SPEED_SELF = {PATH_COMMANDS, COMMAND_SPEED, "Self"};
	public static final Object[] COMMANDS_SPEED_STAFF = {PATH_COMMANDS, COMMAND_SPEED, "Staff"};
	public static final Object[] COMMANDS_SPEED_OTHER = {PATH_COMMANDS, COMMAND_SPEED, "Other"};
	public static final Object[] COMMANDS_SPEED_FLY = {PATH_COMMANDS, COMMAND_SPEED, "Fly"};
	public static final Object[] COMMANDS_SPEED_DEFAULT = {PATH_COMMANDS, COMMAND_SPEED, "Default"};
	public static final Object[] COMMANDS_DISPOSAL_TITLE = {PATH_COMMANDS, COMMAND_DISPOSAL, "Title"};
	public static final Object[] COMMANDS_GAMEMODE_SUCCESS = {PATH_COMMANDS, COMMAND_GAMEMODE, "Success"};
	public static final Object[] COMMANDS_GAMEMODE_OTHER = {PATH_COMMANDS, COMMAND_GAMEMODE, "Other"};
	public static final Object[] COMMANDS_GAMEMODE_CREATIVE = {PATH_COMMANDS, COMMAND_GAMEMODE, "Creative"};
	public static final Object[] COMMANDS_GAMEMODE_SPECTATOR = {PATH_COMMANDS, COMMAND_GAMEMODE, "Spectator"};
	public static final Object[] COMMANDS_GAMEMODE_SURVIVAL = {PATH_COMMANDS, COMMAND_GAMEMODE, "Survival"};
	public static final Object[] COMMANDS_GAMEMODE_ADVENTURE = {PATH_COMMANDS, COMMAND_GAMEMODE, "Adventure"};
	public static final Object[] COMMANDS_WEATHER_SUN = {PATH_COMMANDS, COMMAND_WEATHER, "Sun"};
	public static final Object[] COMMANDS_WEATHER_RAIN = {PATH_COMMANDS, COMMAND_WEATHER, "Rain"};
	public static final Object[] COMMANDS_WEATHER_THUNDER = {PATH_COMMANDS, COMMAND_WEATHER, "Thunder"};
	public static final Object[] COMMANDS_TIME_DAY = {PATH_COMMANDS, COMMAND_TIME, "Day"};
	public static final Object[] COMMANDS_TIME_MORNING = {PATH_COMMANDS, COMMAND_TIME, "Morning"};
	public static final Object[] COMMANDS_TIME_EVENING = {PATH_COMMANDS, COMMAND_TIME, "Evening"};
	public static final Object[] COMMANDS_TIME_NIGHT = {PATH_COMMANDS, COMMAND_TIME, "Night"};
	public static final Object[] COMMANDS_TIME_ADD = {PATH_COMMANDS, COMMAND_TIME, "Add"};
	public static final Object[] COMMANDS_ENCHANT_ITEM_IS_NOT_PRESENT = {PATH_COMMANDS, COMMAND_ENCHANT, PATH_EXCEPTIONS, "ItemIsNotPresent"};
	public static final Object[] COMMANDS_ENCHANT_SUCCES = {PATH_COMMANDS, COMMAND_ENCHANT, "Success"};
	public static final Object[] COMMANDS_ANVIL = {PATH_COMMANDS, COMMAND_ANVIL, "Other"};
	public static final Object[] COMMANDS_CRAFTING_TABLE = {PATH_COMMANDS, COMMAND_CRAFTING_TABLE, "Other"};
	public static final Object[] COMMANDS_ENCHANTMENT_TABLE = {PATH_COMMANDS, COMMAND_ENCHANTMENT_TABLE, "Other"};
	public static final Object[] COMMANDS_FEED_SELF = {PATH_COMMANDS, COMMAND_FEED, "Self"};
	public static final Object[] COMMANDS_FEED_OTHER = {PATH_COMMANDS, COMMAND_FEED, "Other"};
	public static final Object[] COMMANDS_HEAL_SELF = {PATH_COMMANDS, COMMAND_HEAL, "Self"};
	public static final Object[] COMMANDS_HEAL_OTHER = {PATH_COMMANDS, COMMAND_HEAL, "Other"};
	public static final Object[] COMMANDS_BROADCAST = {PATH_COMMANDS, COMMAND_BROADCAST, "Prefix"};
	public static final Object[] COMMANDS_SUDO_COMMAND_NOT_FOUND = {PATH_COMMANDS, COMMAND_SUDO, PATH_EXCEPTIONS, "CommandNotFound"};
	public static final Object[] COMMANDS_SUDO_EXECUTE_NOT_ALLOWED = {PATH_COMMANDS, COMMAND_SUDO, PATH_EXCEPTIONS, "ExecuteNotAllowed"};
	public static final Object[] COMMANDS_SUDO_SUCCESS = {PATH_COMMANDS, COMMAND_SUDO, "Success"};
	public static final Object[] COMMANDS_VANISH_VASHISHED = {PATH_COMMANDS, COMMAND_VANISH, "Vanished"};
	public static final Object[] COMMANDS_VANISH_UNVASHISHED = {PATH_COMMANDS, COMMAND_VANISH, "Unvanished"};
	public static final Object[] COMMANDS_VANISH_VASHISHED_STAFF = {PATH_COMMANDS, COMMAND_VANISH, "VanishedStaff"};
	public static final Object[] COMMANDS_VANISH_UNVASHISHED_STAFF = {PATH_COMMANDS, COMMAND_VANISH, "UnvanishedStaff"};
	public static final Object[] COMMANDS_NICK_SET_SELF = {PATH_COMMANDS, COMMAND_NICK, "SetSelf"};
	public static final Object[] COMMANDS_NICK_CLEAR_SELF = {PATH_COMMANDS, COMMAND_NICK, "ClearSelf"};
	public static final Object[] COMMANDS_NICK_SET_STAFF = {PATH_COMMANDS, COMMAND_NICK, "SetStaff"};
	public static final Object[] COMMANDS_NICK_CLEAR_STAFF = {PATH_COMMANDS, COMMAND_NICK, "ClearStaff"};
	public static final Object[] COMMANDS_ITEM_EMPTY_HAND = {PATH_COMMANDS, COMMAND_ITEM, PATH_EXCEPTIONS, "EmptyHand"};
	public static final Object[] COMMANDS_ITEM_SET_NAME = {PATH_COMMANDS, COMMAND_ITEM, "SetName"};
	public static final Object[] COMMANDS_ITEM_CLEAR_NAME = {PATH_COMMANDS, COMMAND_ITEM, "ClearName"};
	public static final Object[] COMMANDS_ITEM_SET_LORE = {PATH_COMMANDS, COMMAND_ITEM, "SetLore"};
	public static final Object[] COMMANDS_ITEM_CLEAR_LORE = {PATH_COMMANDS, COMMAND_ITEM, "ClearLore"};
	public static final Object[] COMMANDS_SERVERSTAT_HEADER = {PATH_COMMANDS, COMMAND_SERVERSTAT, "Header"};
	public static final Object[] COMMANDS_SERVERSTAT_BUTTON_SYSTEM = {PATH_COMMANDS, COMMAND_SERVERSTAT, "SystemButton"};
	public static final Object[] COMMANDS_SERVERSTAT_BUTTON_WORLDS = {PATH_COMMANDS, COMMAND_SERVERSTAT, "WorldsButton"};
	public static final Object[] COMMANDS_SERVERSTAT_BUTTON_PLUGINS = {PATH_COMMANDS, COMMAND_SERVERSTAT, "PluginsButton"};
	public static final Object[] COMMANDS_SERVERSTAT_BUTTON_MODS = {PATH_COMMANDS, COMMAND_SERVERSTAT, "ModsButton"};
	public static final Object[] COMMANDS_SERVERSTAT_TPS = {PATH_COMMANDS, COMMAND_SERVERSTAT, "TPS"};
	public static final Object[] COMMANDS_SERVERSTAT_UPTIME = {PATH_COMMANDS, COMMAND_SERVERSTAT, "Uptime"};
	public static final Object[] COMMANDS_SERVERSTAT_MEMORY_MAX = {PATH_COMMANDS, COMMAND_SERVERSTAT, "MemoryMax"};
	public static final Object[] COMMANDS_SERVERSTAT_MEMORY_ALLOCATED = {PATH_COMMANDS, COMMAND_SERVERSTAT, "MemoryAllocated"};
	public static final Object[] COMMANDS_SERVERSTAT_MEMORY_UTILISED = {PATH_COMMANDS, COMMAND_SERVERSTAT, "MemoryUtilised"};
	public static final Object[] COMMANDS_SERVERSTAT_MEMORY_FREE = {PATH_COMMANDS, COMMAND_SERVERSTAT, "MemoryFree"};
	public static final Object[] COMMANDS_SERVERSTAT_TIME = {PATH_COMMANDS, COMMAND_SERVERSTAT, "ServerTime"};
	public static final Object[] COMMANDS_SERVERSTAT_OS_HEADER = {PATH_COMMANDS, COMMAND_SERVERSTAT, "HeaderOS"};
	public static final Object[] COMMANDS_SERVERSTAT_OS = {PATH_COMMANDS, COMMAND_SERVERSTAT, "OS"};
	public static final Object[] COMMANDS_SERVERSTAT_JAVA = {PATH_COMMANDS, COMMAND_SERVERSTAT, "Java"};
	public static final Object[] COMMANDS_SERVERSTAT_JAVAHOME = {PATH_COMMANDS, COMMAND_SERVERSTAT, "JavaHome"};
	public static final Object[] COMMANDS_SERVERSTAT_WORLDS_INFO_HEADER = {PATH_COMMANDS, COMMAND_SERVERSTAT, "HeaderWorlds"};
	public static final Object[] COMMANDS_SERVERSTAT_WORLDINFO = {PATH_COMMANDS, COMMAND_SERVERSTAT, "WorldInfo"};
	public static final Object[] COMMANDS_SERVERSTAT_HEADER_PLUGINS = {PATH_COMMANDS, COMMAND_SERVERSTAT, "HeaderPlugins"};
	public static final Object[] COMMANDS_SERVERSTAT_HEADER_MODS = {PATH_COMMANDS, COMMAND_SERVERSTAT, "HeaderMods"};
	public static final Object[] COMMANDS_SERVERSTAT_PLUGIN_REFRESH_BUTTON = {PATH_COMMANDS, COMMAND_SERVERSTAT, "PluginRefreshButton"};
	public static final Object[] COMMANDS_SERVERSTAT_PLUGIN_REFRESH_MESSAGE = {PATH_COMMANDS, COMMAND_SERVERSTAT, "PluginRefreshMessage"};
	public static final Object[] COMMANDS_SERVERSTAT_TIMEFORMAT = {PATH_COMMANDS, COMMAND_SERVERSTAT, "TimeFormat"};
	public static final Object[] COMMANDS_SERVERSTAT_PLUGIN_INFO_HEADER = {PATH_COMMANDS, COMMAND_SERVERSTAT, "PluginInfo", "Header"};
	public static final Object[] COMMANDS_SERVERSTAT_PLUGIN_INFO_ID = {PATH_COMMANDS, COMMAND_SERVERSTAT, "PluginInfo", "ID"};
	public static final Object[] COMMANDS_SERVERSTAT_PLUGIN_INFO_NAME = {PATH_COMMANDS, COMMAND_SERVERSTAT, "PluginInfo", "Name"};
	public static final Object[] COMMANDS_SERVERSTAT_PLUGIN_INFO_VERSION = {PATH_COMMANDS, COMMAND_SERVERSTAT, "PluginInfo", "Version"};
	public static final Object[] COMMANDS_SERVERSTAT_PLUGIN_INFO_ENTRYPOINT = {PATH_COMMANDS, COMMAND_SERVERSTAT, "PluginInfo", "Entrypoint"};
	public static final Object[] COMMANDS_SERVERSTAT_PLUGIN_INFO_DESCRIPTION = {PATH_COMMANDS, COMMAND_SERVERSTAT, "PluginInfo", "Description"};
	public static final Object[] COMMANDS_SERVERSTAT_PLUGIN_INFO_DEPENDENCIES = {PATH_COMMANDS, COMMAND_SERVERSTAT, "PluginInfo", "Dependencies"};
	public static final Object[] COMMANDS_SERVERSTAT_PLUGIN_INFO_CONTRIBUTORS = {PATH_COMMANDS, COMMAND_SERVERSTAT, "PluginInfo", "Contributors"};
	public static final Object[] COMMANDS_SERVERSTAT_PLUGIN_INFO_LINKS = {PATH_COMMANDS, COMMAND_SERVERSTAT, "PluginInfo", "Links"};
	public static final Object[] COMMANDS_SERVERSTAT_MOD_INFO_HEADER = {PATH_COMMANDS, COMMAND_SERVERSTAT, "ModInfo", "Header"};
	public static final Object[] COMMANDS_SERVERSTAT_MOD_INFO_ID = {PATH_COMMANDS, COMMAND_SERVERSTAT, "ModInfo", "ID"};
	public static final Object[] COMMANDS_SERVERSTAT_MOD_INFO_NAME = {PATH_COMMANDS, COMMAND_SERVERSTAT, "ModInfo", "Name"};
	public static final Object[] COMMANDS_SERVERSTAT_MOD_INFO_VERSION = {PATH_COMMANDS, COMMAND_SERVERSTAT, "ModInfo", "Version"};
	public static final Object[] COMMANDS_SERVERSTAT_MOD_INFO_DESCRIPTION = {PATH_COMMANDS, COMMAND_SERVERSTAT, "ModInfo", "Description"};
	public static final Object[] COMMANDS_SERVERSTAT_MOD_INFO_DEPENDENCIES = {PATH_COMMANDS, COMMAND_SERVERSTAT, "ModInfo", "Dependencies"};
	public static final Object[] COMMANDS_SERVERSTAT_MOD_INFO_LINKS = {PATH_COMMANDS, COMMAND_SERVERSTAT, "ModInfo", "Links"};
	public static final Object[] COMMANDS_KITS_CREATE_EXIST = {PATH_COMMANDS, COMMAND_KITS, "Create", PATH_EXCEPTIONS, "Exist"};
	public static final Object[] COMMANDS_KITS_SAVED = {PATH_COMMANDS, COMMAND_KITS, "Saved"};
	public static final Object[] COMMANDS_KITS_LIST_HEADER = {PATH_COMMANDS, COMMAND_KITS, "ListHeader"};
	public static final Object[] COMMANDS_KITS_NO_KITS = {PATH_COMMANDS, COMMAND_KITS, "NoKits"};
	public static final Object[] COMMANDS_KITS_COOLDOWN_SUCCESS = {PATH_COMMANDS, COMMAND_KITS, "Cooldown", "Success"};
	public static final Object[] COMMANDS_KITS_SET_NAME = {PATH_COMMANDS, COMMAND_KITS, "SetName"};
	public static final Object[] COMMANDS_KITS_GIVE_RULE = {PATH_COMMANDS, COMMAND_KITS, "GiveRule"};
	public static final Object[] COMMANDS_KITS_CREATE_LORE = {PATH_COMMANDS, COMMAND_KITS, "CreateLore"};
	public static final Object[] COMMANDS_KITS_GIVE_LIMIT = {PATH_COMMANDS, COMMAND_KITS, "GiveLimit"};
	public static final Object[] COMMANDS_KITS_FIRST_TIME_ENABLE = {PATH_COMMANDS, COMMAND_KITS, "EnableFirstTime"};
	public static final Object[] COMMANDS_KITS_FIRST_TIME_DISABLE = {PATH_COMMANDS, COMMAND_KITS, "DisableFirstTime"};
	public static final Object[] COMMANDS_KITS_GIVE_ON_JOIN_ENABLE = {PATH_COMMANDS, COMMAND_KITS, "EnableGiveOnJoin"};
	public static final Object[] COMMANDS_KITS_GIVE_ON_JOIN_DISABLE = {PATH_COMMANDS, COMMAND_KITS, "DisableGiveOnJoin"};
	public static final Object[] COMMANDS_KITS_NEEDPERM_ENABLE = {PATH_COMMANDS, COMMAND_KITS, "EnableNeedPerm"};
	public static final Object[] COMMANDS_KITS_NEEDPERM_DISABLE = {PATH_COMMANDS, COMMAND_KITS, "DisableNeedPerm"};
	public static final Object[] COMMANDS_KITS_ADD_COMMAND = {PATH_COMMANDS, COMMAND_KITS, "AddCommand"};
	public static final Object[] COMMANDS_KITS_COMMANDS_REMOVE_FAIL = {PATH_COMMANDS, COMMAND_KITS, "CommandsRemoveFail"};
	public static final Object[] COMMANDS_KITS_COMMANDS_REMOVE_SUCCESS = {PATH_COMMANDS, COMMAND_KITS, "CommandsRemoveSuccess"};
	public static final Object[] COMMANDS_KITS_COMMANDS_EMPTY = {PATH_COMMANDS, COMMAND_KITS, "CommandsEmpty"};
	public static final Object[] COMMANDS_KITS_COMMANDS_HEADER = {PATH_COMMANDS, COMMAND_KITS, "CommandsHeader"};
	public static final Object[] COMMANDS_KITS_SET_PRICE = {PATH_COMMANDS, COMMAND_KITS, "SetPrice"};
	public static final Object[] COMMANDS_KIT_LIST_HEADER = {PATH_COMMANDS, COMMAND_KIT, "ListHeader"};
	public static final Object[] COMMANDS_KIT_VIEW = {PATH_COMMANDS, COMMAND_KIT, "View"};
	public static final Object[] COMMANDS_KIT_NO_KITS = {PATH_COMMANDS, COMMAND_KIT, "NoKits"};
	public static final Object[] COMMANDS_KIT_NO_PERM = {PATH_COMMANDS, COMMAND_KIT, "NoPerm"};
	public static final Object[] COMMANDS_KIT_WAIT = {PATH_COMMANDS, COMMAND_KIT, "Wait"};
	public static final Object[] COMMANDS_KIT_INVENTORY_FULL = {PATH_COMMANDS, COMMAND_KIT, "InventoryFull"};
	public static final Object[] COMMANDS_KIT_GIVE_LIMIT = {PATH_COMMANDS, COMMAND_KIT, "GiveLimit"};
	public static final Object[] COMMANDS_KIT_NO_MONEY = {PATH_COMMANDS, COMMAND_KIT, "NoMoney"};
	public static final Object[] COMMANDS_KIT_SUCCESS = {PATH_COMMANDS, COMMAND_KIT, "Success"};
	public static final Object[] COMMANDS_KIT_SUCCESS_STAFF = {PATH_COMMANDS, COMMAND_KIT, "SuccessStaff"};
	public static final Object[] COMMANDS_AFK_ENABLE = {PATH_COMMANDS, COMMAND_AFK, "Enable"};
	public static final Object[] COMMANDS_AFK_DISABLE = {PATH_COMMANDS, COMMAND_AFK, "Disable"};
	public static final Object[] COMMANDS_AFK_TITLE = {PATH_COMMANDS, COMMAND_AFK, "Title"};
	public static final Object[] COMMANDS_AFK_SUBTITLE = {PATH_COMMANDS, COMMAND_AFK, "Subtitle"};
	public static final Object[] COMMANDS_AFK_ENABLE_IN_VANISH = {PATH_COMMANDS, COMMAND_AFK, "EnableInVanish"};
	public static final Object[] COMMANDS_AFK_DISABLE_IN_VANISH = {PATH_COMMANDS, COMMAND_AFK, "DisableInVanish"};
	public static final Object[] COMMANDS_AFK_KICK = {PATH_COMMANDS, COMMAND_AFK, "Kick"};
	public static final Object[] COMMANDS_WORLD_CREATE = {PATH_COMMANDS, COMMAND_WORLD, "Create"};
	public static final Object[] COMMANDS_WORLD_TELEPORT = {PATH_COMMANDS, COMMAND_WORLD, "Teleport"};
	public static final Object[] COMMANDS_WORLD_TELEPORT_OTHER = {PATH_COMMANDS, COMMAND_WORLD, "TeleportOther"};
	public static final Object[] COMMANDS_WORLD_DELETE = {PATH_COMMANDS, COMMAND_WORLD, "Delete"};
	public static final Object[] COMMANDS_WORLD_LOAD = {PATH_COMMANDS, COMMAND_WORLD, "Load"};
	public static final Object[] COMMANDS_WORLD_LOADED = {PATH_COMMANDS, COMMAND_WORLD, PATH_EXCEPTIONS, "Loaded"};
	public static final Object[] COMMANDS_WORLD_UNLOAD = {PATH_COMMANDS, COMMAND_WORLD, "Unload"};
	public static final Object[] COMMANDS_WORLD_UNLOADED = {PATH_COMMANDS, COMMAND_WORLD, PATH_EXCEPTIONS, "Unloaded"};
	public static final Object[] COMMANDS_WORLD_SETSPAWN = {PATH_COMMANDS, COMMAND_WORLD, "SetSpawn"};
	public static final Object[] COMMANDS_WORLD_SETBORDER = {PATH_COMMANDS, COMMAND_WORLD, "SetBorder"};
	public static final Object[] COMMANDS_WORLD_ENABLE = {PATH_COMMANDS, COMMAND_WORLD, "Enable"};
	public static final Object[] COMMANDS_WORLD_DISABLE = {PATH_COMMANDS, COMMAND_WORLD, "Disable"};
	public static final Object[] COMMANDS_WORLD_ENABLE_PVP = {PATH_COMMANDS, COMMAND_WORLD, "EnablePvP"};
	public static final Object[] COMMANDS_WORLD_DISABLE_PVP = {PATH_COMMANDS, COMMAND_WORLD, "DisablePvP"};
	public static final Object[] COMMANDS_WORLD_DIFFICULTY_PEACEFUL = {PATH_COMMANDS, COMMAND_WORLD, "Difficulty", "Peaceful"};
	public static final Object[] COMMANDS_WORLD_DIFFICULTY_EASY = {PATH_COMMANDS, COMMAND_WORLD, "Difficulty", "Easy"};
	public static final Object[] COMMANDS_WORLD_DIFFICULTY_NORMAL = {PATH_COMMANDS, COMMAND_WORLD, "Difficulty", "Normal"};
	public static final Object[] COMMANDS_WORLD_DIFFICULTY_HARD = {PATH_COMMANDS, COMMAND_WORLD, "Difficulty", "Hard"};
	public static final Object[] COMMANDS_WORLD_GAMEMODE_SURVIVAL = {PATH_COMMANDS, COMMAND_WORLD, "GameMode", "Survival"};
	public static final Object[] COMMANDS_WORLD_GAMEMODE_CREATIVE = {PATH_COMMANDS, COMMAND_WORLD, "GameMode", "Creative"};
	public static final Object[] COMMANDS_WORLD_GAMEMODE_ADVENTURE = {PATH_COMMANDS, COMMAND_WORLD, "GameMode", "Adventure"};
	public static final Object[] COMMANDS_WORLD_GAMEMODE_SPECTATOR = {PATH_COMMANDS, COMMAND_WORLD, "GameMode", "Spectator"};
	public static final Object[] COMMANDS_WORLD_VIEWDISTANCE = {PATH_COMMANDS, COMMAND_WORLD, "ViewDistance"};
	public static final Object[] COMMANDS_WORLD_GAMERULE_SUCCESS = {PATH_COMMANDS, COMMAND_WORLD, "GameRule", "Success"};
	public static final Object[] COMMANDS_WORLD_GAMERULE_INCORECT_VALUE = {PATH_COMMANDS, COMMAND_WORLD, PATH_EXCEPTIONS, "GameRule", "IncorectValue"};
	public static final Object[] COMMANDS_WORLD_GAMERULE_UNKNOWN_TYPE = {PATH_COMMANDS, COMMAND_WORLD, PATH_EXCEPTIONS, "GameRule", "UnknownType"};
	public static final Object[] COMMANDS_WORLD_GAMERULES = {PATH_COMMANDS, COMMAND_WORLD, "GameRules", "List"};
	public static final Object[] COMMANDS_WORLD_DEBUG_GENERATE = {PATH_COMMANDS, COMMAND_WORLD, "Generate", "Debug"};
	public static final Object[] COMMANDS_WORLD_START_GENERATE = {PATH_COMMANDS, COMMAND_WORLD, "Generate", "Start"};
	public static final Object[] COMMANDS_WORLD_NOT_STARTED_GENERATE = {PATH_COMMANDS, COMMAND_WORLD, "Generate", "NotStarted"};
	public static final Object[] COMMANDS_WORLD_NOT_PAUSED_GENERATE = {PATH_COMMANDS, COMMAND_WORLD, "Generate", "NotPaused"};
	public static final Object[] COMMANDS_WORLD_STOP_GENERATE = {PATH_COMMANDS, COMMAND_WORLD, "Generate", "Stop"};
	public static final Object[] COMMANDS_WORLD_PAUSE_GENERATE = {PATH_COMMANDS, COMMAND_WORLD, "Generate", "Pause"};
	public static final Object[] COMMANDS_WORLD_SPAWN_LOGIC_ENABLE = {PATH_COMMANDS, COMMAND_WORLD, "SpawnLogic", "Enable"};
	public static final Object[] COMMANDS_WORLD_SPAWN_LOGIC_DISABLE = {PATH_COMMANDS, COMMAND_WORLD, "SpawnLogic", "Disable"};
	public static final Object[] COMMANDS_COMMANDSPY_ENABLE = {PATH_COMMANDS, COMMAND_COMMANDSPY, "Enable"};
	public static final Object[] COMMANDS_COMMANDSPY_DISABLE = {PATH_COMMANDS, COMMAND_COMMANDSPY, "Disable"};
	public static final Object[] COMMANDS_COMMANDSPY_SPY = {PATH_COMMANDS, COMMAND_COMMANDSPY, "Spy"};
	public static final Object[] COMMANDS_PING_SUCCESS = {PATH_COMMANDS, COMMAND_PING, "Success"};
	public static final Object[] COMMANDS_PING_SUCCESS_STAFF = {PATH_COMMANDS, COMMAND_PING, "SuccessStaff"};
	public static final Object[] COMMANDS_LIST_SUCCESS = {PATH_COMMANDS, COMMAND_LIST, "Success"};
	public static final Object[] COMMANDS_LIST_VANISHED = {PATH_COMMANDS, COMMAND_LIST, "Vanished"};
	public static final Object[] COMMANDS_INVSEE_TITLE = {PATH_COMMANDS, COMMAND_INVSEE, "Title"};
	public static final Object[] COMMANDS_SEEN_TITLE = {PATH_COMMANDS, COMMAND_SEEN, "Title"};
	public static final Object[] COMMANDS_SEEN_ONLINE = {PATH_COMMANDS, COMMAND_SEEN, "Online"};
	public static final Object[] COMMANDS_SEEN_OFFLINE = {PATH_COMMANDS, COMMAND_SEEN, "Offline"};
	public static final Object[] COMMANDS_SEEN_ONLINE_TIME = {PATH_COMMANDS, COMMAND_SEEN, "OnlineTime"};
	public static final Object[] COMMANDS_SEEN_LAST_ONLINE = {PATH_COMMANDS, COMMAND_SEEN, "LastOnline"};
	public static final Object[] COMMANDS_SEEN_DISPLAY_NAME = {PATH_COMMANDS, COMMAND_SEEN, "DisplayName"};
	public static final Object[] COMMANDS_SEEN_UUID = {PATH_COMMANDS, COMMAND_SEEN, "UUID"};
	public static final Object[] COMMANDS_SEEN_IP = {PATH_COMMANDS, COMMAND_SEEN, "IP"};
	public static final Object[] COMMANDS_SEEN_FIRST_PLAYED = {PATH_COMMANDS, COMMAND_SEEN, "FirstPlayed"};
	public static final Object[] COMMANDS_SEEN_WALKING_SPEED = {PATH_COMMANDS, COMMAND_SEEN, "WalkingSpeed"};
	public static final Object[] COMMANDS_SEEN_FLYING_SPEED = {PATH_COMMANDS, COMMAND_SEEN, "FlyingSpeed"};
	public static final Object[] COMMANDS_SEEN_CURRENT_LOCATION = {PATH_COMMANDS, COMMAND_SEEN, "CurrentLocation"};
	public static final Object[] COMMANDS_SEEN_CAN_FLY = {PATH_COMMANDS, COMMAND_SEEN, "CanFly"};
	public static final Object[] COMMANDS_SEEN_FLYING = {PATH_COMMANDS, COMMAND_SEEN, "IsFlying"};
	public static final Object[] COMMANDS_SEEN_GAMEMODE = {PATH_COMMANDS, COMMAND_SEEN, "Gamemode"};
	public static final Object[] COMMANDS_SEEN_VANISHED = {PATH_COMMANDS, COMMAND_SEEN, "Vanished"};
	public static final Object[] COMMANDS_SEEN_GODMODE = {PATH_COMMANDS, COMMAND_SEEN, "Godmode"};
	public static final Object[] COMMANDS_SEEN_AFK = {PATH_COMMANDS, COMMAND_SEEN, "AFK"};
	public static final Object[] COMMANDS_SEEN_BAN = {PATH_COMMANDS, COMMAND_SEEN, "BanData"};
	//public static final Object[] COMMANDS_SEEN_MUTE = {PATH_COMMANDS, COMMAND_SEEN, "Mute"};
	public static final Object[] COMMANDS_SEEN_YES = {PATH_COMMANDS, COMMAND_SEEN, "Yes"};
	public static final Object[] COMMANDS_SEEN_NO = {PATH_COMMANDS, COMMAND_SEEN, "No"};
	public static final Object[] COMMANDS_SEEN_PADDING = {PATH_COMMANDS, COMMAND_SEEN, "Padding"};
	public static final Object[] COMMANDS_HELP_TITLE = {PATH_COMMANDS, COMMAND_HELP, "Title"};
	public static final Object[] COMMANDS_HELP_LIST = {PATH_COMMANDS, COMMAND_HELP, "List"};
	public static final Object[] COMMANDS_GLOW_ENABLE = {PATH_COMMANDS, COMMAND_GLOW, "Enable"};
	public static final Object[] COMMANDS_GLOW_ENABLE_STAFF = {PATH_COMMANDS, COMMAND_GLOW, "EnableStaff"};
	public static final Object[] COMMANDS_GLOW_DISABLE = {PATH_COMMANDS, COMMAND_GLOW, "Disable"};
	public static final Object[] COMMANDS_GLOW_DISABLE_STAFF = {PATH_COMMANDS, COMMAND_GLOW, "DisableStaff"};
	public static final Object[] COMMANDS_FLAME_SUCCESS = {PATH_COMMANDS, COMMAND_FLAME, "Success"};
	public static final Object[] COMMANDS_FLAME_SUCCESS_DAMAGE = {PATH_COMMANDS, COMMAND_FLAME, "SuccessDamage"};
	public static final Object[] COMMANDS_FLAME_SUCCESS_STAFF = {PATH_COMMANDS, COMMAND_FLAME, "SuccessStaff"};
	public static final Object[] COMMANDS_EXTINGUISH_SUCCESS = {PATH_COMMANDS, COMMAND_EXTINGUISH, "Success"};
	public static final Object[] COMMANDS_EXTINGUISH_SUCCESS_STAFF = {PATH_COMMANDS, COMMAND_EXTINGUISH, "SuccessStaff"};

}
