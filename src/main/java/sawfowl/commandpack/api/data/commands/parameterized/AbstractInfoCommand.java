package sawfowl.commandpack.api.data.commands.parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.plugin.PluginContainer;

import com.google.common.collect.Iterables;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public abstract class AbstractInfoCommand extends AbstractParameterizedCommand {

	protected final String os;
	protected final String java;
	protected final String javaHome;
	private final int linesPerPage = 15;
	public AbstractInfoCommand(CommandPack plugin) {
		super(plugin);
		os = System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch");
		java = System.getProperty("java.vendor") + " " + System.getProperty("java.version");
		javaHome = System.getProperty("java.home");
	}

	protected void sendSystemInfo(Audience target, Locale locale, boolean isPlayer) {
		Component header = getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_OS_HEADER);
		Component os = TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_OS), Placeholders.VALUE, this.os);
		Component jvm = isPlayer ? TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_JAVA), Placeholders.VALUE, this.java).hoverEvent(HoverEvent.showText(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_JAVAHOME), Placeholders.VALUE, this.javaHome))) : TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_OS), Placeholders.VALUE, (this.java + " " + this.javaHome));
		sendPaginationList(target, header, Component.text("=").color(header.color()), linesPerPage, Arrays.asList(os, jvm));
	}

	protected void sendWorldsInfo(Audience target, Locale locale) {
		Component header = getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_WORLDS_INFO_HEADER);
		List<Component> worldsInfo = Sponge.server().worldManager().worlds().stream().map(world -> (TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_WORLDINFO), 
				new String[] {Placeholders.WORLD, Placeholders.CHUNKS_SIZE, Placeholders.ENTITIES_SIZE, Placeholders.VALUE, Placeholders.LIMIT}, 
				new Object[] {world.key().asString(), Iterables.size(world.loadedChunks()), world.entities().size(), world.engine().ticksPerSecond(), 20}))).collect(Collectors.toList());
		sendPaginationList(target, header, Component.text("=").color(header.color()), linesPerPage, worldsInfo);
	}

	protected void sendPluginsInfo(Audience target, Locale locale, boolean isPlayer) {
		Component header = getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_HEADER_PLUGINS);
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) target;
			List<Component> content = new ArrayList<>();
			for(PluginContainer container : Sponge.pluginManager().plugins()) {
				Component plugin = Component.empty();
				if(Permissions.isAllowMoreInfoPlugin(player, container)) {
					Component button = TextUtils.createCallBack(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_BUTTON), () -> {
						List<Component> pluginInfo = new ArrayList<>();
						
					});
				}
			}
		} else {
			header = header.append(text("&f: "));
			int size = Sponge.pluginManager().plugins().size();
			for(PluginContainer container : Sponge.pluginManager().plugins()) {
				header = size > 0 ? header.append(text("&e" + container.metadata().name().orElse(container.metadata().id()) + "&f, ")) : header.append(text("&e" + container.metadata().name().orElse(container.metadata().id()) + "&f."));
				size--;
			}
		}
	}

	protected void sendModsInfo(Audience target, Locale locale, boolean isPlayer) {
		if(!plugin.isForgeServer()) return;
		
	}

}
