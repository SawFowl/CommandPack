package sawfowl.commandpack;

import org.apache.commons.lang3.math.NumberUtils;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class Permissions {

	/**
	 * Added to speed up the creation of commands.
	 * Must be deleted after the job is done.
	 */
	@Deprecated
	public static Permissions instance;

	// User
	public static final String HAT = "commandpack.commands.user.hat";
	public static final String SUICIDE = "commandpack.commands.user.suicide";
	public static final String SPAWN = "commandpack.commands.user.spawn";
	public static final String HOME = "commandpack.commands.user.home";

	// Staff
	public static final String IGNORE_DELAY_TIMER = "commandpack.commands.staff.ignore.delay.timer";
	public static final String IGNORE_DELAY_MOVING = "commandpack.commands.staff.ignore.delay.moving";
	public static final String IGNORE_DELAY_OTHER_COMMAND = "commandpack.commands.staff.ignore.delay.othercommand";
	public static final String IGNORE_PRICE = "commandpack.commands.staff.ignore.price";
	public static final String IGNORE_HIDE_COMMAND_SOURCE = "commandpack.commands.staff.ignore.hidecommandsource";

	public static final String HAT_STAFF = "commandpack.commands.staff.hat";
	public static final String SET_SPAWN_STAFF = "commandpack.commands.staff.setspawn";
	public static final String SPAWN_STAFF = "commandpack.commands.staff.spawn";

	// Limits
	private static final String HOME_LIMIT = "commandpack.limits.home";

	public static String getIgnoreDelayTimer(String command) {
		return IGNORE_DELAY_TIMER + "." + command;
	}

	public static String getIgnoreDelayMoving(String command) {
		return IGNORE_DELAY_MOVING + "." + command;
	}

	public static String getIgnorePrice(String command) {
		return IGNORE_PRICE + "." + command;
	}

	public static int getHomeLimit(ServerPlayer player) {
		return !player.option(HOME_LIMIT).isPresent() ? 0 : toInt(player.option(HOME_LIMIT).get());
	}

	private static int toInt(String option) {
		return NumberUtils.isParsable(option) ? NumberUtils.toInt(option) : 0;
	}

}
