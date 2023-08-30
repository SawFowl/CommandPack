package sawfowl.commandpack.commands.abstractcommands.parameterized;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

import net.minecraftforge.fml.loading.FMLLoader;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.commandpack.utils.ModContainer;
import sawfowl.localeapi.api.TextUtils;

public abstract class AbstractInfoCommand extends AbstractParameterizedCommand {

	protected final String os;
	protected final String java;
	protected final String javaHome;
	protected final int linesPerPage = 15;
	protected Collection<PluginContainer> containers = new ArrayList<>();
	protected Set<ModContainer> mods = new HashSet<>();
	public AbstractInfoCommand(CommandPack plugin) {
		super(plugin);
		os = System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch");
		java = System.getProperty("java.vendor") + " " + System.getProperty("java.version");
		javaHome = System.getProperty("java.home");
		plugin.getLogger().debug(containers);
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
		Component header = TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_HEADER_PLUGINS), Placeholders.VALUE, String.valueOf(containers.size()));
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) target;
			List<Component> content = new ArrayList<>();
			boolean allowRefresh = player.hasPermission(Permissions.SERVER_STAT_STAFF_INFO_PLUGINS_REFRESH);
			for(PluginContainer container : containers) {
				if(allowRefresh) {
					content.add(TextUtils.createCallBack(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_REFRESH_BUTTON), () -> {
						sendRefreshEvent(container);
						player.sendMessage(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_REFRESH_MESSAGE));
					}).append(player.hasPermission(Permissions.SERVER_STAT_STAFF_PLUGINS_INFO)
							?
							TextUtils.createCallBack(text("&a " + container.metadata().name().orElse(container.metadata().id())), () -> {
								sendPluginInfo(player, locale, container.metadata());
							})
							: 
							text("&a " + container.metadata().name().orElse(container.metadata().id()))));
				} else {
					content.add(text("&a" + container.metadata().name().orElse(container.metadata().id())));
				}
			}
			sendPaginationList(target, header, Component.text("=").color(header.color()), linesPerPage, content);
		} else {
			header = header.append(text("&f: "));
			int size = containers.size();
			for(PluginContainer container : containers) {
				header = size > 1 ? header.append(text("&e" + container.metadata().name().orElse(container.metadata().id()) + "&f, ")) : header.append(text("&e" + container.metadata().name().orElse(container.metadata().id()) + "&f."));
				size--;
			}
			target.sendMessage(header);
		}
	}

	protected void sendModsInfo(Audience target, Locale locale, boolean isPlayer) {
		Component header = TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_HEADER_MODS), Placeholders.VALUE, String.valueOf(mods.size()));
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) target;
			List<Component> content = new ArrayList<>();
			for(ModContainer container : mods) {
				content.add(player.hasPermission(Permissions.SERVER_STAT_STAFF_PLUGINS_INFO)
						?
						TextUtils.createCallBack(text("&a" + container.getDisplayName()), () -> {
							sendModInfo(player, locale, container);
						})
						: 
						text("&a" + container.getDisplayName()));
			}
			sendPaginationList(target, header, Component.text("=").color(header.color()), linesPerPage, content);
		} else {
			header = header.append(text("&f: "));
			int size = mods.size();
			for(ModContainer container : mods) {
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
		return TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_TPS), Placeholders.VALUE, tPStoText(currentTPS())
				.append(text("&f, "))
				.append(tPStoText(plugin.getAverageTPS1m()))
				.append(text("&f, "))
				.append(tPStoText(plugin.getAverageTPS5m()))
				.append(text("&f, "))
				.append(tPStoText(plugin.getAverageTPS10m()))
				);
	}

	protected Component getServerTime(Locale locale) {
		SimpleDateFormat format = new SimpleDateFormat(getString(locale, LocalesPaths.COMMANDS_SERVERSTAT_TIMEFORMAT));
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(System.currentTimeMillis());
		return TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_TIME), Placeholders.VALUE, text(format.format(calendar.getTime())));
	}

	protected void sendRefreshEvent(PluginContainer container) {
		RefreshGameEvent event = SpongeEventFactory.createRefreshGameEvent(
			PhaseTracker.getCauseStackManager().currentCause(),
			SpongeCommon.game()
		);
		((SpongeEventManager) SpongeCommon.game().eventManager()).postToPlugin(event, container);
	}

	protected Component getUptime(Locale locale) {
		return TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_UPTIME), Placeholders.VALUE, timeFormat(plugin.getServerUptime(), locale).append(Component.text(" / ")).append(timeFormat(ManagementFactory.getRuntimeMXBean().getUptime() / 1000, locale)));
	}

	protected void fillLists() {
		if(mods == null) mods = new HashSet<>();
		if(containers == null) containers = new ArrayList<>();
		if(plugin.isForgeServer()) {
			FMLLoader.getLoadingModList().getMods().forEach(mod -> {
				plugin.getLogger().warn(mod.getOwningFile().getModLoader());
				plugin.getLogger().warn(mod.getOwningFile().getModLoader().length());
				if(!mod.getOwningFile().getModLoader().equalsIgnoreCase("java_plain") && !mod.getOwningFile().getModLoader().equalsIgnoreCase("")) mods.add(new ModContainer(mod));
			});
			containers.addAll(Sponge.pluginManager().plugins().stream().filter(container -> (!mods.stream().filter(mod -> mod.getModId().equals(container.metadata().id())).findFirst().isPresent())).collect(Collectors.toList()));
		} else containers.addAll(Sponge.pluginManager().plugins());
	}

	protected void sendPluginInfo(Audience audience, Locale locale, PluginMetadata metadata) {
		List<Component> info = new ArrayList<>();
		Component header = getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_HEADER);
		info.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_ID), Placeholders.VALUE, metadata.id()));
		info.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_NAME), Placeholders.VALUE, metadata.name().orElse("-")));
		info.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_VERSION), Placeholders.VALUE, (metadata.version().getMajorVersion() + "." + metadata.version().getMinorVersion() + "." + metadata.version().getIncrementalVersion() + (metadata.version().getBuildNumber() == 0 ? "" : "-" + metadata.version().getBuildNumber()))));
		info.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_ENTRYPOINT), Placeholders.VALUE, metadata.entrypoint()));
		info.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_DESCRIPTION), Placeholders.VALUE, metadata.description().orElse("-")));
		info.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_DEPENDENCIES), Placeholders.VALUE, getDependencies(metadata)));
		info.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_CONTRIBUTORS), Placeholders.VALUE, getContributors(metadata)));
		info.add(TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_LINKS), new String[] {Placeholders.HOME_LINK, Placeholders.SOURCE_LINK, Placeholders.ISSUES_LINK}, new Component[] {createLinkText(metadata.links().homepage()), createLinkText(metadata.links().source()), createLinkText(metadata.links().issues())}));
		sendPaginationList(audience, header, Component.text("=").color(header.color()), linesPerPage, info);
	}

	protected void sendModInfo(Audience audience, Locale locale, ModContainer container) {
		if(container == null) return;
		List<Component> info = new ArrayList<>();
		Component header = getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_HEADER);
		info.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_ID), Placeholders.VALUE, container.getModId()));
		info.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_NAME), Placeholders.VALUE, container.getDisplayName()));
		info.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_VERSION), Placeholders.VALUE, container.getVersion()));
		info.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_DESCRIPTION), Placeholders.VALUE, container.getDescription()));
		info.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_DEPENDENCIES), Placeholders.VALUE, container.getDependencies()));
		info.add(TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_LINKS), new String[] {Placeholders.ISSUES_LINK, Placeholders.UPDATE_LINK}, new Component[] {createLinkText(container.getIssueURL()), createLinkText(container.getUpdateURL())}));
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