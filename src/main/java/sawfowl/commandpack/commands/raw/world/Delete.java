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
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.DefaultWorldKeys;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Delete extends AbstractWorldCommand {

	public Delete(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		if(args.length == 0 || !args[0].startsWith("sponge") || !Sponge.server().worldManager().world(ResourceKey.resolve(args[0])).isPresent()) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_WORLD_NOT_PRESENT);
		for(ServerPlayer player : Sponge.server().onlinePlayers()) if(player.world().key().asString().equalsIgnoreCase(args[0])) player.setLocation(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().location(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get().properties().spawnPosition()));
		Sponge.server().worldManager().deleteWorld(ResourceKey.resolve(args[0])).thenRunAsync(() -> {
			audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_DELETE), Placeholders.WORLD, args[0]));
		});
	}

	@Override
	public List<CommandCompletion> complete(CommandCause cause, List<String> args, Mutable arguments, String currentInput) throws CommandException {
		if(args.size() == 0) return Sponge.server().worldManager().worldKeys().stream().map(ResourceKey::asString).filter(k -> k.startsWith("sponge")).map(CommandCompletion::of).collect(Collectors.toList());
		if(args.size() == 1 && !currentInput.endsWith(" ")) return Sponge.server().worldManager().worldKeys().stream().map(ResourceKey::asString).filter(k -> k.startsWith("sponge") && args.get(0).startsWith(k)).map(CommandCompletion::of).collect(Collectors.toList());
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
		return "delete";
	}

	@Override
	public Component usage(CommandCause cause) {
		return null;
	}

}