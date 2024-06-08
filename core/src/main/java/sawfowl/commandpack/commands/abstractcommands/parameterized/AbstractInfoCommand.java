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

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.miscellaneous.ModContainer;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.ServerStat;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.serverstat.AboutMod;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.serverstat.AboutPlugin;
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
		Component header = getServerStat(locale).getTitle();
		Component os = getServerStat(locale).getSystem(this.os);
		Component jvm = isPlayer ? getServerStat(locale).getJava(this.java).hoverEvent(HoverEvent.showText(getServerStat(locale).getJavaHome(this.javaHome))) : getServerStat(locale).getJava(this.java).append(Component.text(" ")).append(getServerStat(locale).getJavaHome(this.javaHome));
		sendPaginationList(target, header, Component.text("=").color(header.color()), linesPerPage, Arrays.asList(os, jvm));
	}

	protected void sendWorldsInfo(Audience target, Locale locale) {
		Component header = getServerStat(locale).getWorlds().getTitle();
		List<Component> worldsInfo = Sponge.server().worldManager().worlds().stream().map(world -> getServerStat(locale).getWorlds().getWorldInfo(world, BigDecimal.valueOf(plugin.getAPI().getTPS().getWorldTPS(world)).setScale(2, RoundingMode.HALF_UP).doubleValue(), plugin.getAPI().getTPS().getWorldTickTime(world))).toList();
		sendPaginationList(target, header, Component.text("=").color(header.color()), linesPerPage, worldsInfo);
	}

	protected void sendPluginsInfo(Audience target, Locale locale, boolean isPlayer) {
		Component header = getServerStat(locale).getPlugins(plugin.getAPI().getPluginContainers().size());
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) target;
			List<Component> content = new ArrayList<>();
			boolean allowRefresh = player.hasPermission(Permissions.SERVER_STAT_STAFF_INFO_PLUGINS_REFRESH);
			for(PluginContainer container : plugin.getAPI().getPluginContainers()) {
				if(allowRefresh) {
					content.add(getServerStat(locale).getButtons().getRefreshPlugin().createCallBack(cause -> {
						sendRefreshEvent(container);
						player.sendMessage(getServerStat(locale).getRefreshPlugin());
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
		Component header = getServerStat(locale).getMods(plugin.getAPI().getModContainers().size());
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

	protected Component tickToText(double tick) {
		if(tick > 70) return text("&5" + String.format("%.3f", tick));
		if(tick > 50) return text("&c" + String.format("%.3f", tick));
		if(tick > 40) return text("&6" + String.format("%.3f", tick));
		if(tick > 30) return text("&e" + String.format("%.3f", tick));
		if(tick > 20) return text("&a" + String.format("%.3f", tick));
		return text("&2" + String.format("%.3f", tick));
	}

	protected Component getTPS(Locale locale) {
		return getServerStat(locale).getTPS(tPStoText(currentTPS())
				.append(text("&f-"))
				.append(tickToText(Sponge.server().averageTickTime()))
				.append(text("&f, "))
				.append(tPStoText(plugin.getAverageTPS1m()))
				.append(text("&f, "))
				.append(tPStoText(plugin.getAverageTPS5m()))
				.append(text("&f, "))
				.append(tPStoText(plugin.getAverageTPS10m())));
	}

	protected Component getServerTime(Locale locale) {
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getLocale(locale).getTime().getFormat());
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(System.currentTimeMillis());
		return getServerStat(locale).getServerTime(format.format(calendar.getTime()));
	}

	protected void sendRefreshEvent(PluginContainer container) {
		RefreshGameEvent event = SpongeEventFactory.createRefreshGameEvent(
			PhaseTracker.getCauseStackManager().currentCause(),
			SpongeCommon.game()
		);
		((SpongeEventManager) SpongeCommon.game().eventManager()).postToPlugin(event, container);
	}

	protected Component getUptime(Locale locale) {
		return getServerStat(locale).getUptime(timeFormat(plugin.getServerUptime(), locale).append(Component.text(" / ")).append(timeFormat(ManagementFactory.getRuntimeMXBean().getUptime() / 1000, locale)));
	}

	protected void sendPluginInfo(Audience audience, Locale locale, PluginMetadata metadata) {
		List<Component> info = new ArrayList<>();
		info.add(getAboutPlugin(locale).getId(metadata.id()));
		info.add(getAboutPlugin(locale).getName(metadata.name().orElse("-")));
		info.add(getAboutPlugin(locale).getVersion(metadata.version().getMajorVersion() + "." + metadata.version().getMinorVersion() + "." + metadata.version().getIncrementalVersion() + (metadata.version().getBuildNumber() == 0 ? "" : "-" + metadata.version().getBuildNumber())));
		info.add(getAboutPlugin(locale).getEntrypoint(metadata.entrypoint()));
		info.add(getAboutPlugin(locale).getDescription(metadata.description().orElse("-")));
		info.add(getAboutPlugin(locale).getDependencies(getDependencies(metadata)));
		info.add(getAboutPlugin(locale).getContributors(getContributors(metadata)));
		info.add(getAboutPlugin(locale).getLinks(createLinkText(metadata.links().homepage()), createLinkText(metadata.links().source()), createLinkText(metadata.links().issues())));
		sendPaginationList(audience, getAboutPlugin(locale).getTitle(), Component.text("=").color(getAboutPlugin(locale).getTitle().color()), linesPerPage, info);
	}

	protected void sendModInfo(Audience audience, Locale locale, ModContainer container) {
		if(container == null) return;
		List<Component> info = new ArrayList<>();
		Component header = getAboutMod(locale).getTitle();
		info.add(getAboutMod(locale).getId(container.getModId()));
		info.add(getAboutMod(locale).getName(container.getDisplayName()));
		info.add(getAboutMod(locale).getVersion(container.getVersion()));
		info.add(getAboutMod(locale).getDescription(container.getDescription()));
		info.add(getAboutMod(locale).getDependencies(container.getDependencies()));
		info.add(getAboutMod(locale).getLinks(createLinkText(container.getIssueURL()), createLinkText(container.getUpdateURL())));
		sendPaginationList(audience, header, Component.text("=").color(header.color()), linesPerPage, info);
	}

	protected ServerStat getServerStat(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getServerStat();
	}

	protected AboutPlugin getAboutPlugin(Locale locale) {
		return getServerStat(locale).getAboutPlugin();
	}

	protected AboutMod getAboutMod(Locale locale) {
		return getServerStat(locale).getAboutMod();
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