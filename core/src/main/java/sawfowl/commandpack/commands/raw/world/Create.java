package sawfowl.commandpack.commands.raw.world;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

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
import org.spongepowered.api.world.server.WorldArchetype;
import org.spongepowered.api.world.server.WorldArchetypeType;
import org.spongepowered.api.world.server.storage.ServerWorldProperties.LoadOptions;

import net.kyori.adventure.audience.Audience;
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
		Sponge.server().worldManager().loadWorld(
			ResourceKey.sponge(TextUtils.clearDecorations(name).toLowerCase()),
			LoadOptions
				.builder()
				.create(
					WorldArchetype.of(
						WorldArchetypeType
						.builder()
						.worldType(worldType)
						.chunkGenerator(
							args.<ChunkGenerator>get(1).get()
						)
						.build()
					)
				)
			.createCallback(p -> p.offer(Keys.SEED, args.getString(3).map(s -> (long) s.hashCode()).orElse(ThreadLocalRandom.current().nextLong())))
			.build()
		)
		.thenAccept(optWorld -> {
			optWorld.ifPresent(world -> {
				if(args.getInput().length > 3) {
					boolean structures = args.getBoolean(4).get();
					boolean bonusChest = args.getBoolean(5).orElse(false);
					world.properties().offer(Keys.WORLD_GEN_CONFIG, WorldGenerationConfig.builder().from(world.properties().worldGenerationConfig()).generateStructures(structures).generateBonusChest(bonusChest).build());
				}
				world.setBorder(world.border().toBuilder().initialDiameter(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().border().diameter()).build());
				audience.sendMessage(getCommands(locale).getWorld().getCreate(world.key().asString()));
			});
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