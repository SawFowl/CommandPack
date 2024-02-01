package sawfowl.commandpack.api;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.spongepowered.api.event.Event;
import org.spongepowered.api.world.generation.ChunkGenerator;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.plugin.PluginContainer;

import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.data.command.CancelRules;
import sawfowl.commandpack.api.data.command.Delay;
import sawfowl.commandpack.api.data.command.Price;
import sawfowl.commandpack.api.data.command.RawSettings;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.commandpack.api.data.command.UpdateTree;
import sawfowl.commandpack.api.data.miscellaneous.ModContainer;
import sawfowl.commandpack.api.services.CPEconomyService;
import sawfowl.commandpack.api.services.PunishmentService;
import sawfowl.commandpack.api.tps.TPS;
import sawfowl.commandpack.configure.serializers.CancelRulesSerializer;
import sawfowl.commandpack.configure.serializers.CommandPriceSerializer;
import sawfowl.commandpack.configure.serializers.CommandSettingSerializer;
import sawfowl.commandpack.configure.serializers.DelaySerializer;
import sawfowl.commandpack.configure.serializers.RawSettingsSerializer;
import sawfowl.commandpack.configure.serializers.UpdateTreeSerializer;

/**
 * Plugin API.
 * 
 * @author SawFowl
 */
public interface CommandPack {

	public static final TypeSerializerCollection COMMAND_SETTINGS_SERIALIZERS = TypeSerializerCollection.defaults().childBuilder().register(Settings.class, new CommandSettingSerializer()).register(RawSettings.class, new RawSettingsSerializer()).register(UpdateTree.class, new UpdateTreeSerializer()).register(Price.class, new CommandPriceSerializer()).register(Delay.class, new DelaySerializer()).register(CancelRules.class, new CancelRulesSerializer()).build();

	/**
	 * Viewing and changing player data.
	 */
	PlayersData playersData();

	/**
	 * Interface for working with teleportation to random coordinates.
	 */
	RandomTeleportService randomTeleportService();

	/**
	 * Whether the plugin is running on the server with Forge.
	 */
	boolean isForgeServer();

	/**
	 * Kits API.
	 */
	KitService kitService();

	/**
	 * Registration of the custom chunk generator.<br>
	 * All registered generators will be available in the command `/world create`.
	 */
	void registerCustomGenerator(String name, ChunkGenerator chunkGenerator);

	/**
	 * Getting a custom chunk generator.
	 */
	Optional<ChunkGenerator> getCustomGenerator(String name);

	/**
	 * Get a {@link Set} of names of all registered custom chunk generators.
	 */
	Set<String> getAvailableGenerators();

	/**
	 * A system for punishing players.
	 */
	Optional<PunishmentService> getPunishmentService();

	/**
	 * Economy Service.
	 */
	Optional<CPEconomyService> getEconomyService();

	/**
	 * Getting information about server and worlds TPS.
	 */
	TPS getTPS();

	/**
	 * Getting a collection of {@link PluginContainer} on the server.
	 */
	Collection<PluginContainer> getPluginContainers();

	/**
	 * Getting a collection of {@link ModContainer} on the server.
	 */
	Collection<ModContainer> getModContainers();

	/**
	 * Updates the argument tree for a registered command using the {@link RawCommand} interface.<br>
	 * If argument tree building is not enabled in the command settings, there will be no effect.<br>
	 * This operation can be useful, for example, when a player join/exits,<br>
	 * if his nickname is used as a command argument and the command uses an argument tree.
	 */
	void updateCommandTree(String command);

	/**
	 * Updates the argument tree for a registered commands using the {@link RawCommand} interface.<br>
	 * If argument tree building is not enabled in the command settings, there will be no effect.<br>
	 * This operation can be useful, for example, when a player join/exits,<br>
	 * if his nickname is used as a command argument and the command uses an argument tree.
	 */
	void updateCommandsTree(String... commands);

	/**
	 * Event for getting the plugin API.
	 */
	interface PostAPI extends Event {

		public CommandPack getAPI();

	}

}
