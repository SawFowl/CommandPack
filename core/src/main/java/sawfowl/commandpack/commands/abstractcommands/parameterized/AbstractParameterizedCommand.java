package sawfowl.commandpack.commands.abstractcommands.parameterized;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.LocatableBlock;
import org.spongepowered.plugin.PluginContainer;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.AbstractPluginCommand;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.commands.parameterized.ParameterizedCommand;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.CommandExceptions;

public abstract class AbstractParameterizedCommand extends AbstractPluginCommand<CommandPack> implements ParameterizedCommand {

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
	public Map<String, ParameterSettings> getSettingsMap() {
		return parameterSettings;
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

	protected boolean isCommandBlock(CommandCause cause) {
		return getLocatableBlock(cause).filter(block -> (block.blockState().type().equals(BlockTypes.COMMAND_BLOCK.get()) || block.blockState().type().equals(BlockTypes.CHAIN_COMMAND_BLOCK.get()) || block.blockState().type().equals(BlockTypes.REPEATING_COMMAND_BLOCK.get()))).isPresent();
	}

	protected Optional<LocatableBlock> getLocatableBlock(CommandCause cause) {
		return cause.first(LocatableBlock.class);
	}

	protected boolean isCommandBlockMinecart(CommandCause cause) {
		return getEntity(cause).isPresent();
	}

	protected Optional<Entity> getEntity(CommandCause cause) {
		return cause.first(Entity.class).filter(entity -> (entity.type().equals(EntityTypes.COMMAND_BLOCK_MINECART.get())));
	}

	protected CommandExceptions getExceptions(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommandExceptions();
	}

	protected CommandExceptions getExceptions(ServerPlayer player) {
		return getExceptions(player.locale());
	}

}
