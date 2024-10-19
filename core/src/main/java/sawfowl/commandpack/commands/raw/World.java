package sawfowl.commandpack.commands.raw;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
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
import sawfowl.commandpack.commands.raw.world.SpawnLogic;
import sawfowl.commandpack.commands.raw.world.Teleport;
import sawfowl.commandpack.commands.raw.world.Unload;
import sawfowl.commandpack.commands.raw.world.ViewDistance;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class World extends AbstractWorldCommand {

	private List<RawCommand> childs;
	public World(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		getChildExecutors().forEach((k, v) -> {
			if(v.canExecute(cause)) audience.sendMessage(v.usage(cause).color(NamedTextColor.GREEN));
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

	@Override
	public List<RawCommand> childCommands() {
		return childs != null ? childs : (childs = Arrays.asList(
			new Create(plugin),
			new Delete(plugin),
			new Teleport(plugin),
			new Load(plugin),
			new Unload(plugin),
			new ViewDistance(plugin),
			new Enable(plugin),
			new Disable(plugin),
			new SetWorldSpawn(plugin),
			new SpawnLogic(plugin),
			new SetBorder(plugin),
			new PvP(plugin),
			new Difficulty(plugin),
			new GameMode(plugin),
			new GameRule(plugin),
			new Generate(plugin)
		));
	}
}
