package sawfowl.commandpack.commands.abstractcommands.raw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.plugin.PluginContainer;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.data.command.Settings;
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

	@Override
	public Component getText(Object[] path) {
		return null;
	}

	@Override
	public PluginContainer getContainer() {
		return plugin.getPluginContainer();
	}

	@Override
	public Settings getCommandSettings() {
		return commandSettings;
	}

	@Override
	public Map<UUID, Long> getCooldowns() {
		return plugin.getPlayersData().getTempData().getTrackingMap(trackingName());
	}

}
