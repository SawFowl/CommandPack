package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.serverstat.AboutMod;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.serverstat.AboutPlugin;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.serverstat.Buttons;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.serverstat.Memory;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.serverstat.Worlds;

public interface ServerStat {

	Buttons getButtons();

	Memory getMemory();

	Worlds getWorlds();

	AboutPlugin getAboutPlugin();

	AboutMod getAboutMod();

	Component getTitle();

	Component getTPS(Component value);

	Component getUptime(Component value);

	Component getServerTime(Component value);

	Component getSystemInfo();

	Component getSystem(Component value);

	Component getJava(Component value);

	Component getJavaHome(Component value);

	Component getPlugins(Component value);

	Component getRefreshPlugin();

	Component getMods(Component value);

	Component getPlayerMods(ServerPlayer player, Component value);

}
