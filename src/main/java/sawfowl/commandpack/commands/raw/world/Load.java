package sawfowl.commandpack.commands.raw.world;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.world.DefaultWorldKeys;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Load extends AbstractWorldCommand {

	public Load(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		if(args.length == 0 || DefaultWorldKeys.DEFAULT.asString().equals(args[0])) exceptionAppendUsage(cause, locale, LocalesPaths.COMMANDS_EXCEPTION_WORLD_NOT_PRESENT);
		if(!Sponge.server().worldManager().offlineWorldKeys().stream().map(ResourceKey::asString).filter(k -> k.equals(args[0])).findFirst().isPresent()) exceptionAppendUsage(cause, TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_LOADED), Placeholders.WORLD, args[0]));
		Sponge.server().worldManager().loadWorld(ResourceKey.resolve(args[0])).thenRunAsync(() -> {
			audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_LOAD), Placeholders.WORLD, args[0]));
		});
	}

	@Override
	public List<CommandCompletion> complete(CommandCause cause, List<String> args, Mutable arguments, String currentInput) throws CommandException {
		if(!plugin.getMainConfig().isAutoCompleteRawCommands()) return getEmptyCompletion();
		if(args.size() == 0) return Sponge.server().worldManager().offlineWorldKeys().stream().map(ResourceKey::asString).filter(k -> !k.equals(DefaultWorldKeys.DEFAULT.asString())).map(CommandCompletion::of).collect(Collectors.toList());
		if(args.size() == 1 && !currentInput.endsWith(" ")) return Sponge.server().worldManager().offlineWorldKeys().stream().map(ResourceKey::asString).filter(k -> (!k.equals(DefaultWorldKeys.DEFAULT.asString()) && (k.split(":")[1].startsWith(args.get(0))) || (args.get(0).contains(k) && !args.get(0).contains(k + " ")))).map(CommandCompletion::of).collect(Collectors.toList());
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
		return "load";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/world load <World>");
	}

}
