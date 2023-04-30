package sawfowl.commandpack.commands.raw.onlyplayercommands.kits;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractPlayerCommand;

public class Kits extends AbstractPlayerCommand {

	public Kits(CommandPack plugin) {
		super(plugin);
		getChildExecutors().put("create", new Create(plugin));
		getChildExecutors().put("edit", new Edit(plugin));
		getChildExecutors().put("cooldown", new Cooldown(plugin));
		getChildExecutors().put("setname", new SetName(plugin));
	}

	@Override
	public String permission() {
		return Permissions.KIT_STAFF;
	}

	@Override
	public String command() {
		return "kits";
	}

	@Override
	public void process(CommandCause cause, ServerPlayer src, Locale locale, String[] args, Mutable arguments) throws CommandException {
		src.sendMessage(usage(cause));
	}

	@Override
	public List<CommandCompletion> complete(CommandCause cause, List<String> args, Mutable arguments, String currentInput) throws CommandException {
		return getEmptyCompletion();
	}

	@Override
	public Component shortDescription(Locale locale) {
		return text("Create and edit kits.");
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return text("Create and edit kits.");
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/kits create|edit|setname|cooldown");
	}

}
