package sawfowl.commandpack.commands.abstractcommands.raw;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandCompletion;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.commands.abstractcommands.PluginCommand;

public abstract class AbstractRawCommand extends PluginCommand implements RawCommand {

	private List<CommandCompletion> empty = new ArrayList<>();
	public AbstractRawCommand(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public List<CommandCompletion> getEmptyCompletion() {
		return empty;
	}

}
