package sawfowl.commandpack.api.commands;

import sawfowl.commandpack.api.data.command.Settings;

public abstract class AbstractPluginCommand<T> {

	protected final T plugin;
	private Settings commandSettings;
	public AbstractPluginCommand(T instance) {
		plugin = instance;
		commandSettings = applyCommandSettings();
	}

	public abstract Settings applyCommandSettings();

	public Settings getCommandSettings() {
		return commandSettings;
	}

}
