package sawfowl.commandpack.commands.raw.world;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.command.registrar.tree.CommandTreeNodeTypes;
import org.spongepowered.api.world.gamerule.GameRules;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentData;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.commands.raw.arguments.RawBasicArgumentData;
import sawfowl.commandpack.api.commands.raw.arguments.RawOptional;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;

public class GameRule extends AbstractWorldCommand {

	private Map<String, org.spongepowered.api.world.gamerule.GameRule<?>> gamerules = new HashMap<>();
	public GameRule(CommandPack plugin) {
		super(plugin);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		if(args.getInput().length == 0 || !Sponge.server().worldManager().world(ResourceKey.resolve(args.getInput()[0])).isPresent()) exceptionAppendUsage(cause, getExceptions(locale).getWorldNotPresent(), getArguments().get(0).getTreeKey());
		if(args.getInput().length == 1 || !gamerules.containsKey(args.getInput()[1])) {
			audience.sendMessage(getWorld(locale).getGameRule().getList(String.join(", ", gamerules.keySet().stream().filter(k -> !k.contains(":")).toArray(String[]::new))));
			return;
		}
		if(args.getInput().length == 2) exception(getExceptions(locale).getValueNotPresent());
		ServerWorld world = args.getWorld(0).get();
		org.spongepowered.api.world.gamerule.GameRule<?> rule = gamerules.get(args.getString(1).get());
		String stringValueArg = args.getString(2).get();
		if(isBooleanType(rule)) {
			if(BooleanUtils.toBooleanObject(args.getInput()[2]) == null) exception(getExceptions(locale).getValueNotPresent());
			org.spongepowered.api.world.gamerule.GameRule<Boolean> boolRule = (org.spongepowered.api.world.gamerule.GameRule<Boolean>) rule;
			boolean value = BooleanUtils.toBooleanObject(args.getInput()[2]);
			world.properties().setGameRule(boolRule, value);
			audience.sendMessage(getWorld(locale).getGameRule().getSuccess(rule.name(), world, value));
		} else if(isIntType(rule)) {
			if(!NumberUtils.isParsable(stringValueArg)) exception(getWorld(locale).getGameRule().getIncorrectValue(rule.name()));
			org.spongepowered.api.world.gamerule.GameRule<Integer> intRule = (org.spongepowered.api.world.gamerule.GameRule<Integer>) rule;
			int value = NumberUtils.toInt(stringValueArg);
			world.properties().setGameRule(intRule, value);
			audience.sendMessage(getWorld(locale).getGameRule().getSuccess(rule.name(), world, value));
		} else if(isLongType(rule)) {
			if(!NumberUtils.isParsable(stringValueArg)) exception(getWorld(locale).getGameRule().getIncorrectValue(rule.name()));
			org.spongepowered.api.world.gamerule.GameRule<Long> longRule = (org.spongepowered.api.world.gamerule.GameRule<Long>) rule;
			long value = NumberUtils.toLong(stringValueArg);
			world.properties().setGameRule(longRule, value);
			audience.sendMessage(getWorld(locale).getGameRule().getSuccess(rule.name(), world, value));
		} else if(isDoubleType(rule)) {
			if(!NumberUtils.isParsable(stringValueArg)) exception(getWorld(locale).getGameRule().getIncorrectValue(rule.name()));
			org.spongepowered.api.world.gamerule.GameRule<Double> doubleRule = (org.spongepowered.api.world.gamerule.GameRule<Double>) rule;
			double value = NumberUtils.toDouble(stringValueArg);
			world.properties().setGameRule(doubleRule, value);
			audience.sendMessage(getWorld(locale).getGameRule().getSuccess(rule.name(), world, value));
		} else exception(getWorld(locale).getGameRule().getUnknownType(rule.name()));
	}

	@Override
	public Component shortDescription(Locale locale) {
		return null;
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return null;
	}

	@Override
	public String command() {
		return "gamerule";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/world gamerule <World> <GameRule> <Value>").clickEvent(ClickEvent.suggestCommand("/world gamerule"));
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			createWorldArg(),
			RawArguments.createStringArgument(gamerules.keySet().isEmpty() ? fillMap() : gamerules.keySet(), new RawBasicArgumentData<String>(null, "GameRule", 1, null, null), RawOptional.optional(), locale -> getExceptions(locale).getTypeNotPresent()),
			createValueArg()
		);
	}

	private Set<String> fillMap() {
		GameRules.registry().streamEntries().forEach(gameRule -> {
			gamerules.put(gameRule.key().asString(), gameRule.value());
			gamerules.put(gameRule.value().name(), gameRule.value());
		});
		return gamerules.keySet();
	}

	private boolean isBooleanType(org.spongepowered.api.world.gamerule.GameRule<?> gameRule) {
		return Boolean.class.isInstance(gameRule.defaultValue()) || gameRule.defaultValue() instanceof Boolean || gameRule.valueType().getTypeName().toLowerCase().contains("boolean");
	}

	private boolean isIntType(org.spongepowered.api.world.gamerule.GameRule<?> gameRule) {
		return Integer.class.isInstance(gameRule.defaultValue()) || gameRule.defaultValue() instanceof Integer || gameRule.valueType().getTypeName().toLowerCase().contains("integer");
	}

	private boolean isLongType(org.spongepowered.api.world.gamerule.GameRule<?> gameRule) {
		return Long.class.isInstance(gameRule.defaultValue()) || gameRule.defaultValue() instanceof Long || gameRule.valueType().getTypeName().toLowerCase().contains("long");
	}

	private boolean isDoubleType(org.spongepowered.api.world.gamerule.GameRule<?> gameRule) {
		return Double.class.isInstance(gameRule.defaultValue()) || gameRule.defaultValue() instanceof Double || gameRule.valueType().getTypeName().toLowerCase().contains("double");
	}

	private RawArgument<String> createValueArg() {
		return RawArgument.of(
			String.class,
			(cause, args) -> args.length >= 2 && gamerules.containsKey(args[1]) && isBooleanType(gamerules.get(args[1])) ? Stream.of("true", "false") : RawArguments.EMPTY.stream(),
			(cause, args) -> args.length > 2 && gamerules.containsKey(args[1]) ? Optional.ofNullable(args[2]) : Optional.empty(),
			new RawArgumentData<>("Value", CommandTreeNodeTypes.BOOL.get().createNode(), 2, null, null),
			RawOptional.notOptional(),
			locale -> getExceptions(locale).getBooleanNotPresent()
		);
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
