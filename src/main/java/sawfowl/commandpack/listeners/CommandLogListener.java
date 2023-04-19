package sawfowl.commandpack.listeners;

import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.SystemSubject;
import org.spongepowered.api.block.BlockTypes;
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
import sawfowl.localeapi.api.TextUtils;

public class CommandLogListener {

	private final CommandPack plugin;
	private final Locale locale;
	public CommandLogListener(CommandPack plugin) {
		this.plugin = plugin;
		locale = plugin.getLocales().getLocaleService().getSystemOrDefaultLocale();
	}

	@Listener(order = Order.PRE)
	public void onExecute(ExecuteCommandEvent.Pre event) {
		/*plugin.getLogger().error(isCommandBlock(event.commandCause()));
		getLocatableBlock(event.commandCause()).ifPresent(block -> {
			plugin.getLogger().error(block.blockPosition());
		});*/
		String name = event.commandCause().audience() instanceof SystemSubject ? getString(LocalesPaths.NAME_SYSTEM) :
				isCommandBlock(event.commandCause()) ? getString(LocalesPaths.NAME_COMMANDBLOCK) + blockCords(event.commandCause()) :
					isCommandBlockMinecart(event.commandCause()) ? getString(LocalesPaths.NAME_COMMANDBLOCK_MINECART) + entityCords(event.commandCause()) :
						event.commandCause().audience() instanceof Nameable ? ((Nameable) event.commandCause().audience()).name() :
							getString(LocalesPaths.NAME_UNKNOWN);
		plugin.getLogger().info(getString(LocalesPaths.COMMANDS_LOG).replace(Placeholders.SOURCE, TextUtils.clearDecorations(name)).replace(Placeholders.COMMAND, event.command()).replace(Placeholders.ARGS, " " + event.arguments()));
	}

	private String getString(Object[] path) {
		return plugin.getLocales().getString(locale, path);
	}

	private boolean isCommandBlock(CommandCause cause) {
		return getLocatableBlock(cause).filter(block -> (block.blockState().type().equals(BlockTypes.COMMAND_BLOCK.get()) || block.blockState().type().equals(BlockTypes.CHAIN_COMMAND_BLOCK.get()) || block.blockState().type().equals(BlockTypes.REPEATING_COMMAND_BLOCK.get()))).isPresent();
	}

	private Optional<LocatableBlock> getLocatableBlock(CommandCause cause) {
		return cause.first(LocatableBlock.class);
	}

	private String blockCords(CommandCause cause) {
		return getLocatableBlock(cause).map(LocatableBlock::serverLocation).map(location -> ("<" + location.worldKey().asString() + ">" + location.blockPosition())).orElse("");
	}

	private boolean isCommandBlockMinecart(CommandCause cause) {
		return getEntity(cause).isPresent();
	}

	private Optional<Entity> getEntity(CommandCause cause) {
		return cause.first(Entity.class).filter(entity -> (entity.type().equals(EntityTypes.COMMAND_BLOCK_MINECART.get())));
	}

	private String entityCords(CommandCause cause) {
		return getEntity(cause).map(Entity::serverLocation).map(location -> ("<" + location.worldKey().asString() + ">" + location.blockPosition())).orElse("");
	}

}
