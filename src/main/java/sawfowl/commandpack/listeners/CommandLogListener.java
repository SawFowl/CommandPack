package sawfowl.commandpack.listeners;

import org.spongepowered.api.SystemSubject;
import org.spongepowered.api.block.entity.CommandBlock;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.ExecuteCommandEvent;
import org.spongepowered.api.util.Nameable;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.configure.LocalesPaths;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.localeapi.api.TextUtils;

public class CommandLogListener {

	private final CommandPack plugin;
	private final Component toLog;
	public CommandLogListener(CommandPack plugin) {
		this.plugin = plugin;
		toLog = plugin.getLocales().getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_LOG);
	}

	@Listener(order = Order.PRE)
	public void onExecute(ExecuteCommandEvent.Pre event) {
		String name = event.commandCause().audience() instanceof Nameable ? ((Nameable) event.commandCause().audience()).name() :
			event.commandCause().audience() instanceof SystemSubject ? "Server" :
				event.commandCause().audience() instanceof CommandBlock ? "CommandBlock " + ((CommandBlock) event.commandCause().audience()).blockPosition().toString() : "UnknownSource";
		plugin.getLogger().info(TextUtils.replace(toLog, new String[] {Placeholders.SOURCE, Placeholders.COMMAND}, new String[] {name, event.command()}));
	}

}
