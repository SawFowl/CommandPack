package sawfowl.commandpack.commands.abstractcommands.parameterized;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.lifecycle.RefreshGameEvent;
import org.spongepowered.common.SpongeCommon;
import org.spongepowered.common.event.manager.SpongeEventManager;
import org.spongepowered.common.event.tracking.PhaseTracker;
import org.spongepowered.plugin.PluginContainer;

import com.google.common.collect.Iterables;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

import sawfowl.localeapi.api.TextUtils;

public abstract class AbstractInfoCommand extends AbstractParameterizedCommand {

	protected final String os;
	protected final String java;
	protected final String javaHome;
	protected final int linesPerPage = 15;
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
		List<Component> worldsInfo = Sponge.server().worldManager().worlds().stream().map(world -> (TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_WORLDINFO), 
				new String[] {Placeholders.WORLD, Placeholders.CHUNKS_SIZE, Placeholders.ENTITIES_SIZE, Placeholders.VALUE}, 
				new Component[] {text(world.key().asString()), text(Iterables.size(world.loadedChunks())), text(world.entities().size()), tPStoText(BigDecimal.valueOf(world.engine().ticksPerSecond()).setScale(2, RoundingMode.HALF_UP).doubleValue())}))).collect(Collectors.toList());
		sendPaginationList(target, header, Component.text("=").color(header.color()), linesPerPage, worldsInfo);
	}

	protected void sendPluginsInfo(Audience target, Locale locale, boolean isPlayer) {
		Component header = getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_HEADER_PLUGINS);
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) target;
			List<Component> content = new ArrayList<>();
			boolean allowRefresh = player.hasPermission(Permissions.SERVER_STAT_STAFF_INFO_PLUGINS_REFRESH);
			for(PluginContainer container : Sponge.pluginManager().plugins()) {
				if(allowRefresh) {
					content.add(TextUtils.createCallBack(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_REFRESH_BUTTON), () -> {
						sendRefreshEvent(container);
						player.sendMessage(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_REFRESH_MESSAGE));
					}).append(text("&a " + container.metadata().name().orElse(container.metadata().id()))));
				} else {
					content.add(text("&a" + container.metadata().name().orElse(container.metadata().id())));
				}
			}
			sendPaginationList(target, header, Component.text("=").color(header.color()), linesPerPage, content);
		} else {
			header = header.append(text("&f: "));
			int size = Sponge.pluginManager().plugins().size();
			for(PluginContainer container : Sponge.pluginManager().plugins()) {
				header = size > 1 ? header.append(text("&e" + container.metadata().name().orElse(container.metadata().id()) + "&f, ")) : header.append(text("&e" + container.metadata().name().orElse(container.metadata().id()) + "&f."));
				size--;
			}
			target.sendMessage(header);
		}
	}

	protected Component tPStoText(double tps) {
		if(tps < 10) return text("&4" + tps);
		if(tps < 15) return text("&c" + tps);
		if(tps < 17) return text("&e" + tps);
		if(tps < 20) return text("&a" + tps);
		return text("&2" + tps);
	}

	private void sendRefreshEvent(PluginContainer container) {
		RefreshGameEvent event = SpongeEventFactory.createRefreshGameEvent(
			PhaseTracker.getCauseStackManager().currentCause(),
			SpongeCommon.game()
		);
		((SpongeEventManager) SpongeCommon.game().eventManager()).postToPlugin(event, container);
	}

}
