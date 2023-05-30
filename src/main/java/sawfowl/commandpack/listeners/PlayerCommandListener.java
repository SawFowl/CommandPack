package sawfowl.commandpack.listeners;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.ExecuteCommandEvent;
import org.spongepowered.api.event.filter.cause.First;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class PlayerCommandListener {

	private final CommandPack plugin;
	public PlayerCommandListener(CommandPack plugin) {
		this.plugin = plugin;
	}

	@Listener(order = Order.LAST)
	public void onExecute(ExecuteCommandEvent.Pre event, @First ServerPlayer player) {
		if(Sponge.server().onlinePlayers().size() > 40) {
			Sponge.asyncScheduler().executor(plugin.getPluginContainer()).execute(() -> {
				spyCommand(event, player, true);
			});
		} else spyCommand(event, player, false);
		if(plugin.getPlayersData().getTempData().isAfk(player) && event.command().equalsIgnoreCase("afk")) return;
		plugin.getPlayersData().getTempData().updateLastActivity(player);
		if(!plugin.getPlayersData().getTempData().isTrackingPlayer(player)) return;
		plugin.getPlayersData().getTempData().getTrackingPlayerCommands(player).ifPresent(map -> {
			map.forEach((commandName, config) -> {
				if(!config.getDelay().getCancelRules().isAllowOtherCommand()) {
					plugin.getPlayersData().getTempData().removeCommandTracking(commandName, player);
					player.sendMessage(TextUtils.replace(plugin.getLocales().getText(player.locale(), LocalesPaths.COMMANDS_STOP_TRACKING_COMMAND), Placeholders.COMMAND, "/" + commandName));
				}
			});
		});
	}

	private void spyCommand(ExecuteCommandEvent.Pre event, ServerPlayer player, boolean parallel) {
		(parallel ? Sponge.server().onlinePlayers().parallelStream() : Sponge.server().onlinePlayers().stream()).filter(p -> !p.uniqueId().equals(player.uniqueId()) && plugin.getPlayersData().getTempData().isSpyCommand(p)).forEach(p -> {
			p.sendMessage(TextUtils.replaceToComponents(plugin.getLocales().getText(p.locale(), LocalesPaths.COMMANDS_COMMANDSPY_SPY), new String[] {Placeholders.PLAYER, Placeholders.COMMAND}, new Component[] {Component.text(player.name()).clickEvent(ClickEvent.suggestCommand("/tell " + player.name())), Component.text("/" + event.command() + " " + event.arguments())}));
		});
	}
}
