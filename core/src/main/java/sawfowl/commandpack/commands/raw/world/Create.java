package sawfowl.commandpack.commands.raw.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.math.NumberUtils;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.world.DefaultWorldKeys;
import org.spongepowered.api.world.WorldType;
import org.spongepowered.api.world.generation.config.WorldGenerationConfig;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.api.world.server.WorldTemplate;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Create extends AbstractWorldCommand {

	public Create(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		WorldType worldType = args.<WorldType>get(0).get();
		String name = args.getString(2).get();
		WorldTemplate.Builder builder = (WorldTemplate.builder().key(ResourceKey.sponge(TextUtils.clearDecorations(name).toLowerCase()))
				.add(Keys.CHUNK_GENERATOR, plugin.getAPI().getCustomGenerator(args.getString(1).get()).get())
				.add(Keys.GAME_MODE, Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().gameMode())
				.add(Keys.HARDCORE, Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().hardcore())
				.add(Keys.WORLD_DIFFICULTY, Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().difficulty())
				.add(Keys.PERFORM_SPAWN_LOGIC, Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().performsSpawnLogic())
				.add(Keys.PVP, Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().pvp()).add(Keys.IS_LOAD_ON_STARTUP, true)
				.add(Keys.WORLD_TYPE, worldType));
		if(args.getInput().length > 3) {
			String seed = args.getString(3).get();
			builder = builder.add(Keys.SEED, NumberUtils.isCreatable(seed) ? NumberUtils.createLong(seed) : (long) seed.hashCode());
			boolean structures = args.getBoolean(4).get();
			boolean bonusChest = args.getBoolean(5).get();
			// need test
			builder = builder.add(Keys.WORLD_GEN_CONFIG, WorldGenerationConfig.builder().seed(seed.hashCode()).generateStructures(structures).generateBonusChest(bonusChest).build());
		}
		WorldTemplate template = ((AbstractBuilder<WorldTemplate>) builder).build();
		Sponge.server().worldManager().loadWorld(template).thenRunAsync(() -> {
			ServerWorld world = Sponge.server().worldManager().world(template.key()).get();
			if(args.getInput().length > 3) {
				boolean structures = args.getBoolean(4).get();
				boolean bonusChest = args.getBoolean(5).get();
				world.properties().offer(Keys.WORLD_GEN_CONFIG, WorldGenerationConfig.builder().from(world.properties().worldGenerationConfig()).generateStructures(structures).generateBonusChest(bonusChest).build());
			}
			world.setBorder(world.border().toBuilder().initialDiameter(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().border().diameter()).build());
			audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_WORLD_CREATE).replace(Placeholders.WORLD, template.key().asString()).get());
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
		return text("&c/world create <WorldType> <ChunkGenerator> <Name> [Seed] [Structures] [BonusChest]").clickEvent(ClickEvent.suggestCommand("/world create"));
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			RawArguments.createWorldTypeArgument(false, false, 0, null, LocalesPaths.COMMANDS_EXCEPTION_TYPE_NOT_PRESENT),
			RawArguments.createStringArgument("ChunkGenerator", plugin.getAPI().getAvailableGenerators(), false, false, 1, null, null, LocalesPaths.COMMANDS_EXCEPTION_TYPE_NOT_PRESENT),
			RawArguments.createStringArgument("Name", new ArrayList<>(), false, false, 2, null, null, LocalesPaths.COMMANDS_EXCEPTION_NAME_NOT_PRESENT),
			RawArguments.createStringArgument("Seed", new ArrayList<>(), true, true, 3, "0", null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
			RawArguments.createBooleanArgument("Structures", true, true, 4, false, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
			RawArguments.createBooleanArgument("BonusChest", true, true, 5, false, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
