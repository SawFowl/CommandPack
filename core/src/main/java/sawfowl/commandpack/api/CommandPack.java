package sawfowl.commandpack.api;

import java.util.Optional;
import java.util.Set;

import org.spongepowered.api.event.Event;
import org.spongepowered.api.world.generation.ChunkGenerator;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.plugin.PluginContainer;

import sawfowl.commandpack.api.commands.parameterized.ParameterizedCommand;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.data.command.CancelRules;
import sawfowl.commandpack.api.data.command.Delay;
import sawfowl.commandpack.api.data.command.Price;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.commandpack.api.data.miscellaneous.ModContainer;
import sawfowl.commandpack.api.services.CPEconomyService;
import sawfowl.commandpack.api.services.PunishmentService;
import sawfowl.commandpack.api.tps.TPS;
import sawfowl.commandpack.configure.serializers.CancelRulesSerializer;
import sawfowl.commandpack.configure.serializers.CommandPriceSerializer;
import sawfowl.commandpack.configure.serializers.CommandSettingSerializer;
import sawfowl.commandpack.configure.serializers.DelaySerializer;

/**
 * Plugin API.
 * 
 * @author SawFowl
 */
public interface CommandPack {

	public static final TypeSerializerCollection COMMAND_SETTINGS_SERIALIZERS = TypeSerializerCollection.defaults().childBuilder().register(Settings.class, new CommandSettingSerializer()).register(Price.class, new CommandPriceSerializer()).register(Delay.class, new DelaySerializer()).register(CancelRules.class, new CancelRulesSerializer()).build();

	/**
	 * Viewing and changing player data.
	 */
	PlayersData getPlayersData();

	/**
	 * Interface for working with teleportation to random coordinates.
	 */
	RandomTeleportService getRandomTeleportService();

	/**
	 * Whether the plugin is running on the server with LexForge.
	 */
	boolean isForgeServer();

	/**
	 * Whether the plugin is running on the server with NeoForge.
	 */
	boolean isNeoForgeServer();

	/**
	 * Whether the plugin is running on the server with LexForge or NeoForge.
	 */
	boolean isModifiedServer();

	/**
	 * Kits API.
	 */
	KitService getKitService();

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
	 * Getting a collections of {@link PluginContainer} and {@link ModContainer} on the server.
	 */
	ContainersCollection getContainersCollection();

	/**
	 * Registering a command at the final stage of server loading, when all in-game data is available.<br>
	 * The method will be available only when getting to CommandPack API.<br>
	 * Registration of commands using this method will be blocked after server loading is completed.
	 */
	void registerCommand(RawCommand command) throws IllegalStateException;

	/**
	 * Registering a command at the final stage of server loading, when all in-game data is available.<br>
	 * The method will be available only when getting to CommandPack API.<br>
	 * Registration of commands using this method will be blocked after server loading is completed.
	 */
	void registerCommand(ParameterizedCommand command) throws IllegalStateException;

	/**
	 * Event for getting the plugin API.
	 */
	interface PostAPI extends Event {

		public CommandPack getAPI();

	}

}
