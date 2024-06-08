package sawfowl.commandpack.configure.locale.locales.ru;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.Commands;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Afk;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Anvil;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Back;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Balance;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.BalanceTop;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Ban;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.BanIP;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.BanInfo;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Banlist;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Broadcast;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.ClearInventory;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.CommandSpy;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.CraftingTable;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Disposal;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Economy;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Enchant;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.EnchantmentTable;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Extinguish;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Feed;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Flame;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Fly;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.GameMode;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Glow;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.GodMode;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Hat;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Heal;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Help;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.HideBalance;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Home;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.InventorySee;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Item;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Jump;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Kick;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Kit;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Kits;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.List;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Mute;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.MuteInfo;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.MuteList;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Nick;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Pay;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Ping;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.RandomTeleport;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Repair;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Reply;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Seen;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.ServerStat;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.SetHome;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.SetWarp;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Spawn;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Speed;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Sudo;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Tell;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Time;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.TpPos;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.TpToggle;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Tpa;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Unban;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.UnbanIP;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Unmute;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Vanish;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Warn;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Warns;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Warp;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Warps;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Weather;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.World;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementAfk;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementAnvil;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementBack;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementBalance;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementBalanceTop;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementBan;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementBanIP;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementBanInfo;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementBanlist;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementBroadcast;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementClearInventory;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementCommandSpy;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementCraftingTable;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementDisposal;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementEconomy;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementEnchant;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementEnchantmentTable;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementExtinguish;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementFeed;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementFlame;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementFly;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementGameMode;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementGlow;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementGodMode;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementHat;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementHeal;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementHelp;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementHideBalance;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementHome;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementInventorySee;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementItem;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementJump;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementKick;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementKit;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementKits;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementList;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementMute;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementMuteInfo;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementMuteList;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementNick;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementPay;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementPing;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementRandomTeleport;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementRepair;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementReply;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementSeen;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementServerStat;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementSetHome;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementSetWarp;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementSpawn;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementSpeed;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementSudo;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementTell;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementTime;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementTpPos;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementTpToggle;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementTpa;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementUnban;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementUnbanIP;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementUnmute;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementVanish;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementWarn;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementWarns;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementWarp;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementWarps;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementWeather;
import sawfowl.commandpack.configure.locale.locales.ru.commands.ImplementWorld;

@ConfigSerializable
public class ImplementCommands implements Commands {

	public ImplementCommands() {}

	@Setting("Afk")
	private ImplementAfk afk = new ImplementAfk();
	@Setting("Anvil")
	private ImplementAnvil anvil = new ImplementAnvil();
	@Setting("Back")
	private ImplementBack back = new ImplementBack();
	@Setting("Balance")
	private ImplementBalance balance = new ImplementBalance();
	@Setting("BalanceTop")
	private ImplementBalanceTop balanceTop = new ImplementBalanceTop();
	@Setting("Ban")
	private ImplementBan ban = new ImplementBan();
	@Setting("BanInfo")
	private ImplementBanInfo banInfo = new ImplementBanInfo();
	@Setting("BanIP")
	private ImplementBanIP banIP = new ImplementBanIP();
	@Setting("Banlist")
	private ImplementBanlist banlist = new ImplementBanlist();
	@Setting("Broadcast")
	private ImplementBroadcast broadcast = new ImplementBroadcast();
	@Setting("ClearInventory")
	private ImplementClearInventory clearInventory = new ImplementClearInventory();
	@Setting("CommandSpy")
	private ImplementCommandSpy commandSpy = new ImplementCommandSpy();
	@Setting("CraftingTable")
	private ImplementCraftingTable craftingTable = new ImplementCraftingTable();
	@Setting("Disposal")
	private ImplementDisposal disposal = new ImplementDisposal();
	@Setting("Economy")
	private ImplementEconomy economy = new ImplementEconomy();
	@Setting("Enchant")
	private ImplementEnchant enchant = new ImplementEnchant();
	@Setting("EnchantmentTable")
	private ImplementEnchantmentTable enchantmentTable = new ImplementEnchantmentTable();
	@Setting("Extinguish")
	private ImplementExtinguish extinguish = new ImplementExtinguish();
	@Setting("Feed")
	private ImplementFeed feed = new ImplementFeed();
	@Setting("Flame")
	private ImplementFlame flame = new ImplementFlame();
	@Setting("Fly")
	private ImplementFly fly = new ImplementFly();
	@Setting("GameMode")
	private ImplementGameMode gameMode = new ImplementGameMode();
	@Setting("Glow")
	private ImplementGlow glow = new ImplementGlow();
	@Setting("GodMode")
	private ImplementGodMode godMode = new ImplementGodMode();
	@Setting("Hat")
	private ImplementHat hat = new ImplementHat();
	@Setting("Heal")
	private ImplementHeal heal = new ImplementHeal();
	@Setting("Help")
	private ImplementHelp help = new ImplementHelp();
	@Setting("HideBalance")
	private ImplementHideBalance hideBalance = new ImplementHideBalance();
	@Setting("Home")
	private ImplementHome home = new ImplementHome();
	@Setting("InventorySee")
	private ImplementInventorySee inventorySee = new ImplementInventorySee();
	@Setting("Item")
	private ImplementItem item = new ImplementItem();
	@Setting("Jump")
	private ImplementJump jump = new ImplementJump();
	@Setting("Kick")
	private ImplementKick kick = new ImplementKick();
	@Setting("Kit")
	private ImplementKit kit = new ImplementKit();
	@Setting("Kits")
	private ImplementKits kits = new ImplementKits();
	@Setting("List")
	private ImplementList list = new ImplementList();
	@Setting("Mute")
	private ImplementMute mute = new ImplementMute();
	@Setting("MuteInfo")
	private ImplementMuteInfo muteInfo = new ImplementMuteInfo();
	@Setting("MuteList")
	private ImplementMuteList muteList = new ImplementMuteList();
	@Setting("Nick")
	private ImplementNick nick = new ImplementNick();
	@Setting("Pay")
	private ImplementPay pay = new ImplementPay();
	@Setting("Ping")
	private ImplementPing ping = new ImplementPing();
	@Setting("RandomTeleport")
	private ImplementRandomTeleport randomTeleport = new ImplementRandomTeleport();
	@Setting("Repair")
	private ImplementRepair repair = new ImplementRepair();
	@Setting("Reply")
	private ImplementReply reply = new ImplementReply();
	@Setting("Seen")
	private ImplementSeen seen = new ImplementSeen();
	@Setting("ServerStat")
	private ImplementServerStat serverStat = new ImplementServerStat();
	@Setting("SetHome")
	private ImplementSetHome setHome = new ImplementSetHome();
	@Setting("SetWarp")
	private ImplementSetWarp setWarp = new ImplementSetWarp();
	@Setting("Spawn")
	private ImplementSpawn spawn = new ImplementSpawn();
	@Setting("Speed")
	private ImplementSpeed speed = new ImplementSpeed();
	@Setting("Sudo")
	private ImplementSudo sudo = new ImplementSudo();
	@Setting("Tell")
	private ImplementTell tell = new ImplementTell();
	@Setting("Time")
	private ImplementTime time = new ImplementTime();
	@Setting("Tpa")
	private ImplementTpa tpa = new ImplementTpa();
	@Setting("TpPos")
	private ImplementTpPos tpPos = new ImplementTpPos();
	@Setting("TpToggle")
	private ImplementTpToggle tpToggle = new ImplementTpToggle();
	@Setting("Unban")
	private ImplementUnban unban = new ImplementUnban();
	@Setting("UnbanIP")
	private ImplementUnbanIP unbanIP = new ImplementUnbanIP();
	@Setting("Unmute")
	private ImplementUnmute unmute = new ImplementUnmute();
	@Setting("Vanish")
	private ImplementVanish vanish = new ImplementVanish();
	@Setting("Warn")
	private ImplementWarn warn = new ImplementWarn();
	@Setting("Warns")
	private ImplementWarns warns = new ImplementWarns();
	@Setting("Warp")
	private ImplementWarp warp = new ImplementWarp();
	@Setting("Warps")
	private ImplementWarps warps = new ImplementWarps();
	@Setting("Weather")
	private ImplementWeather weather = new ImplementWeather();
	@Setting("World")
	private ImplementWorld world = new ImplementWorld();

	@Override
	public Afk getAfk() {
		return afk;
	}

	@Override
	public Anvil getAnvil() {
		return anvil;
	}

	@Override
	public Back getBack() {
		return back;
	}

	@Override
	public Balance getBalance() {
		return balance;
	}

	@Override
	public BalanceTop getBalanceTop() {
		return balanceTop;
	}

	@Override
	public Ban getBan() {
		return ban;
	}

	@Override
	public BanInfo getBanInfo() {
		return banInfo;
	}

	@Override
	public BanIP getBanIP() {
		return banIP;
	}

	@Override
	public Banlist getBanlist() {
		return banlist;
	}

	@Override
	public Broadcast getBroadcast() {
		return broadcast;
	}

	@Override
	public ClearInventory getClearInventory() {
		return clearInventory;
	}

	@Override
	public CommandSpy getCommandSpy() {
		return commandSpy;
	}

	@Override
	public CraftingTable getCraftingTable() {
		return craftingTable;
	}

	@Override
	public Disposal getDisposal() {
		return disposal;
	}

	@Override
	public Economy getEconomy() {
		return economy;
	}

	@Override
	public Enchant getEnchant() {
		return enchant;
	}

	@Override
	public EnchantmentTable getEnchantmentTable() {
		return enchantmentTable;
	}

	@Override
	public Extinguish getExtinguish() {
		return extinguish;
	}

	@Override
	public Feed getFeed() {
		return feed;
	}

	@Override
	public Flame getFlame() {
		return flame;
	}

	@Override
	public Fly getFly() {
		return fly;
	}

	@Override
	public GameMode getGameMode() {
		return gameMode;
	}

	@Override
	public Glow getGlow() {
		return glow;
	}

	@Override
	public GodMode getGodMode() {
		return godMode;
	}

	@Override
	public Hat getHat() {
		return hat;
	}

	@Override
	public Heal getHeal() {
		return heal;
	}

	@Override
	public Help getHelp() {
		return help;
	}

	@Override
	public HideBalance getHideBalance() {
		return hideBalance;
	}

	@Override
	public Home getHome() {
		return home;
	}

	@Override
	public InventorySee getInventorySee() {
		return inventorySee;
	}

	@Override
	public Item getItem() {
		return item;
	}

	@Override
	public Jump getJump() {
		return jump;
	}

	@Override
	public Kick getKick() {
		return kick;
	}

	@Override
	public Kit getKit() {
		return kit;
	}

	@Override
	public Kits getKits() {
		return kits;
	}

	@Override
	public List getList() {
		return list;
	}

	@Override
	public Mute getMute() {
		return mute;
	}

	@Override
	public MuteInfo getMuteInfo() {
		return muteInfo;
	}

	@Override
	public MuteList getMuteList() {
		return muteList;
	}

	@Override
	public Nick getNick() {
		return nick;
	}

	@Override
	public Pay getPay() {
		return pay;
	}

	@Override
	public Ping getPing() {
		return ping;
	}

	@Override
	public RandomTeleport getRandomTeleport() {
		return randomTeleport;
	}

	@Override
	public Repair getRepair() {
		return repair;
	}

	@Override
	public Reply getReply() {
		return reply;
	}

	@Override
	public Seen getSeen() {
		return seen;
	}

	@Override
	public ServerStat getServerStat() {
		return serverStat;
	}

	@Override
	public SetHome getSetHome() {
		return setHome;
	}

	@Override
	public SetWarp getSetWarp() {
		return setWarp;
	}

	@Override
	public Spawn getSpawn() {
		return spawn;
	}

	@Override
	public Speed getSpeed() {
		return speed;
	}

	@Override
	public Sudo getSudo() {
		return sudo;
	}

	@Override
	public Tell getTell() {
		return tell;
	}

	@Override
	public Time getTime() {
		return time;
	}

	@Override
	public Tpa getTpa() {
		return tpa;
	}

	@Override
	public TpPos getTpPos() {
		return tpPos;
	}

	@Override
	public TpToggle getTpToggle() {
		return tpToggle;
	}

	@Override
	public Unban getUnban() {
		return unban;
	}

	@Override
	public UnbanIP getUnbanIP() {
		return unbanIP;
	}

	@Override
	public Unmute getUnmute() {
		return unmute;
	}

	@Override
	public Vanish getVanish() {
		return vanish;
	}

	@Override
	public Warn getWarn() {
		return warn;
	}

	@Override
	public Warns getWarns() {
		return warns;
	}

	@Override
	public Warp getWarp() {
		return warp;
	}

	@Override
	public Warps getWarps() {
		return warps;
	}

	@Override
	public Weather getWeather() {
		return weather;
	}

	@Override
	public World getWorld() {
		return world;
	}

}
