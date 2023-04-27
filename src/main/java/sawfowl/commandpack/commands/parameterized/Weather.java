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
import sawfowl.commandpack.commands.parameterized.weather.Rain;
import sawfowl.commandpack.commands.parameterized.weather.Sun;
import sawfowl.commandpack.commands.parameterized.weather.Thunder;

public class Weather extends AbstractParameterizedCommand {

	public Weather(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
	}

	@Override
	public Parameterized build() {
		return builder().reset()
				.permission(permission())
				.addChild(new Sun(plugin).build(), "sun", "clear")
				.addChild(new Rain(plugin).build(), "rain")
				.addChild(new Thunder(plugin).build(), "thunder", "storm")
				.build();
	}

	@Override
	public String permission() {
		return Permissions.WEATHER;
	}

	@Override
	public String command() {
		return "weather";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

}
