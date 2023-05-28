package sawfowl.commandpack.commands.raw.world;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.world.gamerule.GameRules;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawArgument;
import sawfowl.commandpack.api.commands.raw.RawArguments;
import sawfowl.commandpack.api.commands.raw.RawCompleterSupplier;
import sawfowl.commandpack.api.commands.raw.RawResultSupplier;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class GameRule extends AbstractWorldCommand {

	private Map<String, org.spongepowered.api.world.gamerule.GameRule<?>> gamerules = new HashMap<>();
	private List<String> bools = Arrays.asList("true", "false");
	public GameRule(CommandPack plugin) {
		super(plugin);
		Sponge.eventManager().registerListeners(getContainer(), this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		if(args.length == 0 || !Sponge.server().worldManager().world(ResourceKey.resolve(args[0])).isPresent()) exceptionAppendUsage(cause, locale, LocalesPaths.COMMANDS_EXCEPTION_WORLD_NOT_PRESENT);
		if(args.length == 1 || !gamerules.containsKey(args[1])) {
			audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_GAMERULES), Placeholders.VALUE, String.join(", ", gamerules.keySet().stream().filter(k -> !k.contains(":")).toArray(String[]::new))));
			return;
		}
		if(args.length == 2) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT);
		ServerWorld world = getWorld(args, 0).get();
		org.spongepowered.api.world.gamerule.GameRule<?> rule = gamerules.get(getString(args, 1).get());
		String stringValueArg = getString(args, 2).get();
		if(isBooleanType(rule)) {
			org.spongepowered.api.world.gamerule.GameRule<Boolean> boolRule = (org.spongepowered.api.world.gamerule.GameRule<Boolean>) rule;
			boolean value = Boolean.valueOf(stringValueArg);
			world.properties().setGameRule(boolRule, value);
			audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_GAMERULE_SUCCESS), new String[] {Placeholders.RULE, Placeholders.WORLD, Placeholders.VALUE}, new Object[] {rule.name(), world.key().asString(), value}));
		} else if(isIntType(rule)) {
			if(!NumberUtils.isParsable(stringValueArg)) exception(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_GAMERULE_INCORECT_VALUE), Placeholders.RULE, rule.name()));
			org.spongepowered.api.world.gamerule.GameRule<Integer> intRule = (org.spongepowered.api.world.gamerule.GameRule<Integer>) rule;
			int value = NumberUtils.toInt(stringValueArg);
			world.properties().setGameRule(intRule, value);
			audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_GAMERULE_SUCCESS), new String[] {Placeholders.RULE, Placeholders.WORLD, Placeholders.VALUE}, new Object[] {rule.name(), world.key().asString(), value}));
		} else if(isLongType(rule)) {
			if(!NumberUtils.isParsable(stringValueArg)) exception(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_GAMERULE_INCORECT_VALUE), Placeholders.RULE, rule.name()));
			org.spongepowered.api.world.gamerule.GameRule<Long> longRule = (org.spongepowered.api.world.gamerule.GameRule<Long>) rule;
			long value = NumberUtils.toLong(stringValueArg);
			world.properties().setGameRule(longRule, value);
			audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_GAMERULE_SUCCESS), new String[] {Placeholders.RULE, Placeholders.WORLD, Placeholders.VALUE}, new Object[] {rule.name(), world.key().asString(), value}));
		} else if(isDoubleType(rule)) {
			if(!NumberUtils.isParsable(stringValueArg)) exception(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_GAMERULE_INCORECT_VALUE), Placeholders.RULE, rule.name()));
			org.spongepowered.api.world.gamerule.GameRule<Double> doubleRule = (org.spongepowered.api.world.gamerule.GameRule<Double>) rule;
			double value = NumberUtils.toDouble(stringValueArg);
			world.properties().setGameRule(doubleRule, value);
			audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_GAMERULE_SUCCESS), new String[] {Placeholders.RULE, Placeholders.WORLD, Placeholders.VALUE}, new Object[] {rule.name(), world.key().asString(), value}));
		} else exception(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_GAMERULE_UNKNOWN_TYPE), Placeholders.RULE, rule.name()));
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
		return text("&c/world gamerule <World> <GameRule> <Value>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			createWorldArg(),
			RawArguments.createStringArgument(gamerules.keySet(), true, true, 1, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
			createValueArg()
		);
	}

	@Listener(order = Order.LAST)
	public void onServerStarted(StartedEngineEvent<Server> event) {
		GameRules.registry().streamEntries().forEach(gameRule -> {
			gamerules.put(gameRule.key().asString(), gameRule.value());
			gamerules.put(gameRule.value().name(), gameRule.value());
		});
		getArguments();
		Sponge.eventManager().unregisterListeners(this);
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
		return RawArgument.of(String.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(String[] args) {
				return args.length >= 2 && gamerules.containsKey(args[1]) && isBooleanType(gamerules.get(args[1])) ? bools.stream() : RawArguments.EMPTY.stream();
			}
		}, new RawResultSupplier<String>() {
			@Override
			public Optional<String> get(String[] args) {
				return args.length > 2 && gamerules.containsKey(args[1]) && isBooleanType(gamerules.get(args[1])) && BooleanUtils.toBooleanObject(args[2]) != null ? Optional.ofNullable(String.valueOf(BooleanUtils.toBooleanObject(args[2]))) : Optional.empty();
			}
		}, true, true, 2, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT);
	}

}
