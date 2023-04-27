package sawfowl.commandpack.commands.parameterized;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.parameterized.time.Add;
import sawfowl.commandpack.commands.parameterized.time.Day;
import sawfowl.commandpack.commands.parameterized.time.Evening;
import sawfowl.commandpack.commands.parameterized.time.Morning;
import sawfowl.commandpack.commands.parameterized.time.Night;

public class Time extends AbstractParameterizedCommand {

	public Time(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
	}

	@Override
	public Parameterized build() {
		return builder().reset()
				.permission(permission())
				.addChild(new Add(plugin).build(), "add")
				.addChild(new Day(plugin).build(), "day")
				.addChild(new Evening(plugin).build(), "evening")
				.addChild(new Morning(plugin).build(), "morning")
				.addChild(new Night(plugin).build(), "night")
				.build();
	}

	@Override
	public String permission() {
		return Permissions.TIME;
	}

	@Override
	public String command() {
		return "time";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

}
