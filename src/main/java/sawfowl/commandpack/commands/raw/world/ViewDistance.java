package sawfowl.commandpack.commands.raw.world;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class ViewDistance extends AbstractWorldCommand {

	public ViewDistance(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		if(args.length == 0 || !Sponge.server().worldManager().world(ResourceKey.resolve(args[0])).isPresent()) exceptionAppendUsage(cause, locale, LocalesPaths.COMMANDS_EXCEPTION_WORLD_NOT_PRESENT);
		if(args.length == 1 || !NumberUtils.isParsable(args[0])) exceptionAppendUsage(cause, locale, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT);
		ServerWorld world = Sponge.server().worldManager().world(ResourceKey.resolve(args[0])).get();
		int distance = NumberUtils.toInt(args[1]);
		if(distance < 1) exceptionAppendUsage(cause, locale, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT);
		world.properties().setViewDistance(distance);
		audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_VIEWDISTANCE), new String[] {Placeholders.WORLD, Placeholders.VALUE}, new Object[] {world.key().asString(), distance}));
	}

	@Override
	public List<CommandCompletion> complete(CommandCause cause, List<String> args, Mutable arguments, String currentInput) throws CommandException {
		if(!plugin.getMainConfig().isAutoCompleteRawCommands()) return getEmptyCompletion();
		if(args.size() == 0) return Sponge.server().worldManager().worlds().stream().map(ServerWorld::key).map(ResourceKey::asString).map(CommandCompletion::of).collect(Collectors.toList());
		if(args.size() == 1 && !currentInput.endsWith(" ")) return Sponge.server().worldManager().worlds().stream().map(ServerWorld::key).map(ResourceKey::asString).filter(k -> (k.split(":")[1].startsWith(args.get(0))) || (args.get(0).contains(k) && !args.get(0).contains(k + " "))).map(CommandCompletion::of).collect(Collectors.toList());
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
		return "setborder";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/cworld viewdistance <World> <Distance>");
	}

}
