package sawfowl.commandpack.configure.locale.locales.def.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.ServerStat;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.serverstat.AboutMod;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.serverstat.AboutPlugin;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.serverstat.Buttons;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.serverstat.Memory;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.serverstat.Worlds;
import sawfowl.commandpack.configure.locale.locales.def.commands.serverstat.ImplementAboutMod;
import sawfowl.commandpack.configure.locale.locales.def.commands.serverstat.ImplementAboutPlugin;
import sawfowl.commandpack.configure.locale.locales.def.commands.serverstat.ImplementButtons;
import sawfowl.commandpack.configure.locale.locales.def.commands.serverstat.ImplementMemory;
import sawfowl.commandpack.configure.locale.locales.def.commands.serverstat.ImplementWorlds;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementServerStat implements ServerStat {

	public ImplementServerStat() {}

	@Setting("Buttons")
	private ImplementButtons buttons = new ImplementButtons();
	@Setting("Memory")
	private ImplementMemory memory = new ImplementMemory();
	@Setting("Worlds")
	private ImplementWorlds worlds = new ImplementWorlds();
	@Setting("AboutPlugin")
	private ImplementAboutPlugin aboutPlugin = new ImplementAboutPlugin();
	@Setting("AboutMod")
	private ImplementAboutMod aboutMod = new ImplementAboutMod();
	@Setting("Title")
	private Component title = TextUtils.deserializeLegacy("&3&lServer info");
	@Setting("TPS")
	private Component tps = TextUtils.deserializeLegacy("&aTPS(Current, 1m, 5m, 10m)&f: " + Placeholders.VALUE);
	@Setting("Uptime")
	private Component uptime = TextUtils.deserializeLegacy("&aUptime / JVM&f: &e" + Placeholders.VALUE);
	@Setting("ServerTime")
	private Component serverTime = TextUtils.deserializeLegacy("&aServer time&f: &e" + Placeholders.VALUE);
	@Setting("SystemInfo")
	private Component systemInfo = TextUtils.deserializeLegacy("&3&lSystem information");
	@Setting("System")
	private Component system = TextUtils.deserializeLegacy("&aSystem&f: &e" + Placeholders.VALUE);
	@Setting("Java")
	private Component java = TextUtils.deserializeLegacy("&aJava&f: &e" + Placeholders.VALUE);
	@Setting("JavaHome")
	private Component javaHome = TextUtils.deserializeLegacy("&e" + Placeholders.VALUE);
	@Setting("Plugins")
	private Component plugins = TextUtils.deserializeLegacy("&ePlugins &7(&b" + Placeholders.VALUE + "&7)&e");
	@Setting("RefreshPlugin")
	private Component refreshPlugin = TextUtils.deserializeLegacy(null);
	@Setting("Mods")
	private Component mods = TextUtils.deserializeLegacy("&eMods &7(&b" + Placeholders.VALUE + "&7)&e");
	@Setting("PlayerMods")
	private Component playerMods = TextUtils.deserializeLegacy("&eMods " + Placeholders.PLAYER + " &7(&b" + Placeholders.VALUE + "&7)&e");

	@Override
	public Buttons getButtons() {
		return buttons;
	}

	@Override
	public Memory getMemory() {
		return memory;
	}

	@Override
	public Worlds getWorlds() {
		return worlds;
	}

	@Override
	public AboutPlugin getAboutPlugin() {
		return aboutPlugin;
	}

	@Override
	public AboutMod getAboutMod() {
		return aboutMod;
	}

	@Override
	public Component getTitle() {
		return title;
	}

	@Override
	public Component getTPS(Component value) {
		return Text.of(tps).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getUptime(Component value) {
		return Text.of(uptime).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getServerTime(Component value) {
		return Text.of(serverTime).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getSystemInfo() {
		return systemInfo;
	}

	@Override
	public Component getSystem(Component value) {
		return Text.of(system).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getJava(Component value) {
		return Text.of(java).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getJavaHome(Component value) {
		return Text.of(javaHome).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getPlugins(Component value) {
		return Text.of(plugins).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getRefreshPlugin() {
		return javaHome;
	}

	@Override
	public Component getMods(Component value) {
		return Text.of(mods).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getPlayerMods(ServerPlayer player, Component value) {
		return Text.of(playerMods).replace(Placeholders.PLAYER, player.name()).replace(Placeholders.VALUE, value).get();
	}

}
