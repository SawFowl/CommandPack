package sawfowl.commandpack.commands.raw;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.commands.raw.world.Create;
import sawfowl.commandpack.commands.raw.world.Delete;
import sawfowl.commandpack.commands.raw.world.Difficulty;
import sawfowl.commandpack.commands.raw.world.Disable;
import sawfowl.commandpack.commands.raw.world.Enable;
import sawfowl.commandpack.commands.raw.world.Generate;
import sawfowl.commandpack.commands.raw.world.GameMode;
import sawfowl.commandpack.commands.raw.world.GameRule;
import sawfowl.commandpack.commands.raw.world.Load;
import sawfowl.commandpack.commands.raw.world.PvP;
import sawfowl.commandpack.commands.raw.world.SetBorder;
import sawfowl.commandpack.commands.raw.world.SetWorldSpawn;
import sawfowl.commandpack.commands.raw.world.Teleport;
import sawfowl.commandpack.commands.raw.world.Unload;
import sawfowl.commandpack.commands.raw.world.ViewDistance;

public class World extends AbstractWorldCommand {

	public World(CommandPack plugin) {
		super(plugin);
		getChildExecutors().put("create", new Create(plugin));
		getChildExecutors().put("delete", new Delete(plugin));
		getChildExecutors().put("teleport", new Teleport(plugin));
		getChildExecutors().put("tp", getChildExecutors().get("teleport"));
		getChildExecutors().put("load", new Load(plugin));
		getChildExecutors().put("unload", new Unload(plugin));
		getChildExecutors().put("viewdistance", new ViewDistance(plugin));
		getChildExecutors().put("enable", new Enable(plugin));
		getChildExecutors().put("disable", new Disable(plugin));
		getChildExecutors().put("setspawn", new SetWorldSpawn(plugin));
		getChildExecutors().put("setborder", new SetBorder(plugin));
		getChildExecutors().put("pvp", new PvP(plugin));
		getChildExecutors().put("difficulty", new Difficulty(plugin));
		getChildExecutors().put("gamemode", new GameMode(plugin));
		getChildExecutors().put("gamerule", new GameRule(plugin));
		getChildExecutors().put("generate", new Generate(plugin));
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		audience.sendMessage(usage(cause));
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
		return "world";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/world " + String.join("|", getChildExecutors().keySet().toArray(new String[]{})));
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return null;
	}
}
