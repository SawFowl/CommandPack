package sawfowl.commandpack.commands.settings;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.command.parameter.Parameter.Value.Builder;
import org.spongepowered.api.command.parameter.managed.ValueCompleter;
import org.spongepowered.api.command.parameter.managed.standard.VariableValueParameters;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.plugin.PluginContainer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.miscellaneous.ModContainer;
import sawfowl.localeapi.api.EnumLocales;

public class CommandParameters {

	public static final Value<Duration> DURATION = Parameter.duration().key("Duration").build();

	public static final Value<String> REPAIR = Parameter.choices("all", "armor", "hands").optional().key("Repair").requiredPermission(Permissions.REPAIR_SELECT).build();

	public static final Value<String> LOCALES = Parameter.choices(Stream.of(EnumLocales.values()).map(EnumLocales::getTag).toArray(String[]::new)).key("Locale").requiredPermission(Permissions.REPAIR_SELECT).build();

	public static final Parameter.Value<PluginContainer> PLUGIN = Parameter.builder(PluginContainer.class).key("Plugin").completer((context, currentInput) -> getInstance().getAPI().getPluginContainers().stream().filter(plugin -> plugin.metadata().id().contains(currentInput)).map(plugin -> CommandCompletion.of(plugin.metadata().id())).toList()).addParser(VariableValueParameters.dynamicChoicesBuilder(PluginContainer.class).choices(() -> getInstance().getAPI().getPluginContainers().stream().map(plugin -> plugin.metadata().id()).toList()).results(key -> getInstance().getAPI().getPluginContainers().stream().filter(plugin -> plugin.metadata().id().equals(key)).findFirst().orElse(null)).build()).build();

	public static final Parameter.Value<ModContainer> MOD = Parameter.builder(ModContainer.class).key("Mod").completer((context, currentInput) -> getInstance().getAPI().getModContainers().stream().filter(mod -> mod.getModId().contains(currentInput)).map(mod -> CommandCompletion.of(mod.getModId())).toList()).addParser(VariableValueParameters.dynamicChoicesBuilder(ModContainer.class).choices(() -> getInstance().getAPI().getModContainers().stream().map(mod -> mod.getModId()).toList()).results(key -> getInstance().getAPI().getModContainers().stream().filter(mod -> mod.getModId().equals(key)).findFirst().orElse(null)).build()).build();

	public static Value<ServerPlayer> createPlayer(boolean optional) {
		return (optional ? Parameter.player().key("Player").optional() : Parameter.player().key("Player")).build();
	}

	public static Value<ServerPlayer> createPlayer(String permission, boolean optional) {
		return (optional ? Parameter.player().key("Player").optional() : Parameter.player().key("Player")).requiredPermission(permission).build();
	}

	public static Value<String> createUser(boolean optional) {
		return (optional ? Parameter.string().key("User").optional() : Parameter.string().key("User")).completer(new ValueCompleter() {
			@Override
			public List<CommandCompletion> complete(CommandContext context, String currentInput) {
				return Sponge.server().userManager().streamAll().filter(p -> p.name().isPresent() && (currentInput.length() == 0 || p.name().get().startsWith(currentInput))).map(p -> CommandCompletion.of(p.name().get())).collect(Collectors.toList());
			}
		}).build();
	}

	public static Value<String> createUser(String permission, boolean optional) {
		return (optional ? Parameter.string().key("User").optional() : Parameter.string().key("User")).requiredPermission(permission).completer(new ValueCompleter() {
			@Override
			public List<CommandCompletion> complete(CommandContext context, String currentInput) {
				return CommandPack.getInstance().getPlayersData().getTempData().streamUsers().filter(p -> (currentInput.length() == 0 || p.startsWith(currentInput))).map(p -> CommandCompletion.of(p)).collect(Collectors.toList());
			}
		}).build();
	}

	public static Value<Boolean> createBoolean(String key, boolean optional) {
		return (optional ? Parameter.bool().optional() : Parameter.bool()).key(key).build();
	}

	public static Value<Boolean> createBoolean(String key, String permission, boolean optional) {
		return (optional ? Parameter.bool().optional() : Parameter.bool()).key(key).requiredPermission(permission).build();
	}

	public static Value<String> createString(String key, boolean optional) {
		return (optional ? Parameter.string().optional() : Parameter.string()).key(key).build();
	}

	public static Value<String> createStringHome() {
		return Parameter.string().optional().key("Home").completer((context, currentInput) -> getInstance().getPlayersData().getOrCreatePlayerData(context.cause().first(ServerPlayer.class).get()).getHomes().stream().filter(home -> home.getName().contains(currentInput)).map(home -> CommandCompletion.of(home.getName())).toList()).build();
	}

	public static Value<String> createStrings(String key, boolean optional) {
		return (optional ? Parameter.remainingJoinedStrings().optional() : Parameter.remainingJoinedStrings()).key(key).build();
	}

	public static Value<String> createString(String key, String permission, boolean optional) {
		return (optional ? Parameter.string().optional() : Parameter.string()).key(key).requiredPermission(permission).build();
	}

	public static Value<Integer> createInteger(String key, boolean optional) {
		return (optional ? Parameter.integerNumber().optional() : Parameter.integerNumber()).key(key).build();
	}

	public static Value<Integer> createInteger(String key, String permission, boolean optional) {
		return (optional ? Parameter.integerNumber().optional() : Parameter.integerNumber()).key(key).requiredPermission(permission).build();
	}

	public static Value<Integer>  createRangedInteger(String key, int min, int max, boolean optional) {
		Builder<Integer> builder = Parameter.rangedInteger(min, max).key(key);
		if(optional) builder.optional();
		return builder.build();
	}

	public static Value<Integer>  createRangedInteger(String key, String permission, int min, int max, boolean optional) {
		Builder<Integer> builder = Parameter.rangedInteger(min, max).requiredPermission(permission).key(key);
		if(optional) builder.optional();
		return builder.build();
	}

	public static Value<Double> createDouble(String key, boolean optional) {
		return (optional ? Parameter.doubleNumber().optional() : Parameter.doubleNumber()).key(key).build();
	}

	public static Value<Double> createRangedDouble(String key, boolean optional, double min, double max) {
		Builder<Double> builder = Parameter.rangedDouble(min, max).key(key);
		if(optional) builder.optional();
		return builder.build();
	}

	public static Value<Double> createDouble(String key, String permission, boolean optional) {
		return (optional ? Parameter.doubleNumber().optional() : Parameter.doubleNumber()).key(key).requiredPermission(permission).build();
	}

	public static Value<ServerLocation> createLocation(boolean optional) {
		return (optional ? Parameter.location().key("Location").optional() : Parameter.location().key("Location")).build();
	}

	public static Value<String> createInventoryTypes(String permission, boolean optional) {
		return (optional ? Parameter.choices("all", "equipment", "hotbar", "primary", "enderchest").key("InventoryType").optional() : Parameter.choices("all", "equipment", "hotbar", "primary", "enderchest").key("InventoryType")).requiredPermission(permission).build();
	}

	public static Value<String> createInventoryTypes(boolean optional) {
		return (optional ? Parameter.choices("all", "equipment", "hotbar", "primary", "enderchest").key("InventoryType").optional() : Parameter.choices("all", "equipment", "hotbar", "primary", "enderchest").key("InventoryType")).build();
	}

	public static Value<ServerWorld> createWorld(String permission, boolean optional) {
		return (optional ? Parameter.world().key("World").optional() : Parameter.world().key("World")).requiredPermission(permission).build();
	}

	public static Value<ServerWorld> createWorld(boolean optional) {
		return (optional ? Parameter.world().key("World").optional() : Parameter.world().key("World")).build();
	}

	public static Value<Duration> createDuration(boolean optional) {
		return (optional ? Parameter.duration().key("Duration").optional() : Parameter.duration().key("Duration")).build();
	}

	private static CommandPack getInstance() {
		return CommandPack.getInstance();
	}

}