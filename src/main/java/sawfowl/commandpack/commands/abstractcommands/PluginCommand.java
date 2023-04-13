package sawfowl.commandpack.commands.abstractcommands;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.SystemSubject;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.util.Nameable;
import org.spongepowered.api.world.LocatableBlock;

import net.kyori.adventure.audience.Audience;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.configure.configs.commands.CommandSettings;
import sawfowl.commandpack.configure.locale.Locales;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.commandpack.utils.Logger;

public abstract class PluginCommand {

	protected final CommandPack plugin;
	protected final String[] aliases;
	protected Map<UUID, Long> cooldowns = new HashMap<>();
	protected CommandSettings commandSettings;
	public PluginCommand(CommandPack plugin) {
		this.plugin = plugin;
		this.commandSettings = plugin.getCommandsConfig().getCommandConfig(command());
		this.aliases = commandSettings.getAliases();
	}

	public PluginCommand(CommandSettings commandSettings) {
		this.plugin = CommandPack.getInstance();
		this.commandSettings = commandSettings;
		this.aliases = commandSettings.getAliases();
	}

	public abstract String command();

	public void setCommandSettings(CommandSettings commandSettings) {
		this.commandSettings = commandSettings;
	}

	public void updateCommandSettings() {
		commandSettings = plugin.getCommandsConfig().getCommandConfig(command());
	}

	protected BigDecimal createDecimal(double value) {
		return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
	}

	protected Locales getLocales() {
		return plugin.getLocales();
	}

	protected Logger getLogger() {
		return plugin.getLogger();
	}

	protected long getExpireHourFromNow(long second) {
		return TimeUnit.SECONDS.toHours(second);
	}

	protected long getExpireMinuteFromNow(long second) {
		return TimeUnit.SECONDS.toMinutes(second);
	}

	protected Optional<ServerPlayer> getPlayer(UUID uuid) {
		return Sponge.server().player(uuid);
	}

	public Optional<ServerPlayer> getPlayer(String name) {
		return Sponge.server().player(name);
	}

	protected void saveUser(User user) {
		Sponge.server().userManager().forceSave(user.uniqueId());
	}

	protected class CooldownTimerTask implements Consumer<ScheduledTask> {
		public CooldownTimerTask(ServerPlayer player){
			uuid = player.uniqueId();
		}
		private final UUID uuid;
		private final long currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
		long cooldown = plugin.getCommandsConfig().getCommandConfig(command()).getCooldown();
		@Override
		public void accept(ScheduledTask task) {
			if(cooldowns.containsKey(uuid)) {
				if((cooldowns.get(uuid) + cooldown) - currentTime <= 0) cooldowns.remove(uuid);
			} else {
				task.cancel();
			}
			
		}
		
	}

	protected String getSourceName(CommandCause cause, Audience audience, Locale locale, ServerPlayer player) {
		return plugin.getMainConfig().isHideTeleportCommandSource() && !player.hasPermission(Permissions.IGNORE_HIDE_COMMAND_SOURCE) ? player.name() :
			audience instanceof SystemSubject ? getString(locale, LocalesPaths.NAME_SYSTEM) :
				isCommandBlock(cause) ? getString(locale, LocalesPaths.NAME_COMMANDBLOCK) + blockCords(cause) :
					isCommandBlockMinecart(cause) ? getString(locale, LocalesPaths.NAME_COMMANDBLOCK_MINECART) + entityCords(cause) :
						audience instanceof Nameable ? ((Nameable) audience).name() :
							getString(locale, LocalesPaths.NAME_UNKNOWN);
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

	/**
	 * Added to speed up the creation of commands.
	 * Must be deleted after the job is done.
	 */
	@Deprecated
	public static Permissions perm() {
		return Permissions.instance;
	}

}
