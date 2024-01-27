package sawfowl.commandpack.commands.parameterized;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;

import net.kyori.adventure.audience.Audience;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.parameterized.gamemode.GameModeType;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.configure.locale.LocalesPaths;

@Register
public class GameMode extends AbstractParameterizedCommand {

	public GameMode(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
	}

	@Override
	public Parameterized build() {
		return builder().reset()
				.permission(permission())
				.addChild(new GameModeType(plugin, GameModes.CREATIVE, LocalesPaths.COMMANDS_GAMEMODE_CREATIVE).build(), "creative", "1")
				.addChild(new GameModeType(plugin, GameModes.SPECTATOR, LocalesPaths.COMMANDS_GAMEMODE_SPECTATOR).build(), "spectator", "3")
				.addChild(new GameModeType(plugin, GameModes.SURVIVAL, LocalesPaths.COMMANDS_GAMEMODE_SURVIVAL).build(), "survival", "0")
				.addChild(new GameModeType(plugin, GameModes.ADVENTURE, LocalesPaths.COMMANDS_GAMEMODE_ADVENTURE).build(), "adventure", "2")
				.build();
	}

	@Override
	public String permission() {
		return Permissions.GAMEMODE_STAFF;
	}

	@Override
	public String command() {
		return "gamemode";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

}
