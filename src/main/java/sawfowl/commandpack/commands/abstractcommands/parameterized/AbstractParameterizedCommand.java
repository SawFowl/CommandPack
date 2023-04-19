package sawfowl.commandpack.commands.abstractcommands.parameterized;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.spongepowered.plugin.PluginContainer;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterizedCommand;
import sawfowl.commandpack.api.data.commands.settings.CommandSettings;
import sawfowl.commandpack.commands.abstractcommands.PluginCommand;

public abstract class AbstractParameterizedCommand extends PluginCommand implements ParameterizedCommand {

	protected final Map<String, ParameterSettings> parameterSettings = new HashMap<>();
	public AbstractParameterizedCommand(CommandPack plugin) {
		super(plugin);
		List<ParameterSettings> parameterSettings = getParameterSettings();
		if(parameterSettings != null && !parameterSettings.isEmpty()) {
			parameterSettings.forEach(setting -> {
				setting.getParameterUnknownType().key().key();
				this.parameterSettings.put(setting.getParameterUnknownType().key().key(), setting);
			});
		}
	}

	public abstract List<ParameterSettings> getParameterSettings();

	@Override
	public Map<UUID, Long> getCooldowns() {
		return plugin.getPlayersData().getTempData().getTrackingMap(trackingName());
	}

	@Override
	public Map<String, ParameterSettings> getSettingsMap() {
		return parameterSettings;
	}

	@Override
	public CommandSettings getCommandSettings() {
		return commandSettings;
	}

	@Override
	public PluginContainer getContainer() {
		return plugin.getPluginContainer();
	}

	@Override
	public Component getText(Object[] path) {
		return null;
	}

}
