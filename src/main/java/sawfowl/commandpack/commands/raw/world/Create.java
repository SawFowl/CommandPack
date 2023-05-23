package sawfowl.commandpack.commands.raw.world;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.registry.RegistryReference;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.world.DefaultWorldKeys;
import org.spongepowered.api.world.WorldType;
import org.spongepowered.api.world.WorldTypes;
import org.spongepowered.api.world.generation.ChunkGenerator;
import org.spongepowered.api.world.generation.config.WorldGenerationConfig;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.api.world.server.WorldTemplate;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Create extends AbstractWorldCommand {

	private List<String> worldTypes = null;
	private Map<String, ChunkGenerator> chunkGenerators = new HashMap<>();
	private List<String> bools = Arrays.asList("true", "false");
	public Create(CommandPack plugin) {
		super(plugin);
		chunkGenerators.put("overworld", ChunkGenerator.overworld());
		chunkGenerators.put("end", ChunkGenerator.theEnd());
		chunkGenerators.put("nether", ChunkGenerator.theNether());
		Sponge.eventManager().registerListeners(getContainer(), this);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		if(args.length < 2) exceptionAppendUsage(cause, locale, LocalesPaths.COMMANDS_EXCEPTION_TYPE_NOT_PRESENT);
		Optional<WorldType> optType = WorldTypes.registry().findValue(ResourceKey.resolve(args[0]));
		if(!optType.isPresent() || !chunkGenerators.containsKey(args[1])) exceptionAppendUsage(cause, locale, LocalesPaths.COMMANDS_EXCEPTION_TYPE_NOT_PRESENT);
		if(args.length == 2) exceptionAppendUsage(cause, locale, LocalesPaths.COMMANDS_EXCEPTION_NAME_NOT_PRESENT);
		String name = args[2];
		WorldType type = optType.get();
		WorldTemplate.Builder builder = (WorldTemplate.builder().displayName(text(name)).generator(chunkGenerators.get(args[1])).gameMode(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().gameMode().asDefaultedReference(RegistryTypes.GAME_MODE)).hardcore(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().hardcore()).difficulty(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().difficulty().asDefaultedReference(RegistryTypes.DIFFICULTY)).performsSpawnLogic(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().performsSpawnLogic()).pvp(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().pvp()).loadOnStartup(true).key(ResourceKey.sponge(TextUtils.clearDecorations(name).toLowerCase())).worldType(RegistryReference.referenced(Sponge.server(), RegistryTypes.WORLD_TYPE, type)));
		if(args.length > 3) {
			String seed = args[3];
			boolean features = args.length > 4 ? Boolean.valueOf(args[4]) : false;
			boolean bonusChest = args.length > 5 ? Boolean.valueOf(args[5]) : false;
			builder.generationConfig(WorldGenerationConfig.Mutable.builder().seed(seed.hashCode()).generateFeatures(features).generateBonusChest(bonusChest).build());
		}
		WorldTemplate template = ((AbstractBuilder<WorldTemplate>) builder).build();
		Sponge.server().worldManager().loadWorld(template).thenRunAsync(() -> {
			ServerWorld world = Sponge.server().worldManager().world(template.key()).get();
			world.setBorder(world.border().toBuilder().initialDiameter(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().border().diameter()).build());
			audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_CREATE), Placeholders.WORLD, template.key().asString()));
		});
	}

	@Override
	public List<CommandCompletion> complete(CommandCause cause, List<String> args, Mutable arguments, String currentInput) throws CommandException {
		if(!plugin.getMainConfig().isAutoCompleteRawCommands()) return getEmptyCompletion();
		if(worldTypes == null) worldTypes = WorldTypes.registry().streamEntries().map(e -> (e.key().asString())).collect(Collectors.toList());
		if((args.isEmpty() || args.size() == 1) && !currentInput.endsWith(" ")) return worldTypes.stream().filter(t -> (currentInput.length() == 0 || t.startsWith(currentInput) || (t.contains(":") && !t.endsWith(":") && t.split(":")[1].startsWith(currentInput)) || (currentInput.contains(t) && !currentInput.contains(t + " ")))).map(t -> CommandCompletion.of(t, text("&3WorldType"))).collect(Collectors.toList());
		if(args.size() == 1 && currentInput.endsWith(" ")) return chunkGenerators.keySet().stream().map(CommandCompletion::of).collect(Collectors.toList());
		if(args.size() == 2 && !currentInput.endsWith(" ")) return chunkGenerators.keySet().stream().filter(g -> g.startsWith(args.get(1))).map(CommandCompletion::of).collect(Collectors.toList());
		if(args.size() == 4 && currentInput.endsWith(" ")) return Arrays.asList(CommandCompletion.of("true", text("&3GenerateFeatures")), CommandCompletion.of("false", text("&3GenerateFeatures")));
		if(args.size() == 5) {
			if(currentInput.endsWith(" ")) {
				return Arrays.asList(CommandCompletion.of("true", text("&3BonusChest")), CommandCompletion.of("false", text("&3BonusChest")));
			} else return bools.stream().filter(b -> b.startsWith(args.get(4))).map(CommandCompletion::of).collect(Collectors.toList());
		}
		if(args.size() == 6 && !bools.contains(args.get(5)) && !currentInput.endsWith(" ")) return bools.stream().filter(b -> b.startsWith(args.get(5))).map(CommandCompletion::of).collect(Collectors.toList());
		return getEmptyCompletion();
	}

	@Override
	public Component shortDescription(Locale locale) {
		return text("&3Create world");
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return text("&3Create world");
	}

	@Override
	public String command() {
		return "create";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/world create <WorldType> <GeneratorType> <Name> [Seed] [GenerateFeatures] [BonusChest]");
	}

	@Listener(order = Order.LAST)
	public void onServerStarted(StartedEngineEvent<Server> event) {
		plugin.getAPI().getAvailableGenerators().forEach(generator -> {
			chunkGenerators.put(generator.toLowerCase(), plugin.getAPI().getCustomGenerator(generator).get());
		});
		Sponge.eventManager().unregisterListeners(this);
	}

}
