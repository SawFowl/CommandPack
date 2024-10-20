package sawfowl.commandpack.commands.raw.world;

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
import org.spongepowered.api.world.generation.ChunkGenerator;
import org.spongepowered.api.world.generation.config.WorldGenerationConfig;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.api.world.server.WorldTemplate;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.commands.raw.arguments.RawBasicArgumentData;
import sawfowl.commandpack.api.commands.raw.arguments.RawOptional;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.utils.CommandsUtil;
import sawfowl.localeapi.api.TextUtils;

public class Create extends AbstractWorldCommand {

	public Create(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		WorldType worldType = args.<WorldType>get(0).get();
		String name = args.getString(2).get();
		WorldTemplate.Builder builder = (WorldTemplate.builder().key(ResourceKey.sponge(TextUtils.clearDecorations(name).toLowerCase()))
				.add(Keys.CHUNK_GENERATOR, args.<ChunkGenerator>get(1).get())
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
			audience.sendMessage(getCommands(locale).getWorld().getCreate(template.key().asString()));
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
			RawArguments.createWorldTypeArgument(RawBasicArgumentData.createWorldType(0, null, null), RawOptional.notOptional(), locale -> getExceptions(locale).getTypeNotPresent()),
			RawArguments.createChunkGenerator(RawBasicArgumentData.createChunkGenerator(1, null, null), RawOptional.notOptional(), locale -> getExceptions(locale).getTypeNotPresent()),
			RawArguments.createStringArgument(CommandsUtil.getEmptyList(), new RawBasicArgumentData<>(null, "Name", 2, null, null), RawOptional.notOptional(), locale -> getExceptions(locale).getNameNotPresent()),
			RawArguments.createStringArgument(CommandsUtil.getEmptyList(), new RawBasicArgumentData<>(null, "Seed", 3, null, null), RawOptional.optional(), locale -> getExceptions(locale).getValueNotPresent()),
			RawArguments.createBooleanArgument(new RawBasicArgumentData<>(null, "Structures", 4, null, null), RawOptional.optional(), locale -> getExceptions(locale).getBooleanNotPresent()),
			RawArguments.createBooleanArgument(new RawBasicArgumentData<>(null, "BonusChest", 5, null, null), RawOptional.optional(), locale -> getExceptions(locale).getBooleanNotPresent())
		);
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
