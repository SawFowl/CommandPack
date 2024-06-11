package sawfowl.commandpack.configure.locale.locales.ru.commands;

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
import sawfowl.commandpack.configure.locale.locales.ru.commands.serverstat.ImplementAboutMod;
import sawfowl.commandpack.configure.locale.locales.ru.commands.serverstat.ImplementAboutPlugin;
import sawfowl.commandpack.configure.locale.locales.ru.commands.serverstat.ImplementButtons;
import sawfowl.commandpack.configure.locale.locales.ru.commands.serverstat.ImplementMemory;
import sawfowl.commandpack.configure.locale.locales.ru.commands.serverstat.ImplementWorlds;
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
	private Component title = TextUtils.deserializeLegacy("&3&lИнформация о сервере");
	@Setting("TPS")
	private Component tps = TextUtils.deserializeLegacy("&aTPS(Текущий, 1m, 5m, 10m)&f: " + Placeholders.VALUE);
	@Setting("Uptime")
	private Component uptime = TextUtils.deserializeLegacy("&aАптайм / JVM&f: &e" + Placeholders.VALUE);
	@Setting("ServerTime")
	private Component serverTime = TextUtils.deserializeLegacy("&aВремя сервера&f: &e" + Placeholders.VALUE);
	@Setting("SystemInfo")
	private Component systemInfo = TextUtils.deserializeLegacy("&3&lИнформация о системе");
	@Setting("System")
	private Component system = TextUtils.deserializeLegacy("&aСистема&f: &e" + Placeholders.VALUE);
	@Setting("Java")
	private Component java = TextUtils.deserializeLegacy("&aJava&f: &e" + Placeholders.VALUE);
	@Setting("JavaHome")
	private Component javaHome = TextUtils.deserializeLegacy("&e" + Placeholders.VALUE);
	@Setting("Plugins")
	private Component plugins = TextUtils.deserializeLegacy("&eПлагины &7(&b" + Placeholders.VALUE + "&7)&e");
	@Setting("RefreshPlugin")
	private Component refreshPlugin = TextUtils.deserializeLegacy("&eПредпринимается попытка перезагрузить плагин. Если плагин не поддерживает прослушивание события перезагрузки, эффекта не будет.");
	@Setting("Mods")
	private Component mods = TextUtils.deserializeLegacy("&eМоды &7(&b" + Placeholders.VALUE + "&7)&e");
	@Setting("PlayerMods")
	private Component playerMods = TextUtils.deserializeLegacy("&eМоды " + Placeholders.PLAYER + " &7(&b" + Placeholders.VALUE + "&7)&e");

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
	public Component getServerTime(String value) {
		return Text.of(serverTime).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getSystemInfo() {
		return systemInfo;
	}

	@Override
	public Component getSystem(String value) {
		return Text.of(system).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getJava(String value) {
		return Text.of(java).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getJavaHome(String value) {
		return Text.of(javaHome).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getPlugins(int value) {
		return Text.of(plugins).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getRefreshPlugin() {
		return refreshPlugin;
	}

	@Override
	public Component getMods(int value) {
		return Text.of(mods).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getPlayerMods(ServerPlayer player, int value) {
		return Text.of(playerMods).replace(Placeholders.PLAYER, player.name()).replace(Placeholders.VALUE, value).get();
	}

}
