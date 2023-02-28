package sawfowl.commandpack.commands.abstractcommands.parameterized;

import java.util.Locale;

import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.Command.Builder;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.configure.Locales;
import sawfowl.commandpack.utils.Logger;

public abstract class AbstractCommand implements CommandExecutor {

	private final CommandPack plugin;
	private final String command;
	private final String[] aliases;
	public AbstractCommand(CommandPack plugin, String command) {
		this.plugin = plugin;
		this.command = command;
		aliases = null;
	}

	public AbstractCommand(CommandPack plugin, String command, String[] aliases) {
		this.plugin = plugin;
		this.command = command;
		this.aliases = aliases;
	}

	public abstract void execute(CommandContext context, Audience audience, Locale locale) throws CommandException;

	public abstract Command.Parameterized build();

	public abstract boolean canExecute(CommandCause cause);

	public abstract String permission();

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		execute(context, context.cause().audience(), context.cause().audience() instanceof ServerPlayer ? ((ServerPlayer) context.cause().audience()).locale() : plugin.getLocales().getLocaleService().getSystemOrDefaultLocale());
		return success();
	}

	public CommandResult success() {
		return CommandResult.success();
	}

	public Builder builder() {
		return Command.builder().executionRequirements(cause -> (canExecute(cause)));
	}

	public CommandException exception(Component text) throws CommandException {
		throw new CommandException(text);
	}

	public CommandException exception(String text) throws CommandException {
		return exception(text(text));
	}

	public Component text(String string) {
		return LegacyComponentSerializer.legacyAmpersand().deserialize(string);
	}

	public void register(RegisterCommandEvent<Parameterized> event) {
		if(aliases != null && aliases.length > 0) {
			event.register(plugin.getPluginContainer(), build(), command, aliases);
		} else event.register(plugin.getPluginContainer(), build(), command);
	}

	public Locales getLocales() {
		return plugin.getLocales();
	}

	public Logger getLogger() {
		return plugin.getLogger();
	}

}
