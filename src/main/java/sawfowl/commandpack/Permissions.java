package sawfowl.commandpack;

import org.apache.commons.lang3.math.NumberUtils;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.localeapi.api.TextUtils;

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
	public static final String SET_WARP = "commandpack.commands.user.setwarp";
	public static final String WARP = "commandpack.commands.user.warp";
	public static final String WARPS = "commandpack.commands.user.warps";

	// Staff
	public static final String HAT_STAFF = "commandpack.commands.staff.hat";
	public static final String SET_SPAWN_STAFF = "commandpack.commands.staff.setspawn";
	public static final String SPAWN_STAFF = "commandpack.commands.staff.spawn";
	public static final String WARP_STAFF = "commandpack.commands.staff.warp";

	public static final String IGNORE_DELAY_TIMER = "commandpack.commands.ignore.delay.timer";
	public static final String IGNORE_DELAY_MOVING = "commandpack.commands.ignore.delay.moving";
	public static final String IGNORE_DELAY_OTHER_COMMAND = "commandpack.commands.ignore.delay.othercommand";
	public static final String IGNORE_PRICE = "commandpack.commands.ignore.price";
	public static final String IGNORE_HIDE_COMMAND_SOURCE = "commandpack.commands.ignore.hidecommandsource";

	// Limits
	private static final String HOME_LIMIT = "commandpack.limits.home";
	private static final String WARP_LIMIT = "commandpack.limits.warp";

	private static final String WARP_ACCESS = "commandpack.warps";

	public static String getIgnoreDelayTimer(String command) {
		return IGNORE_DELAY_TIMER + "." + command;
	}

	public static String getIgnoreDelayMoving(String command) {
		return IGNORE_DELAY_MOVING + "." + command;
	}

	public static String getIgnorePrice(String command) {
		return IGNORE_PRICE + "." + command;
	}

	public static String getWarpPermission(String warpName) {
		return WARP_ACCESS + "." + TextUtils.clearDecorations(TextUtils.deserialize(warpName));
	}

	public static int getHomeLimit(ServerPlayer player) {
		return !player.option(HOME_LIMIT).isPresent() ? 0 : toInt(player.option(HOME_LIMIT).get());
	}

	public static int getWarpsLimit(ServerPlayer player) {
		return !player.option(WARP_LIMIT).isPresent() ? 0 : toInt(player.option(WARP_LIMIT).get());
	}

	private static int toInt(String option) {
		return NumberUtils.isParsable(option) ? NumberUtils.toInt(option) : 0;
	}

}
