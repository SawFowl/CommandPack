package sawfowl.commandpack.commands.abstractcommands.raw;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.plugin.PluginContainer;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.AbstractPluginCommand;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.data.command.Settings;

public abstract class AbstractRawCommand extends AbstractPluginCommand<CommandPack> implements RawCommand {

	private Map<String, RawCommand> childExecutors = new HashMap<String, RawCommand>();
	private Map<Integer, RawArgument<?>> args = new HashMap<>();
	public AbstractRawCommand(CommandPack plugin) {
		super(plugin);
	}

	public abstract List<RawArgument<?>> arguments();

	public abstract List<RawCommand> childCommands();

	@Override
	public Map<String, RawCommand> getChildExecutors() {
		if(childExecutors.isEmpty()) {
			List<RawCommand> childs = childCommands();
			if(childs != null && !childs.isEmpty()) childs.forEach(this::addChildCommand);
		}
		return childExecutors;
	}

	@Override
	public Map<Integer, RawArgument<?>> getArguments() {
		if(arguments() == null) return null;
		return args.isEmpty() ? args = arguments().stream().collect(Collectors.toMap(arg -> arg.getCursor(), arg -> arg)) : args;
	}

	public void addChildCommand(RawCommand command) {
		if(!childExecutors.containsKey(command.command())) childExecutors.put(command.command(), command);
		if(command.getCommandSettings() != null) for(String alias : command.getCommandSettings().getAliases()) {
			if(!childExecutors.containsKey(alias)) childExecutors.put(alias, command);
		}
	}

	@Override
	public PluginContainer getContainer() {
		return plugin.getPluginContainer();
	}

	@Override
	public Component getComponent(Object[] path) {
		return null;
	}

	@Override
	public Settings applyCommandSettings() {
		return command() != null ? plugin.getCommandsConfig().getCommandConfig(command()) : null;
	}

	public Optional<ServerPlayer> getPlayer(String name) {
		return Sponge.server().player(name);
	}

	protected String getString(Locale locale, Object[] path) {
		return plugin.getLocales().getString(locale, path);
	}

}
