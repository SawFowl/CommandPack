package sawfowl.commandpack.commands.raw.world;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;
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
import org.spongepowered.api.world.DefaultWorldKeys;
import org.spongepowered.api.world.gamerule.GameRules;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class GameRule extends AbstractWorldCommand {

	private Map<String, org.spongepowered.api.world.gamerule.GameRule<?>> gamerules = new HashMap<>();
	public GameRule(CommandPack plugin) {
		super(plugin);
		Sponge.eventManager().registerListeners(getContainer(), this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		if(args.length == 0 || !Sponge.server().worldManager().world(ResourceKey.resolve(args[0])).isPresent()) exceptionAppendUsage(cause, locale, LocalesPaths.COMMANDS_EXCEPTION_WORLD_NOT_PRESENT);
		if(args.length == 1 || !gamerules.containsKey(args[1])) {
			audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_GAMERULES), Placeholders.VALUE, String.join(" ", gamerules.keySet().stream().filter(k -> !k.contains(":")).toArray(String[]::new))));
			return;
		}
		if(args.length == 2) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT);
		ServerWorld world = Sponge.server().worldManager().world(ResourceKey.resolve(args[0])).get();
		org.spongepowered.api.world.gamerule.GameRule<?> rule = gamerules.get(args[1]);
		if(isBooleanType(rule)) {
			org.spongepowered.api.world.gamerule.GameRule<Boolean> boolRule = (org.spongepowered.api.world.gamerule.GameRule<Boolean>) rule;
			boolean value = Boolean.valueOf(args[2]);
			world.properties().setGameRule(boolRule, value);
			audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_GAMERULE_SUCCESS), new String[] {Placeholders.RULE, Placeholders.WORLD, Placeholders.VALUE}, new Object[] {rule.name(), world.key().asString(), value}));
		} else if(isIntType(rule)) {
			if(!NumberUtils.isParsable(args[2])) exception(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_GAMERULE_INCORECT_VALUE), Placeholders.RULE, rule.name()));
			org.spongepowered.api.world.gamerule.GameRule<Integer> intRule = (org.spongepowered.api.world.gamerule.GameRule<Integer>) rule;
			int value = NumberUtils.toInt(args[2]);
			world.properties().setGameRule(intRule, value);
			audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_GAMERULE_SUCCESS), new String[] {Placeholders.RULE, Placeholders.WORLD, Placeholders.VALUE}, new Object[] {rule.name(), world.key().asString(), value}));
		} else if(isLongType(rule)) {
			if(!NumberUtils.isParsable(args[2])) exception(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_GAMERULE_INCORECT_VALUE), Placeholders.RULE, rule.name()));
			org.spongepowered.api.world.gamerule.GameRule<Long> longRule = (org.spongepowered.api.world.gamerule.GameRule<Long>) rule;
			long value = NumberUtils.toLong(args[2]);
			world.properties().setGameRule(longRule, value);
			audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_GAMERULE_SUCCESS), new String[] {Placeholders.RULE, Placeholders.WORLD, Placeholders.VALUE}, new Object[] {rule.name(), world.key().asString(), value}));
		} else if(isDoubleType(rule)) {
			if(!NumberUtils.isParsable(args[2])) exception(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_GAMERULE_INCORECT_VALUE), Placeholders.RULE, rule.name()));
			org.spongepowered.api.world.gamerule.GameRule<Double> doubleRule = (org.spongepowered.api.world.gamerule.GameRule<Double>) rule;
			double value = NumberUtils.toDouble(args[2]);
			world.properties().setGameRule(doubleRule, value);
			audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_GAMERULE_SUCCESS), new String[] {Placeholders.RULE, Placeholders.WORLD, Placeholders.VALUE}, new Object[] {rule.name(), world.key().asString(), value}));
		} else exception(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_GAMERULE_UNKNOWN_TYPE), Placeholders.RULE, rule.name()));
	}

	@Override
	public List<CommandCompletion> complete(CommandCause cause, List<String> args, Mutable arguments, String currentInput) throws CommandException {
		if(!plugin.getMainConfig().isAutoCompleteRawCommands()) return getEmptyCompletion();
		if(args.size() == 0) return Sponge.server().worldManager().worlds().stream().map(ServerWorld::key).map(ResourceKey::asString).filter(k -> !k.equals(DefaultWorldKeys.DEFAULT.asString())).map(CommandCompletion::of).collect(Collectors.toList());
		if(args.size() == 1) {
			if(currentInput.endsWith(" ")) {
				return gamerules.keySet().stream().map(CommandCompletion::of).collect(Collectors.toList());
			} else return Sponge.server().worldManager().worlds().stream().map(ServerWorld::key).map(ResourceKey::asString).filter(k -> (!k.equals(DefaultWorldKeys.DEFAULT.asString()) && (k.split(":")[1].startsWith(args.get(0))) || (args.get(0).contains(k) && !args.get(0).contains(k + " ")))).map(CommandCompletion::of).collect(Collectors.toList());
		}
		if(args.size() == 2 && !currentInput.endsWith(" ")) gamerules.keySet().stream().filter(r -> ((r.contains(":") && r.split(":")[1].startsWith(args.get(0)))) || (args.get(1).contains(r) && !args.get(1).contains(r + " "))).map(CommandCompletion::of).collect(Collectors.toList());
		return getEmptyCompletion();
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
		return text("&c/cworld gamemode <World> <GameRule> <Value>");
	}

	@Listener(order = Order.LAST)
	public void onServerStarted(StartedEngineEvent<Server> event) {
		GameRules.registry().streamEntries().forEach(gameRule -> {
			gamerules.put(gameRule.key().asString(), gameRule.value());
			gamerules.put(gameRule.value().name(), gameRule.value());
		});
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

}