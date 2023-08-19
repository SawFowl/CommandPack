package sawfowl.commandpack;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.math.NumberUtils;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.world.server.ServerWorld;

import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.localeapi.api.TextUtils;

public class Permissions {

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
	public static final String BACK = "commandpack.commands.user.back";
	public static final String CLEAR = "commandpack.commands.user.clear";
	public static final String REPAIR = "commandpack.commands.user.repair";
	public static final String REPAIR_SELECT = "commandpack.commands.user.selectrepair";
	public static final String ENDERCHEST = "commandpack.commands.user.enderchest";
	public static final String TOP = "commandpack.commands.user.top";
	public static final String JUMP = "commandpack.commands.user.jump";
	public static final String FLY = "commandpack.commands.user.fly";
	public static final String GODMODE = "commandpack.commands.user.godmode";
	public static final String INVENTORYSEE = "commandpack.commands.user.inventorysee";
	public static final String SPEED = "commandpack.commands.user.speed";
	public static final String SPEED_FLY = "commandpack.commands.user.flyspeed";
	public static final String DISPOSAL = "commandpack.commands.user.disposal";
	public static final String WEATHER = "commandpack.commands.user.weather";
	public static final String TIME = "commandpack.commands.user.time";
	public static final String CRAFTING_TABLE = "commandpack.commands.user.craftingtable";
	public static final String ANVIL = "commandpack.commands.user.anvil";
	public static final String ENCHANTING_TABLE = "commandpack.commands.user.enchantmenttable";
	public static final String BACKPACK = "commandpack.commands.user.backpack";
	public static final String FEED = "commandpack.commands.user.feed";
	public static final String HEAL = "commandpack.commands.user.heal";
	public static final String VANISH = "commandpack.commands.user.vanish";
	public static final String NICK = "commandpack.commands.user.nick";
	public static final String SERVER_STAT_INFO_TIME = "commandpack.commands.user.serverstat.time";
	public static final String KIT = "commandpack.commands.user.kit";
	public static final String AFK = "commandpack.commands.user.afk";
	public static final String PING = "commandpack.commands.user.ping";
	public static final String LIST = "commandpack.commands.user.list";
	public static final String SEEN = "commandpack.commands.user.seen";
	public static final String HELP = "commandpack.commands.user.help";
	public static final String GLOW = "commandpack.commands.user.glow";
	public static final String FLAME = "commandpack.commands.user.flame";
	public static final String EXTINGUISH = "commandpack.commands.user.extinguish";
	public static final String WARNS = "commandpack.commands.user.warnings.self";
	public static final String WARNS_OTHER = "commandpack.commands.user.warnings.other";
	public static final String BANLIST = "commandpack.commands.user.banlist";
	public static final String BANINFO = "commandpack.commands.user.baninfo";
	public static final String MUTEINFO = "commandpack.commands.user.muteinfo";
	public static final String MUTELIST = "commandpack.commands.user.mutelist";
	public static final String BALANCE = "commandpack.commands.user.balance.self";
	public static final String BALANCE_OTHER = "commandpack.commands.user.balance.other";
	public static final String BALANCE_HIDEN_VIEW = "commandpack.commands.user.balance.hiddenview";
	public static final String HIDE_BALANCE = "commandpack.commands.user.balance.hide";
	public static final String PAY = "commandpack.commands.user.balance.pay";
	public static final String TELL = "commandpack.commands.user.tell";
	public static final String REPLY = "commandpack.commands.user.reply";

	// Staff
	public static final String HAT_STAFF = "commandpack.commands.staff.hat";
	public static final String SET_SPAWN_STAFF = "commandpack.commands.staff.setspawn";
	public static final String SPAWN_STAFF = "commandpack.commands.staff.spawn";
	public static final String WARP_STAFF = "commandpack.commands.staff.warp";
	public static final String TELEPORT_STAFF = "commandpack.commands.staff.teleport";
	public static final String TELEPORT_HERE_STAFF = "commandpack.commands.staff.tphere.one";
	public static final String TELEPORT_HERE_ALL_STAFF = "commandpack.commands.staff.tphere.all";
	public static final String TPPOS_STAFF = "commandpack.commands.staff.tppos";
	public static final String RTP_STAFF = "commandpack.commands.staff.randomteleport";
	public static final String CLEAR_STAFF = "commandpack.commands.staff.clear";
	public static final String REPAIR_STAFF = "commandpack.commands.staff.repair";
	public static final String ENDERCHEST_STAFF = "commandpack.commands.staff.enderchest";
	public static final String FLY_STAFF = "commandpack.commands.staff.fly";
	public static final String GODMODE_STAFF = "commandpack.commands.staff.godmode";
	public static final String INVENTORYSEE_STAFF = "commandpack.commands.staff.inventorysee";
	public static final String SPEED_STAFF = "commandpack.commands.staff.speed";
	public static final String GAMEMODE_STAFF = "commandpack.commands.staff.gamemode";
	public static final String GAMEMODE_OTHER_STAFF = "commandpack.commands.staff.othergamemode";
	public static final String WEATHER_STAFF = "commandpack.commands.staff.weather";
	public static final String TIME_STAFF = "commandpack.commands.staff.time";
	public static final String CRAFTING_TABLE_STAFF = "commandpack.commands.staff.craftingtable";
	public static final String ANVIL_STAFF = "commandpack.commands.staff.anvil";
	public static final String ENCHANTMENT_TABLE_STAFF = "commandpack.commands.staff.enchantmenttable";
	public static final String ENCHANT_STAFF = "commandpack.commands.staff.enchant";
	public static final String BACKPACK_STAFF = "commandpack.commands.staff.backpack";
	public static final String FEED_STAFF = "commandpack.commands.staff.feed";
	public static final String HEAL_STAFF = "commandpack.commands.staff.heal";
	public static final String BROADCAST_STAFF = "commandpack.commands.staff.broadcast";
	public static final String SUDO_STAFF = "commandpack.commands.staff.sudo";
	public static final String VANISH_STAFF = "commandpack.commands.staff.vanish";
	public static final String NICK_STAFF = "commandpack.commands.staff.nick";
	public static final String ITEMNAME_STAFF = "commandpack.commands.staff.itemname";
	public static final String ITEMLORE_STAFF = "commandpack.commands.staff.itemlore";
	public static final String SERVER_STAT_BASIC = "commandpack.commands.staff.serverstat.basic";
	public static final String SERVER_STAT_STAFF_INFO_SYSTEM = "commandpack.commands.staff.serverstat.systeminfo";
	public static final String SERVER_STAT_STAFF_INFO_WORLDS = "commandpack.commands.staff.serverstat.worldsinfo";
	public static final String SERVER_STAT_STAFF_PLUGINS_LIST = "commandpack.commands.staff.serverstat.plugins.list";
	public static final String SERVER_STAT_STAFF_PLUGINS_INFO = "commandpack.commands.staff.serverstat.plugins.info";
	public static final String SERVER_STAT_STAFF_INFO_PLUGINS_REFRESH = "commandpack.commands.staff.serverstat.plugins.refresh";
	public static final String SERVER_STAT_STAFF_MODS_LIST = "commandpack.commands.staff.serverstat.mods.list";
	public static final String SERVER_STAT_STAFF_MODS_INFO = "commandpack.commands.staff.serverstat.mods.info";
	public static final String SERVER_STAT_STAFF_INFO_TPS = "commandpack.commands.staff.serverstat.tps";
	public static final String KIT_STAFF = "commandpack.commands.staff.kit";
	public static final String WORLD_STAFF = "commandpack.commands.staff.world";
	public static final String COMMANDSPY_STAFF = "commandpack.commands.staff.commandspy";
	public static final String PING_STAFF = "commandpack.commands.staff.ping";
	public static final String LIST_STAFF = "commandpack.commands.staff.list";
	public static final String SEEN_STAFF = "commandpack.commands.staff.seen";
	public static final String GLOW_STAFF = "commandpack.commands.staff.glow";
	public static final String FLAME_STAFF = "commandpack.commands.staff.flame";
	public static final String EXTINGUISH_STAFF = "commandpack.commands.staff.extinguish";
	public static final String BAN_STAFF = "commandpack.commands.staff.ban";
	public static final String BANIP_STAFF = "commandpack.commands.staff.banip";
	public static final String UNBAN_STAFF = "commandpack.commands.staff.unban";
	public static final String UNBANIP_STAFF = "commandpack.commands.staff.unbanip";
	public static final String KICK_STAFF = "commandpack.commands.staff.kick";
	public static final String MUTE_STAFF = "commandpack.commands.staff.mute";
	public static final String UNMUTE_STAFF = "commandpack.commands.staff.unmute";
	public static final String WARN_STAFF = "commandpack.commands.staff.warn";
	public static final String WARNS_STAFF = "commandpack.commands.staff.warnings";
	public static final String ECONOMY_STAFF = "commandpack.commands.staff.economy";

	public static final String IGNORE_DELAY_TIMER = "commandpack.commands.ignore.delay.timer";
	public static final String IGNORE_COOLDOWN = "commandpack.commands.ignore.cooldown";
	public static final String IGNORE_DELAY_MOVING = "commandpack.commands.ignore.delay.moving";
	public static final String IGNORE_DELAY_OTHER_COMMAND = "commandpack.commands.ignore.delay.othercommand";
	public static final String IGNORE_PRICE = "commandpack.commands.ignore.price";
	public static final String IGNORE_BAN = "commandpack.commands.ignore.ban";
	public static final String IGNORE_BANIP = "commandpack.commands.ignore.banip";
	public static final String IGNORE_KICK = "commandpack.commands.ignore.kick";
	public static final String IGNORE_MUTE = "commandpack.commands.ignore.mute";
	public static final String IGNORE_WARN = "commandpack.commands.ignore.ban";

	// Limits
	private static final String HOME_LIMIT = "commandpack.limits.home";
	private static final String WARP_LIMIT = "commandpack.limits.warp";
	private static final String SPEED_LIMIT = "commandpack.limits.speed";
	private static final String SPEED_FLY_LIMIT = "commandpack.limits.flyspeed";
	private static final String ENCHANTMENT_TABLE_LIMIT = "commandpack.limits.enchantmenttable";
	private static final String BACKPACK_LIMIT = "commandpack.limits.backpack";
	private static final String KEEP_INVENTORY = "commandpack.keep.inventory";
	private static final String KEEP_EXP = "commandpack.keep.exp";
	public  static final String AFK_UNLIMIT = "commandpack.limits.unlimitafk";

	private static final String WARP_ACCESS = "commandpack.access.warps";
	private static final String RTP_WORLD_ARG_ACCESS = "commandpack.access.worlds.randomteleport";
	private static final String GAMEMODE_ACCESS = "commandpack.access.gamemode";
	private static final String WEATHER_ACCESS = "commandpack.access.weather";
	private static final String WEATHER_WORLD_ACCESS = "commandpack.access.worlds.weather";
	private static final String TIME_ACCESS = "commandpack.access.time";
	private static final String TIME_WORLD_ACCESS = "commandpack.access.worlds.time";
	private static final String KIT_ACCESS = "commandpack.access.kit";
	public  static final String MOTD_ACCESS = "commandpack.access.motd";
	public  static final String ALL_MODS_ACCESS = "commandpack.access.allmods";
	public  static final String PERMANENT_WARN_ACCESS = "commandpack.access.permanent.warn";
	public  static final String PERMANENT_MUTE_ACCESS = "commandpack.access.permanent.mute";
	public  static final String PERMANENT_BAN_ACCESS = "commandpack.access.permanent.ban";
	private  static final String CURRENCY_ACCESS = "commandpack.access.currency.";

	public static String getIgnoreDelayTimer(String command) {
		return IGNORE_DELAY_TIMER + "." + command;
	}

	public static String getIgnoreCooldown(String command) {
		return IGNORE_COOLDOWN + "." + command;
	}

	public static String getIgnoreDelayMoving(String command) {
		return IGNORE_DELAY_MOVING + "." + command;
	}

	public static String getIgnorePrice(String command) {
		return IGNORE_PRICE + "." + command;
	}

	public static String getWarpPermission(String warpName) {
		return WARP_ACCESS + "." + TextUtils.clearDecorations(warpName);
	}

	public static String getRtpWorldAcess(ResourceKey worldKey) {
		return RTP_WORLD_ARG_ACCESS + "." + worldKey.asString();
	}

	public static String getGameModePermission(String gamemode) {
		return GAMEMODE_ACCESS + "." + TextUtils.clearDecorations(gamemode);
	}

	public static String getWeatherPermission(String weather) {
		return WEATHER_ACCESS + "." + TextUtils.clearDecorations(weather);
	}

	public static String getWeatherWorldPermission(String weather, ServerWorld world) {
		return WEATHER_WORLD_ACCESS + "." + TextUtils.clearDecorations(weather) + "." + TextUtils.clearDecorations(world.key().asString().replace(":", "_"));
	}

	public static String getTimePermission(String time) {
		return TIME_ACCESS + "." + TextUtils.clearDecorations(TextUtils.deserialize(time));
	}

	public static String getTimeWorldPermission(String time, ServerWorld world) {
		return TIME_WORLD_ACCESS + "." + TextUtils.clearDecorations(world.key().asString().replace(":", "_")) + "." + TextUtils.clearDecorations(time);
	}

	public static String getKitPermission(Kit kit) {
		return KIT_ACCESS + "." + TextUtils.clearDecorations(kit.id());
	}

	public static String getCurrencyAccess(Currency currency) {
		return CURRENCY_ACCESS + TextUtils.clearDecorations(currency.displayName());
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

	public static int getEnchantmentTableLimit(ServerPlayer player) {
		return !player.option(ENCHANTMENT_TABLE_LIMIT).isPresent() ? 2 : toInt(player.option(ENCHANTMENT_TABLE_LIMIT).get(), 2);
	}

	public static int getBackpackLimit(ServerPlayer player) {
		return !player.option(BACKPACK_LIMIT).isPresent() ? 1 : toInt(player.option(BACKPACK_LIMIT).get(), 1);
	}

	public static double getKeepInventoryLimit(ServerPlayer player) {
		return player.option(KEEP_INVENTORY).map(o -> toDouble(o, 0)).orElse(0d);
	}

	public static double getKeepExpLimit(ServerPlayer player) {
		return player.option(KEEP_EXP).map(o -> toDouble(o, 0)).orElse(0d);
	}

	private static int toInt(String option, int def) {
		return NumberUtils.isParsable(option) ? NumberUtils.toInt(option) : def;
	}

	private static Double toDouble(String option, double def) {
		return NumberUtils.isParsable(option) ? BigDecimal.valueOf(NumberUtils.toDouble(option)).setScale(2, RoundingMode.HALF_UP).doubleValue() : def;
	}

}
