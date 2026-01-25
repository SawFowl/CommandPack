package sawfowl.commandpack.api;

import java.util.Optional;
import java.util.Set;

import org.spongepowered.api.event.Event;
import org.spongepowered.api.world.generation.ChunkGenerator;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.plugin.PluginContainer;

import com.google.inject.Inject;

import sawfowl.commandpack.api.commands.parameterized.ParameterizedCommand;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.data.command.CancelRules;
import sawfowl.commandpack.api.data.command.Delay;
import sawfowl.commandpack.api.data.command.Price;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.commandpack.api.data.miscellaneous.ModContainer;
import sawfowl.commandpack.api.mixin.game.MixinServerWorld;
import sawfowl.commandpack.api.network.CustomPayloadsService;
import sawfowl.commandpack.api.services.CPEconomyService;
import sawfowl.commandpack.api.services.PunishmentService;
import sawfowl.commandpack.api.tps.AverageTPS;
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
public abstract class CommandPack {

	@Inject
	private static CommandPack INSTANCE;

	public static final TypeSerializerCollection COMMAND_SETTINGS_SERIALIZERS = TypeSerializerCollection.defaults().childBuilder().register(Settings.class, new CommandSettingSerializer()).register(Price.class, new CommandPriceSerializer()).register(Delay.class, new DelaySerializer()).register(CancelRules.class, new CancelRulesSerializer()).build();


	/**
	 * Getting the API.<br>
	 * You can use this method in your plugin's constructor if your plugin is loaded after CommandPack.
	 */
	public static CommandPack getInstance() {
		return INSTANCE;
	}

	/**
	 * Viewing and changing player data.
	 */
	public abstract PlayersData getPlayersData();

	/**
	 * Interface for working with teleportation to random coordinates.
	 */
	public abstract RandomTeleportService getRandomTeleportService();

	/**
	 * Whether the plugin is running on the server with LexForge.
	 */
	public abstract boolean isForgeServer();

	/**
	 * Whether the plugin is running on the server with NeoForge.
	 */
	public abstract boolean isNeoForgeServer();

	/**
	 * Whether the plugin is running on the server with LexForge or NeoForge.
	 */
	public abstract boolean isModifiedServer();

	/**
	 * Kits API.
	 */
	public abstract KitService getKitService();

	/**
	 * Registration of the custom chunk generator.<br>
	 * All registered generators will be available in the command `/world create`.
	 */
	public abstract void registerCustomGenerator(String name, ChunkGenerator chunkGenerator);

	/**
	 * Getting a custom chunk generator.
	 */
	public abstract Optional<ChunkGenerator> getCustomGenerator(String name);

	/**
	 * Get a {@link Set} of names of all registered custom chunk generators.
	 */
	public abstract Set<String> getAvailableGenerators();

	/**
	 * A system for punishing players.
	 */
	public abstract Optional<PunishmentService> getPunishmentService();

	/**
	 * Economy Service.
	 */
	public abstract Optional<CPEconomyService> getEconomyService();

	/**
	 * Getting information about server and worlds TPS.
	 * @deprecated Use {@link MixinServerWorld}
	 */
	@Deprecated
	public abstract TPS getTPS();

	/**
	 * View average TPS values over time intervals of 1m, 5m, 10m.
	 */
	public abstract AverageTPS getAverageTPS();

	/**
	 * Getting a collections of {@link PluginContainer} and {@link ModContainer} on the server.
	 */
	public abstract ContainersCollection getContainersCollection();

	/**
	 * Registering a command at the final stage of server loading, when all in-game data is available.<br>
	 * The method will be available only when getting to CommandPack API.<br>
	 * Registration of commands using this method will be blocked after server loading is completed.
	 */
	public abstract void registerCommand(RawCommand command) throws IllegalStateException;

	/**
	 * Registering a command at the final stage of server loading, when all in-game data is available.<br>
	 * The method will be available only when getting to CommandPack API.<br>
	 * Registration of commands using this method will be blocked after server loading is completed.
	 */
	public abstract void registerCommand(ParameterizedCommand command) throws IllegalStateException;

	public abstract CustomPayloadsService getCustomPayloadsService();

	/**
	 * Event for getting the plugin API.
	 */
	@Deprecated
	public interface PostAPI extends Event {

		public CommandPack getAPI();

	}

}
