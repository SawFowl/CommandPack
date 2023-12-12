package sawfowl.commandpack.commands.raw.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.registry.RegistryReference;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.world.DefaultWorldKeys;
import org.spongepowered.api.world.WorldType;
import org.spongepowered.api.world.generation.ChunkGenerator;
import org.spongepowered.api.world.generation.config.WorldGenerationConfig;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.api.world.server.WorldTemplate;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Create extends AbstractWorldCommand {

	private Map<String, ChunkGenerator> chunkGenerators = new HashMap<>();
	public Create(CommandPack plugin) {
		super(plugin);
		chunkGenerators.put("overworld", ChunkGenerator.overworld());
		chunkGenerators.put("end", ChunkGenerator.theEnd());
		chunkGenerators.put("nether", ChunkGenerator.theNether());
		Sponge.eventManager().registerListeners(getContainer(), this);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		WorldType worldType = getWorldType(args, 0).get();
		String name = getString(args, 2).get();
		WorldTemplate.Builder builder = (WorldTemplate.builder().displayName(text(name)).generator(chunkGenerators.get(args[1])).gameMode(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().gameMode().asDefaultedReference(RegistryTypes.GAME_MODE)).hardcore(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().hardcore()).difficulty(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().difficulty().asDefaultedReference(RegistryTypes.DIFFICULTY)).performsSpawnLogic(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().performsSpawnLogic()).pvp(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().pvp()).loadOnStartup(true).key(ResourceKey.sponge(TextUtils.clearDecorations(name).toLowerCase())).worldType(RegistryReference.referenced(Sponge.server(), RegistryTypes.WORLD_TYPE, worldType)));
		if(args.length > 3) {
			String seed = getString(args, 3).get();
			boolean features = getBoolean(args, 4).get();
			boolean bonusChest = getBoolean(args, 5).get();
			builder = builder.generationConfig(WorldGenerationConfig.Mutable.builder().seed(seed.hashCode()).generateFeatures(features).generateBonusChest(bonusChest).build());
		}
		WorldTemplate template = ((AbstractBuilder<WorldTemplate>) builder).build();
		Sponge.server().worldManager().loadWorld(template).thenRunAsync(() -> {
			ServerWorld world = Sponge.server().worldManager().world(template.key()).get();
			world.setBorder(world.border().toBuilder().initialDiameter(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().border().diameter()).build());
			audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_CREATE), Placeholders.WORLD, template.key().asString()));
		});
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
		return text("&c/world create <WorldType> <GeneratorType> <Name> [Seed] [GenerateFeatures] [BonusChest]").clickEvent(ClickEvent.suggestCommand("/world create"));
	}

	@Listener(order = Order.LAST)
	public void onServerStarted(sawfowl.commandpack.api.CommandPack.PostAPI event) {
		event.getAPI().getAvailableGenerators().forEach(generator -> {
			chunkGenerators.put(generator.toLowerCase(), plugin.getAPI().getCustomGenerator(generator).get());
		});
		Sponge.eventManager().unregisterListeners(this);
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			RawArguments.createWorldTypeArgument(false, false, 0, LocalesPaths.COMMANDS_EXCEPTION_TYPE_NOT_PRESENT),
			RawArguments.createStringArgument(chunkGenerators.keySet(), false, false, 1, null, LocalesPaths.COMMANDS_EXCEPTION_TYPE_NOT_PRESENT),
			RawArguments.createStringArgument(new ArrayList<>(), false, false, 2, null, LocalesPaths.COMMANDS_EXCEPTION_NAME_NOT_PRESENT),
			RawArguments.createStringArgument(new ArrayList<>(), true, true, 3, "0", LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
			RawArguments.createBooleanArgument(true, true, 4, false, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
			RawArguments.createBooleanArgument(true, true, 5, false, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

}
