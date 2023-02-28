package sawfowl.commandpack.configure;

public class LocalesPaths {

	private static final String PATH_ECONOMY = "Economy";

	private static final String PATH_COMMANDS = "Commands";

	//private static final String PATH_EVENTS = "Events";

	//private static final String PATH_HOVER = "HoverText";

	private static final Object PATH_EXCEPTIONS = "Exceptions";

	public static final Object[] ECONOMY_NOT_FOUND = {PATH_ECONOMY, PATH_EXCEPTIONS, "EconomyNotFound"};
	public static final Object[] ECONOMY_ERROR_GIVE_MONEY = {PATH_ECONOMY, PATH_EXCEPTIONS, "ErrorGiveMoney"};
	public static final Object[] ECONOMY_ERROR_TAKE_MONEY = {PATH_ECONOMY, PATH_EXCEPTIONS, "ErrorTakeMoney"};

	public static final Object[] COMMANDS_ONLY_PLAYER = {PATH_COMMANDS, PATH_EXCEPTIONS, "OnlyPlayer"};
	public static final Object[] COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT = {PATH_COMMANDS, PATH_EXCEPTIONS, "PlayerNotPresent"};

}
