package sawfowl.commandpack.commands.abstractcommands.parameterized;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.spongepowered.plugin.PluginContainer;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.data.commands.CommandSettings;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterizedCommand;
import sawfowl.commandpack.commands.abstractcommands.PluginCommand;

public abstract class AbstractParameterizedCommand extends PluginCommand implements ParameterizedCommand {

	Map<UUID, Long> cooldowns = new HashMap<>();
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
		return cooldowns;
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
