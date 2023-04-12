package sawfowl.commandpack;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.math.NumberUtils;
import org.spongepowered.api.ResourceKey;
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
	public static final String TPTOGGLE = "commandpack.commands.user.tptoggle";
	public static final String TPA = "commandpack.commands.user.tpa";
	public static final String TPA_HERE = "commandpack.commands.user.tpahere";
	public static final String TPA_HERE_ALL = "commandpack.commands.user.tpahereall";
	public static final String RTP = "commandpack.commands.user.randomteleport";
	public static final String CLEAR = "commandpack.commands.user.clear";
	public static final String REPAIR = "commandpack.commands.user.repair";
	public static final String REPAIR_SELECT = "commandpack.commands.user.selectrepair";
	public static final String ENDERCHEST = "commandpack.commands.user.enderchest";
	public static final String TOP = "commandpack.commands.user.top";
	public static final String JUMP = "commandpack.commands.user.jump";
	public static final String FLY = "commandpack.commands.user.fly";
	public static final String GODMODE = "commandpack.commands.user.godmode";
	public static final String SPEED = "commandpack.commands.user.speed";
	public static final String SPEED_FLY = "commandpack.commands.user.flyspeed";
	public static final String DISPOSAL = "commandpack.commands.user.disposal";

	// Staff
	public static final String HAT_STAFF = "commandpack.commands.staff.hat";
	public static final String SET_SPAWN_STAFF = "commandpack.commands.staff.setspawn";
	public static final String SPAWN_STAFF = "commandpack.commands.staff.spawn";
	public static final String WARP_STAFF = "commandpack.commands.staff.warp";
	public static final String TELEPORT_STAFF = "commandpack.commands.staff.teleport";
	public static final String TELEPORT_HERE_STAFF = "commandpack.commands.staff.teleport";
	public static final String TELEPORT_HERE_ALL_STAFF = "commandpack.commands.staff.teleport";
	public static final String TPPOS_STAFF = "commandpack.commands.staff.tppos";
	public static final String RTP_STAFF = "commandpack.commands.staff.randomteleport";
	public static final String CLEAR_STAFF = "commandpack.commands.staff.clear";
	public static final String REPAIR_STAFF = "commandpack.commands.staff.repair";
	public static final String ENDERCHEST_STAFF = "commandpack.commands.staff.enderchest";
	public static final String FLY_STAFF = "commandpack.commands.staff.fly";
	public static final String GODMODE_STAFF = "commandpack.commands.staff.godmode";
	public static final String INVENTORYSEE_STAFF = "commandpack.commands.staff.inventorysee";
	public static final String SPEED_STAFF = "commandpack.commands.staff.speed";

	public static final String IGNORE_DELAY_TIMER = "commandpack.commands.ignore.delay.timer";
	public static final String IGNORE_DELAY_MOVING = "commandpack.commands.ignore.delay.moving";
	public static final String IGNORE_DELAY_OTHER_COMMAND = "commandpack.commands.ignore.delay.othercommand";
	public static final String IGNORE_PRICE = "commandpack.commands.ignore.price";
	public static final String IGNORE_HIDE_COMMAND_SOURCE = "commandpack.commands.ignore.hidecommandsource";

	// Limits
	private static final String HOME_LIMIT = "commandpack.limits.home";
	private static final String WARP_LIMIT = "commandpack.limits.warp";
	private static final String SPEED_LIMIT = "commandpack.limits.speed";
	private static final String SPEED_FLY_LIMIT = "commandpack.limits.speed";

	private static final String WARP_ACCESS = "commandpack.warps";
	private static final String RTP_WORLD_ARG_ACCESS = "commandpack.randomteleport.world";

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
		return !player.option(HOME_LIMIT).isPresent() ? 0 : toInt(player.option(HOME_LIMIT).get(), 0);
	}

	public static int getWarpsLimit(ServerPlayer player) {
		return !player.option(WARP_LIMIT).isPresent() ? 0 : toInt(player.option(WARP_LIMIT).get(), 0);
	}

	public static double getSpeedLimit(ServerPlayer player) {
		return !player.option(SPEED_LIMIT).isPresent() ? 1 : toDouble(player.option(SPEED_LIMIT).get(), 1);
	}

	public static double getSpeedFlyLimit(ServerPlayer player) {
		return !player.option(SPEED_FLY_LIMIT).isPresent() ? 1 : toDouble(player.option(SPEED_FLY_LIMIT).get(), 1);
	}

	public static String getRtpWorldAcess(ResourceKey worldKey) {
		return RTP_WORLD_ARG_ACCESS + "." + worldKey.asString();
	}

	private static int toInt(String option, int def) {
		return NumberUtils.isParsable(option) ? NumberUtils.toInt(option) : def;
	}

	private static Double toDouble(String option, double def) {
		return NumberUtils.isParsable(option) ? BigDecimal.valueOf(NumberUtils.toDouble(option)).setScale(2, RoundingMode.HALF_UP).doubleValue() : def;
	}

}
