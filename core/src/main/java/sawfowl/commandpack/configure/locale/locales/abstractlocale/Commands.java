package sawfowl.commandpack.configure.locale.locales.abstractlocale;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Afk;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Anvil;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Back;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Balance;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.BalanceTop;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Ban;
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
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.TpPos;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.TpToggle;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Tpa;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Time;
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

@ConfigSerializable
public interface Commands {

	Afk getAfk();

	Anvil getAnvil();

	Back getBack();

	Balance getBalance();

	BalanceTop getBalanceTop();

	Ban getBan();

	Banlist getBanlist();

	Broadcast getBroadcast();

	ClearInventory getClearInventory();

	CommandSpy getCommandSpy();

	CraftingTable getCraftingTable();

	Disposal getDisposal();

	Economy getEconomy();

	Enchant getEnchant();

	EnchantmentTable getEnchantmentTable();

	Extinguish getExtinguish();

	Feed getFeed();

	Flame getFlame();

	Fly getFly();

	GameMode getGameMode();

	Glow getGlow();

	GodMode getGodMode();

	Hat getHat();

	Heal getHeal();

	Help getHelp();

	HideBalance getHideBalance();

	Home getHome();

	InventorySee getInventorySee();

	Item getItem();

	Jump getJump();

	Kick getKick();

	Kit getKit();

	Kits getKits();

	List getList();

	Mute getMute();

	MuteInfo getMuteInfo();

	MuteList getMuteList();

	Nick getNick();

	Pay getPay();

	Ping getPing();

	RandomTeleport getRandomTeleport();

	Repair getRepair();

	Reply getReply();

	Seen getSeen();

	ServerStat getServerStat();

	SetHome getSetHome();

	SetWarp getSetWarp();

	Spawn getSpawn();

	Speed getSpeed();

	Sudo getSudo();

	Tell getTell();

	Time getTime();

	Tpa getTpa();

	TpPos getTpPos();

	TpToggle getTpToggle();

	Unban getUnban();

	UnbanIP getUnbanIP();

	Unmute getUnmute();

	Vanish getVanish();

	Warn getWarn();

	Warns getWarns();

	Warp getWarp();

	Warps getWarps();

	Weather getWeather();

	World getWorld();

}
