package sawfowl.commandpack.commands.raw.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
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
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Create extends AbstractWorldCommand {

	public Create(CommandPack plugin) {
		super(plugin);
		Sponge.eventManager().registerListeners(getContainer(), this);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		WorldType worldType = getWorldType(args, 0).get();
		String name = getString(args, 2).get();
		WorldTemplate.Builder builder = (WorldTemplate.builder().key(ResourceKey.sponge(TextUtils.clearDecorations(name).toLowerCase()))
				.add(Keys.CHUNK_GENERATOR, plugin.getAPI().getCustomGenerator(args[1]).get())
				.add(Keys.GAME_MODE, Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().gameMode())
				.add(Keys.HARDCORE, Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().hardcore())
				.add(Keys.WORLD_DIFFICULTY, Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().difficulty())
				.add(Keys.PERFORM_SPAWN_LOGIC, Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().performsSpawnLogic())
				.add(Keys.PVP, Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().pvp()).add(Keys.IS_LOAD_ON_STARTUP, true)
				.add(Keys.WORLD_TYPE, worldType));
		if(args.length > 3) {
			String seed = getString(args, 3).get();
			boolean structures = getBoolean(args, 4).get();
			boolean bonusChest = getBoolean(args, 5).get();
			builder = builder.add(Keys.WORLD_GEN_CONFIG, WorldGenerationConfig.builder().seed(seed.hashCode()).generateStructures(structures).generateBonusChest(bonusChest).build());
		}
		WorldTemplate template = ((AbstractBuilder<WorldTemplate>) builder).build();
		Sponge.server().worldManager().loadWorld(template).thenRunAsync(() -> {
			ServerWorld world = Sponge.server().worldManager().world(template.key()).get();
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
		return text("&c/world create <WorldType> <GeneratorType> <Name> [Seed] [GenerateFeatures] [BonusChest]").clickEvent(ClickEvent.suggestCommand("/world create"));
	}

	@Listener(order = Order.LAST)
	public void onServerStarted(sawfowl.commandpack.api.CommandPack.PostAPI event) {
		Sponge.eventManager().unregisterListeners(this);
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			RawArguments.createWorldTypeArgument(false, false, 0, LocalesPaths.COMMANDS_EXCEPTION_TYPE_NOT_PRESENT),
			RawArguments.createStringArgument(plugin.getAPI().getAvailableGenerators(), false, false, 1, null, LocalesPaths.COMMANDS_EXCEPTION_TYPE_NOT_PRESENT),
			RawArguments.createStringArgument(new ArrayList<>(), false, false, 2, null, LocalesPaths.COMMANDS_EXCEPTION_NAME_NOT_PRESENT),
			RawArguments.createStringArgument(new ArrayList<>(), true, true, 3, "0", LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
			RawArguments.createBooleanArgument(true, true, 4, false, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
			RawArguments.createBooleanArgument(true, true, 5, false, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

}
