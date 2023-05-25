package sawfowl.commandpack.commands.abstractcommands.raw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spongepowered.api.command.CommandCompletion;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawArgument;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.commands.abstractcommands.PluginCommand;

public abstract class AbstractRawCommand extends PluginCommand implements RawCommand {

	private List<CommandCompletion> empty = new ArrayList<>();
	private Map<String, RawCommand> childExecutors = new HashMap<String, RawCommand>();
	private Map<Integer, RawArgument<?>> args = new HashMap<>();
	public AbstractRawCommand(CommandPack plugin) {
		super(plugin);
	}

	public abstract List<RawArgument<?>> arguments();

	@Override
	public List<CommandCompletion> getEmptyCompletion() {
		return empty;
	}

	@Override
	public Map<String, RawCommand> getChildExecutors() {
		return childExecutors;
	}

	@Override
	public Map<Integer, RawArgument<?>> getArguments() {
		if(arguments() == null) return null;
		for(RawArgument<?> arg : arguments()) args.put(arg.getCursor(), arg);
		return args;
	}

}
