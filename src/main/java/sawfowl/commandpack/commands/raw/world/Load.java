package sawfowl.commandpack.commands.raw.world;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.world.DefaultWorldKeys;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawArgument;
import sawfowl.commandpack.api.commands.raw.RawCompleterSupplier;
import sawfowl.commandpack.api.commands.raw.RawResultSupplier;
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
		ResourceKey world = getArgument(ResourceKey.class, args, 0).get();
		if(!Sponge.server().worldManager().offlineWorldKeys().stream().map(ResourceKey::asString).filter(k -> k.equals(world.asString())).findFirst().isPresent()) exceptionAppendUsage(cause, TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_LOADED), Placeholders.WORLD, args[0]));
		Sponge.server().worldManager().loadWorld(ResourceKey.resolve(args[0])).thenRunAsync(() -> {
			audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_LOAD), Placeholders.WORLD, args[0]));
		});
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
		return text("&c/world load <UnloadedWorld>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(createWorldArgument());
	}

	private RawArgument<ResourceKey> createWorldArgument() {
		return RawArgument.of(ResourceKey.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(String[] args) {
				return Sponge.server().worldManager().offlineWorldKeys().stream().filter(w -> !w.asString().equals(DefaultWorldKeys.DEFAULT.asString())).map(ResourceKey::asString);
			}
		}, new RawResultSupplier<ResourceKey>() {
			@Override
			public Optional<ResourceKey> get(String[] args) {
				return args.length >= 1 ? Sponge.server().worldManager().offlineWorldKeys().stream().filter(w -> args[0].equals(w.asString()) && !w.asString().equals(DefaultWorldKeys.DEFAULT.asString())).findFirst() : Optional.ofNullable(null);
			}
		}, false, false, 0, LocalesPaths.COMMANDS_EXCEPTION_WORLD_NOT_PRESENT);
	}

}
