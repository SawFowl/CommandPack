package sawfowl.commandpack.listeners;

import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.ExecuteCommandEvent;
import org.spongepowered.api.util.Nameable;
import org.spongepowered.api.world.LocatableBlock;
import org.spongepowered.common.SpongeGame;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Debug.Commands;
import sawfowl.commandpack.utils.CommandExecutorTypes;

public class CommandLogListener {

	private final CommandPackInstance plugin;
	private final Locale locale;
	public CommandLogListener(CommandPackInstance plugin) {
		this.plugin = plugin;
		locale = plugin.getLocales().getLocaleService().getSystemOrDefaultLocale();
	}

	@Listener(order = Order.PRE)
	public void onExecute(ExecuteCommandEvent.Post event) {
		if(event.commandCause().root() instanceof SpongeGame) return; //Bug in SpongeForge
		switch (CommandExecutorTypes.findType(event.commandCause())) {
		case SYSTEM:
			log(getCommands().getLog(getCommands().getExecutors().getServer(), event.command(), event.arguments()));
			break;
		case COMMAND_BLOCK:
			log(getCommands().getLog(getCommands().getExecutors().getCommandBlock(blockCords(event.commandCause())), event.command(), event.arguments()));
			break;
		case COMMAND_BLOCK_MINECART:
			log(getCommands().getLog(getCommands().getExecutors().getCommandBlockMinecart(entityCords(event.commandCause())), event.command(), event.arguments()));
			break;
		case NAMEABLE:
			log(getCommands().getLog(((Nameable) event.commandCause().audience()).name(), event.command(), event.arguments()));
			break;
		case CUSTOM_NPC:
			log(getCommands().getLog("CustomNPC", event.command(), event.arguments()));
			break;
		default:
			log(getCommands().getLog(getCommands().getExecutors().getUnknown(), event.command(), event.arguments()));
			break;
		}
	}

	private void log(String string) {
		plugin.getLogger().info(string);
	}

	private Commands getCommands() {
		return plugin.getLocales().getLocale(locale).getDebug().getCommands();
	}

	private Optional<LocatableBlock> getLocatableBlock(CommandCause cause) {
		return cause.first(LocatableBlock.class);
	}

	private String blockCords(CommandCause cause) {
		return getLocatableBlock(cause).map(LocatableBlock::serverLocation).map(location -> ("<" + location.worldKey().asString() + ">" + location.blockPosition())).orElse("");
	}

	private Optional<Entity> getEntity(CommandCause cause) {
		return cause.first(Entity.class).filter(entity -> (entity.type().equals(EntityTypes.COMMAND_BLOCK_MINECART.get())));
	}

	private String entityCords(CommandCause cause) {
		return getEntity(cause).map(Entity::serverLocation).map(location -> ("<" + location.worldKey().asString() + ">" + location.blockPosition())).orElse("");
	}

}
