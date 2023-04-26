package sawfowl.commandpack.api.data.commands.parameterized;

import java.net.URL;
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
import org.spongepowered.plugin.metadata.model.PluginContributor;
import org.spongepowered.plugin.metadata.model.PluginDependency;

import com.google.common.collect.Iterables;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.IModInfo.ModVersion;
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
			boolean allowInfo = player.hasPermission(Permissions.SERVER_STAT_STAFF_INFO_PLUGINS);
			boolean allowRefresh = player.hasPermission(Permissions.SERVER_STAT_STAFF_INFO_PLUGINS_REFRESH);
			for(PluginContainer container : Sponge.pluginManager().plugins()) {
				Component plugin = Component.empty();
				if(allowInfo) {
					Component infoButton = TextUtils.createCallBack(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_BUTTON), () -> {
						sendPluginInfo(player, locale, container);
					});
					plugin = plugin.append(infoButton).append(text("&r "));
				}
				if(allowRefresh) {
					Component refreshButton = TextUtils.createCallBack(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_REFRESH_BUTTON), () -> {
						sendRefreshEvent(container);
						player.sendMessage(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_REFRESH_MESSAGE));
					});
					plugin = plugin.append(refreshButton).append(text("&r "));
				}
				plugin = plugin.append(text("&a" + container.metadata().name().orElse(container.metadata().id())));
				content.add(plugin);
			}
			sendPaginationList(target, header, Component.text("=").color(header.color()), linesPerPage, content);
		} else {
			header = header.append(text("&f: "));
			int size = Sponge.pluginManager().plugins().size();
			for(PluginContainer container : Sponge.pluginManager().plugins()) {
				header = size > 0 ? header.append(text("&e" + container.metadata().name().orElse(container.metadata().id()) + "&f, ")) : header.append(text("&e" + container.metadata().name().orElse(container.metadata().id()) + "&f."));
				size--;
			}
			target.sendMessage(header);
		}
	}

	protected void sendModsInfo(Audience target, Locale locale, boolean isPlayer) {
		if(!plugin.isForgeServer()) return;
		List<ModInfo> mods = ModList.get().getMods().stream().filter(mod -> (!Sponge.pluginManager().plugin(mod.getModId()).isPresent())).collect(Collectors.toList());
		Component header = getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_HEADER_MODS);
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) target;
			List<Component> content = new ArrayList<>();
			for(ModInfo mod : mods) {
				Component infoButton = TextUtils.createCallBack(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_BUTTON), () -> {
					sendModInfo(player, locale, mod);
				});
				content.add(infoButton.append(Component.text(" ")).append(text("&6" + mod.getDisplayName())));
			}
			sendPaginationList(target, header, Component.text("=").color(header.color()), linesPerPage, content);
		} else {
			int size = mods.size();
			for(ModInfo mod : mods) {
				header = size > 0 ? header.append(text("&e" + mod.getDisplayName() + "&f, ")) : header.append(text("&e" + mod.getDisplayName() + "&f."));
				size--;
			}
			target.sendMessage(header);
		}
	}

	private void sendPluginInfo(ServerPlayer target, Locale locale, PluginContainer container) {
		Component info = TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_NAME), Placeholders.VALUE, container.metadata().name().orElse("-")).append(Component.newline());
		info = info.append(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_ID), Placeholders.VALUE, container.metadata().id())).append(Component.newline());
		info = info.append(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_VERSION), Placeholders.VALUE, container.metadata().version().getQualifier())).append(Component.newline());
		info = info.append(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_DESCRIPTION), Placeholders.VALUE, container.metadata().description().orElse("-"))).append(Component.newline());
		info = info.append(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_ENTRYPOINT), Placeholders.VALUE, container.metadata().entrypoint())).append(Component.newline());
		info = info.append(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_CONTRIBUTORS), Placeholders.VALUE, makeContributorsText(container))).append(Component.newline());
		info = info.append(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_DEPENDECIES), Placeholders.VALUE, makePluginDependsText(container)));
		if(container.metadata().links().homepage().isPresent() || container.metadata().links().source().isPresent() || container.metadata().links().issues().isPresent()) {
			info = info.append(Component.newline());
			Component links = getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_LINKS);
			if(container.metadata().links().homepage().isPresent()) links.append(createClickableText(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_LINKS_HOME), container.metadata().links().homepage().get()));
			if(container.metadata().links().source().isPresent()) links.append(createClickableText(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_LINKS_SOURCE), container.metadata().links().source().get()));
			if(container.metadata().links().issues().isPresent()) links.append(createClickableText(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_LINKS_ISSUES), container.metadata().links().issues().get()));
			info = info.append(links);
		}
		target.sendMessage(info);
	}

	private void sendModInfo(ServerPlayer target, Locale locale, ModInfo mod) {
		Component info = TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MOD_ID), Placeholders.VALUE, mod.getModId()).append(Component.newline());
		info = info.append(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MOD_NAME), Placeholders.VALUE, mod.getDisplayName())).append(Component.newline());
		info = info.append(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MOD_VERSION), Placeholders.VALUE, mod.getVersion().getQualifier())).append(Component.newline());
		info = info.append(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MOD_DESCRIPTION), Placeholders.VALUE, mod.getDescription())).append(Component.newline());
		info = info.append(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MOD_DEPENDECIES), Placeholders.VALUE, makeModDependsText(mod))).append(Component.newline());
		info = info.append(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MOD_LINKS)).append(Component.newline());
		info = info.append(createClickableText(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MOD_URL_UPDATE), mod.getUpdateURL())).append(Component.newline());
		info = info.append(createClickableText(getText(locale, LocalesPaths.COMMANDS_SERVERSTAT_MOD_URL_ISSUES), mod.getOwningFile().getIssueURL()));
		target.sendMessage(info);
	}

	private void sendRefreshEvent(PluginContainer container) {
		RefreshGameEvent event = SpongeEventFactory.createRefreshGameEvent(
			PhaseTracker.getCauseStackManager().currentCause(),
			SpongeCommon.game()
		);
		((SpongeEventManager) SpongeCommon.game().eventManager()).postToPlugin(event, container);
	}

	private Component makeContributorsText(PluginContainer container) {
		if(container.metadata().contributors().isEmpty()) return Component.text("-");
		Component contributors = Component.empty();
		int size = container.metadata().contributors().size();
		for(PluginContributor contributor : container.metadata().contributors()) {
			contributors = size > 0 ? contributors.append(text("&e" + contributor.name() + "&f, ")) : contributors.append(text("&e" + contributor.name() + "&f."));
			size--;
		}
		return contributors;
	}

	private Component makePluginDependsText(PluginContainer container) {
		if(container.metadata().dependencies().isEmpty()) return Component.text("-");
		Component dependencies = Component.empty();
		int size = container.metadata().contributors().size();
		for(PluginDependency dependency : container.metadata().dependencies()) {
			dependencies = size > 0 ? dependencies.append(text("&e" + dependency.id() + "&f, ")) : dependencies.append(text("&e" + dependency.id() + "&f."));
			size--;
		}
		return dependencies;
	}

	private Component makeModDependsText(ModInfo mod) {
		if(mod.getDependencies().isEmpty()) return Component.text("-");
		Component dependencies = Component.empty();
		int size = mod.getDependencies().size();
		for(ModVersion dependency : mod.getDependencies()) {
			dependencies = size > 0 ? dependencies.append(text("&e" + dependency.getModId() + "&f, ")) : dependencies.append(text("&e" + dependency.getModId() + "&f."));
			size--;
		}
		return dependencies;
	}

	private Component createClickableText(Component component, URL url) {
		return component.clickEvent(ClickEvent.openUrl(url));
	}

}
