package sawfowl.commandpack.commands.raw.world;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawArgument;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class SetWorldSpawn extends AbstractWorldCommand {

	public SetWorldSpawn(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		ServerPlayer player = (ServerPlayer) audience;
		player.world().properties().setSpawnPosition(player.blockPosition());
		player.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_SETSPAWN), new String[] {Placeholders.WORLD, Placeholders.LOCATION}, new Object[] {player.world().key().asString(), player.blockPosition().toString()}));
	}

	@Override
	public List<CommandCompletion> complete(CommandCause cause, List<String> args, Mutable arguments, String currentInput) throws CommandException {
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
		return null;
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/cworld setspawn");
	}

	@Override
	public boolean canExecute(CommandCause cause) {
		return cause.hasPermission(permission()) && cause.audience() instanceof ServerPlayer;
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return null;
	}

}
