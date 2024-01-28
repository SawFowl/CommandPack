package sawfowl.commandpack.commands.abstractcommands;

import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.LocatableBlock;
import org.spongepowered.plugin.PluginContainer;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.commandpack.configure.configs.commands.CommandSettings;
import sawfowl.commandpack.configure.locale.Locales;
import sawfowl.commandpack.utils.Logger;

public abstract class PluginCommand implements sawfowl.commandpack.api.commands.PluginCommand {

	protected final CommandPack plugin;
	protected final String[] aliases;
	protected CommandSettings commandSettings;
	public PluginCommand(CommandPack plugin) {
		this.plugin = plugin;
		if(command() != null) this.commandSettings = plugin.getCommandsConfig().getCommandConfig(command());
		this.aliases = commandSettings != null ? commandSettings.getAliases() : null;
	}

	public PluginCommand(CommandSettings commandSettings) {
		this.plugin = CommandPack.getInstance();
		this.commandSettings = commandSettings;
		this.aliases = commandSettings.getAliases();
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
	public Component getComponent(Object[] path) {
		return null;
	}

	public void setCommandSettings(CommandSettings commandSettings) {
		this.commandSettings = commandSettings;
	}

	public void updateCommandSettings() {
		commandSettings = plugin.getCommandsConfig().getCommandConfig(command());
	}

	protected Locales getLocales() {
		return plugin.getLocales();
	}

	protected Logger getLogger() {
		return plugin.getLogger();
	}

	public Optional<ServerPlayer> getPlayer(String name) {
		return Sponge.server().player(name);
	}

	protected boolean isCommandBlock(CommandCause cause) {
		return getLocatableBlock(cause).filter(block -> (block.blockState().type().equals(BlockTypes.COMMAND_BLOCK.get()) || block.blockState().type().equals(BlockTypes.CHAIN_COMMAND_BLOCK.get()) || block.blockState().type().equals(BlockTypes.REPEATING_COMMAND_BLOCK.get()))).isPresent();
	}

	protected Optional<LocatableBlock> getLocatableBlock(CommandCause cause) {
		return cause.first(LocatableBlock.class);
	}

	protected String blockCords(CommandCause cause) {
		return getLocatableBlock(cause).map(LocatableBlock::serverLocation).map(location -> ("<" + location.worldKey().asString() + ">" + location.blockPosition())).orElse("");
	}

	protected boolean isCommandBlockMinecart(CommandCause cause) {
		return getEntity(cause).isPresent();
	}

	protected Optional<Entity> getEntity(CommandCause cause) {
		return cause.first(Entity.class).filter(entity -> (entity.type().equals(EntityTypes.COMMAND_BLOCK_MINECART.get())));
	}

	protected String entityCords(CommandCause cause) {
		return getEntity(cause).map(Entity::serverLocation).map(location -> ("<" + location.worldKey().asString() + ">" + location.blockPosition())).orElse("");
	}

	protected String getString(Locale locale, Object[] path) {
		return plugin.getLocales().getString(locale, path);
	}

}
