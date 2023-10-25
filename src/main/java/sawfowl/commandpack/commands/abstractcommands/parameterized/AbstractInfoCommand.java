package sawfowl.commandpack.commands.abstractcommands.parameterized;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.lifecycle.RefreshGameEvent;
import org.spongepowered.common.SpongeCommon;
import org.spongepowered.common.event.manager.SpongeEventManager;
import org.spongepowered.common.event.tracking.PhaseTracker;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.metadata.PluginMetadata;
import org.spongepowered.plugin.metadata.model.PluginContributor;
import org.spongepowered.plugin.metadata.model.PluginDependency;

import com.google.common.collect.Iterables;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.miscellaneous.ModContainer;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.Text;
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
		Component header = getComponent(locale, LocalesPaths.COMMANDS_SERVERSTAT_OS_HEADER);
		Component os = getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_OS).replace(Placeholders.VALUE, this.os).get();
		Component jvm = isPlayer ? getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_JAVA).replace(Placeholders.VALUE, this.java).get().hoverEvent(HoverEvent.showText(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_JAVAHOME).replace(Placeholders.VALUE, this.javaHome).get())) : getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_OS).replace(Placeholders.VALUE, (this.java + " " + this.javaHome)).get();
		sendPaginationList(target, header, Component.text("=").color(header.color()), linesPerPage, Arrays.asList(os, jvm));
	}

	protected void sendWorldsInfo(Audience target, Locale locale) {
		Component header = getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_WORLDS_INFO_HEADER).get();
		List<Component> worldsInfo = Sponge.server().worldManager().worlds().stream().map(world -> getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_WORLDINFO).replace(
				new String[] {Placeholders.WORLD, Placeholders.CHUNKS_SIZE, Placeholders.ENTITIES_SIZE, Placeholders.VALUE},
				text(world.key().asString()), text(Iterables.size(world.loadedChunks())), text(world.entities().size()), tPStoText(BigDecimal.valueOf(world.engine().ticksPerSecond()).setScale(2, RoundingMode.HALF_UP).doubleValue())).get()).toList();
		sendPaginationList(target, header, Component.text("=").color(header.color()), linesPerPage, worldsInfo);
	}

	protected void sendPluginsInfo(Audience target, Locale locale, boolean isPlayer) {
		Component header = getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_HEADER_PLUGINS).replace(Placeholders.VALUE, plugin.getAPI().getPluginContainers().size()).get();
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) target;
			List<Component> content = new ArrayList<>();
			boolean allowRefresh = player.hasPermission(Permissions.SERVER_STAT_STAFF_INFO_PLUGINS_REFRESH);
			for(PluginContainer container : plugin.getAPI().getPluginContainers()) {
				if(allowRefresh) {
					content.add(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_REFRESH_BUTTON).createCallBack(cause -> {
						sendRefreshEvent(container);
						player.sendMessage(getComponent(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_REFRESH_MESSAGE));
					}).append(player.hasPermission(Permissions.SERVER_STAT_STAFF_PLUGINS_INFO)
							?
							Text.of(text("&a " + container.metadata().name().orElse(container.metadata().id()))).createCallBack(() -> {
								sendPluginInfo(player, locale, container.metadata());
							}).get()
							: 
							text("&a " + container.metadata().name().orElse(container.metadata().id()))).get());
				} else {
					content.add(text("&a" + container.metadata().name().orElse(container.metadata().id())));
				}
			}
			sendPaginationList(target, header, Component.text("=").color(header.color()), linesPerPage, content);
		} else {
			header = header.append(text("&f: "));
			int size = plugin.getAPI().getPluginContainers().size();
			for(PluginContainer container : plugin.getAPI().getPluginContainers()) {
				header = size > 1 ? header.append(text("&e" + container.metadata().name().orElse(container.metadata().id()) + "&f, ")) : header.append(text("&e" + container.metadata().name().orElse(container.metadata().id()) + "&f."));
				size--;
			}
			target.sendMessage(header);
		}
	}

	protected void sendModsInfo(Audience target, Locale locale, boolean isPlayer) {
		Component header = getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_HEADER_MODS).replace(Placeholders.VALUE, plugin.getAPI().getModContainers().size()).get();
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) target;
			List<Component> content = new ArrayList<>();
			for(ModContainer container : plugin.getAPI().getModContainers()) {
				content.add(player.hasPermission(Permissions.SERVER_STAT_STAFF_PLUGINS_INFO)
						?
						Text.of(text("&a" + container.getDisplayName())).createCallBack(() -> {
							sendModInfo(player, locale, container);
						}).get()
						: 
						text("&a" + container.getDisplayName()));
			}
			sendPaginationList(target, header, Component.text("=").color(header.color()), linesPerPage, content);
		} else {
			header = header.append(text("&f: "));
			int size = plugin.getAPI().getModContainers().size();
			for(ModContainer container : plugin.getAPI().getModContainers()) {
				header = size > 1 ? header.append(text("&e" + container.getDisplayName() + "&f, ")) : header.append(text("&e" + container.getDisplayName() + "&f."));
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

	protected Component getTPS(Locale locale) {
		return getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_TPS).replace(Placeholders.VALUE, tPStoText(currentTPS())
				.append(text("&f, "))
				.append(tPStoText(plugin.getAverageTPS1m()))
				.append(text("&f, "))
				.append(tPStoText(plugin.getAverageTPS5m()))
				.append(text("&f, "))
				.append(tPStoText(plugin.getAverageTPS10m()))).get();
	}

	protected Component getServerTime(Locale locale) {
		SimpleDateFormat format = new SimpleDateFormat(getString(locale, LocalesPaths.COMMANDS_SERVERSTAT_TIMEFORMAT));
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(System.currentTimeMillis());
		return getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_TIME).replace(Placeholders.VALUE, format.format(calendar.getTime())).get();
	}

	protected void sendRefreshEvent(PluginContainer container) {
		RefreshGameEvent event = SpongeEventFactory.createRefreshGameEvent(
			PhaseTracker.getCauseStackManager().currentCause(),
			SpongeCommon.game()
		);
		((SpongeEventManager) SpongeCommon.game().eventManager()).postToPlugin(event, container);
	}

	protected Component getUptime(Locale locale) {
		return getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_UPTIME).replace(Placeholders.VALUE, timeFormat(plugin.getServerUptime(), locale).append(Component.text(" / ")).append(timeFormat(ManagementFactory.getRuntimeMXBean().getUptime() / 1000, locale))).get();
	}

	protected void sendPluginInfo(Audience audience, Locale locale, PluginMetadata metadata) {
		List<Component> info = new ArrayList<>();
		Component header = getComponent(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_HEADER);
		info.add(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_ID).replace(Placeholders.VALUE, metadata.id()).get());
		info.add(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_NAME).replace(Placeholders.VALUE, metadata.name().orElse("-")).get());
		info.add(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_VERSION).replace(Placeholders.VALUE, (metadata.version().getMajorVersion() + "." + metadata.version().getMinorVersion() + "." + metadata.version().getIncrementalVersion() + (metadata.version().getBuildNumber() == 0 ? "" : "-" + metadata.version().getBuildNumber()))).get());
		info.add(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_ENTRYPOINT).replace(Placeholders.VALUE, metadata.entrypoint()).get());
		info.add(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_DESCRIPTION).replace(Placeholders.VALUE, metadata.description().orElse("-")).get());
		info.add(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_DEPENDENCIES).replace(Placeholders.VALUE, getDependencies(metadata)).get());
		info.add(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_CONTRIBUTORS).replace(Placeholders.VALUE, getContributors(metadata)).get());
		info.add(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_LINKS).replace(new String[] {Placeholders.HOME_LINK, Placeholders.SOURCE_LINK, Placeholders.ISSUES_LINK}, createLinkText(metadata.links().homepage()), createLinkText(metadata.links().source()), createLinkText(metadata.links().issues())).get());
		sendPaginationList(audience, header, Component.text("=").color(header.color()), linesPerPage, info);
	}

	protected void sendModInfo(Audience audience, Locale locale, ModContainer container) {
		if(container == null) return;
		List<Component> info = new ArrayList<>();
		Component header = getComponent(locale, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_HEADER);
		info.add(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_ID).replace(Placeholders.VALUE, container.getModId()).get());
		info.add(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_NAME).replace(Placeholders.VALUE, container.getDisplayName()).get());
		info.add(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_VERSION).replace(Placeholders.VALUE, container.getVersion()).get());
		info.add(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_DESCRIPTION).replace(Placeholders.VALUE, container.getDescription()).get());
		info.add(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_DEPENDENCIES).replace(Placeholders.VALUE, container.getDependencies()).get());
		info.add(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_LINKS).replace(new String[] {Placeholders.ISSUES_LINK, Placeholders.UPDATE_LINK}, createLinkText(container.getIssueURL()), createLinkText(container.getUpdateURL())).get());
		sendPaginationList(audience, header, Component.text("=").color(header.color()), linesPerPage, info);
	}

	private Component getContributors(PluginMetadata metadata) {
		if(metadata.contributors().isEmpty()) return TextUtils.deserializeLegacy("&e-");
		String contributors = "";
		int size = metadata.contributors().size();
		for(PluginContributor contributor : metadata.contributors()) {
			contributors = contributors + (size > 1 ? "&e" + contributor.name() + "&f, " : "&e" + contributor.name() + "&f.");
			size--;
		}
		return TextUtils.deserializeLegacy(contributors);
	}

	private Component getDependencies(PluginMetadata metadata) {
		if(metadata.dependencies().isEmpty()) return TextUtils.deserializeLegacy("&e-");
		String dependencies = "";
		int size = metadata.dependencies().size();
		for(PluginDependency dependency : metadata.dependencies()) {
			dependencies = dependencies + (size > 1 ? "&e" + dependency.id() + " (" + dependency.version().getRecommendedVersion().getMajorVersion() + "." + dependency.version().getRecommendedVersion().getMinorVersion()  + "." + dependency.version().getRecommendedVersion().getIncrementalVersion() + ")" + "&f, " : "&e" + dependency.id() + " (" + dependency.version().getRecommendedVersion().getMajorVersion() + "." + dependency.version().getRecommendedVersion().getMinorVersion()  + "." + dependency.version().getRecommendedVersion().getIncrementalVersion() + ")" + "&f.");
			size--;
		}
		return TextUtils.deserializeLegacy(dependencies);
	}

	private Component createLinkText(Optional<URL> optional) {
		return optional.isPresent() ? TextUtils.deserializeLegacy("&e" + optional.get().toString()).clickEvent(ClickEvent.openUrl(optional.get())) : TextUtils.deserializeLegacy("&e-");
	}

	private Component createLinkText(URL url) {
		return url == null ? TextUtils.deserializeLegacy("&e-") : TextUtils.deserializeLegacy("&e" + url.toString()).clickEvent(ClickEvent.openUrl(url));
	}

	private double currentTPS() {
		return BigDecimal.valueOf(Sponge.server().ticksPerSecond()).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

}