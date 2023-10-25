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

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.commandpack.utils.CommandExecutorTypes;

public class CommandLogListener {

	private final CommandPack plugin;
	private final Locale locale;
	public CommandLogListener(CommandPack plugin) {
		this.plugin = plugin;
		locale = plugin.getLocales().getLocaleService().getSystemOrDefaultLocale();
	}

	@Listener(order = Order.PRE)
	public void onExecute(ExecuteCommandEvent.Pre event) {
		switch (CommandExecutorTypes.findType(event.commandCause())) {
		case SYSTEM:
			log(getString(LocalesPaths.COMMANDS_LOG).replace(Placeholders.SOURCE, getString(LocalesPaths.NAME_SYSTEM)).replace(Placeholders.COMMAND, event.command()).replace(Placeholders.ARGS, " " + event.arguments()));
			break;
		case COMMAND_BLOCK:
			log(getString(LocalesPaths.COMMANDS_LOG).replace(Placeholders.SOURCE, (getString(LocalesPaths.NAME_COMMANDBLOCK) + blockCords(event.commandCause()))).replace(Placeholders.COMMAND, event.command()).replace(Placeholders.ARGS, " " + event.arguments()));
			break;
		case COMMAND_BLOCK_MINECART:
			log(getString(LocalesPaths.COMMANDS_LOG).replace(Placeholders.SOURCE, (getString(LocalesPaths.NAME_COMMANDBLOCK_MINECART) + entityCords(event.commandCause()))).replace(Placeholders.COMMAND, event.command()).replace(Placeholders.ARGS, " " + event.arguments()));
			break;
		case NAMEABLE:
			log(getString(LocalesPaths.COMMANDS_LOG).replace(Placeholders.SOURCE, ((Nameable) event.commandCause().audience()).name()).replace(Placeholders.COMMAND, event.command()).replace(Placeholders.ARGS, " " + event.arguments()));
			break;
		case CUSTOM_NPC:
			log(getString(LocalesPaths.COMMANDS_LOG).replace(Placeholders.SOURCE, "CustomNPC").replace(Placeholders.COMMAND, event.command()).replace(Placeholders.ARGS, " " + event.arguments()));
			break;
		default:
			log(getString(LocalesPaths.COMMANDS_LOG).replace(Placeholders.SOURCE, getString(LocalesPaths.NAME_UNKNOWN)).replace(Placeholders.COMMAND, event.command()).replace(Placeholders.ARGS, " " + event.arguments()));
			break;
		}
	}

	private void log(String string) {
		plugin.getLogger().info(string);
	}

	private String getString(Object[] path) {
		return plugin.getLocales().getString(locale, path);
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
