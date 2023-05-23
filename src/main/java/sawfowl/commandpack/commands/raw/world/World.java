package sawfowl.commandpack.commands.raw.world;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;

public class World extends AbstractWorldCommand {

	public World(CommandPack plugin) {
		super(plugin);
		getChildExecutors().put("create", new Create(plugin));
		getChildExecutors().put("delete", new Delete(plugin));
		getChildExecutors().put("teleport", new Teleport(plugin));
		getChildExecutors().put("tp", getChildExecutors().get("teleport"));
		getChildExecutors().put("load", new Load(plugin));
		getChildExecutors().put("unload", new Unload(plugin));
		getChildExecutors().put("enable", new Enable(plugin));
		getChildExecutors().put("disable", new Disable(plugin));
		getChildExecutors().put("setspawn", new SetWorldSpawn(plugin));
		getChildExecutors().put("setborder", new SetBorder(plugin));
		getChildExecutors().put("pvp", new PvP(plugin));
		getChildExecutors().put("difficulty", new Difficulty(plugin));
		getChildExecutors().put("gamemode", new GameMode(plugin));
		getChildExecutors().put("gamerule", new GameRule(plugin));
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
		return text("&c/world create|delete|teleport|load|unload|enable|disable|setspawn|setborder|pvp|difficulty|gamemode|gamerule");
	}
}
