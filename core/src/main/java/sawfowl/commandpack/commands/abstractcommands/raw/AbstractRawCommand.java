package sawfowl.commandpack.commands.abstractcommands.raw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.commands.abstractcommands.PluginCommand;

public abstract class AbstractRawCommand extends PluginCommand implements RawCommand {

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

	@Override
	public boolean enableAutoComplete() {
		return plugin.getMainConfig().isAutoCompleteRawCommands();
	}

	public void addChildCommand(RawCommand command) {
		if(!childExecutors.containsKey(command.command())) childExecutors.put(command.command(), command);
		for(String alias : command.getCommandSettings().getAliases()) {
			if(!childExecutors.containsKey(alias)) childExecutors.put(alias, command);
		}
	}

}
