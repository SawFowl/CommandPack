package sawfowl.commandpack.commands.raw.world;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class SetWorldSpawn extends AbstractWorldCommand {

	public SetWorldSpawn(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		ServerPlayer player = (ServerPlayer) audience;
		player.world().properties().setSpawnPosition(player.blockPosition());
		player.sendMessage(getText(locale, LocalesPaths.COMMANDS_WORLD_SETSPAWN).replace(new String[] {Placeholders.WORLD, Placeholders.LOCATION}, new Object[] {player.world().key().asString(), player.blockPosition().toString()}).get());
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
		return text("&c/world setspawn").clickEvent(ClickEvent.suggestCommand("/world setspawn"));
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
