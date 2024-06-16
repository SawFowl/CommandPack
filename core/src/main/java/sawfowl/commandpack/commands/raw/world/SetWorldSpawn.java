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

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;

public class SetWorldSpawn extends AbstractWorldCommand {

	public SetWorldSpawn(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		ServerPlayer player = (ServerPlayer) audience;
		player.world().properties().setSpawnPosition(player.blockPosition());
		player.sendMessage(getWorld(locale).getSetSpawn(player.world(), player.blockPosition()));
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
		return "setworldspawn";
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

	@Override
	public Settings getCommandSettings() {
		return Settings.builder().setAliases("setspawn").build();
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
