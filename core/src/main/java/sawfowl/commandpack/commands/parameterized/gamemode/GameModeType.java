package sawfowl.commandpack.commands.parameterized.gamemode;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.registry.DefaultedRegistryReference;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

@Register
public class GameModeType extends AbstractParameterizedCommand {

	private DefaultedRegistryReference<GameMode> gameMode;
	private String name;
	private Object[] path;

	public GameModeType(CommandPack plugin) {
		super(plugin);
	}

	public GameModeType(CommandPack plugin, DefaultedRegistryReference<GameMode> gameMode, Object[] path) {
		super(plugin);
		this.gameMode = gameMode;
		name = gameMode.location().value();
		this.path = path;
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			ServerPlayer player = getPlayer(context).orElse((ServerPlayer) src);
			player.offer(Keys.GAME_MODE, gameMode.get());
			if(!player.uniqueId().equals(((ServerPlayer) src).uniqueId())) {
				player.sendMessage(getText(player, LocalesPaths.COMMANDS_GAMEMODE_SUCCESS).replace(Placeholders.VALUE, getComponent(locale, path)).get());
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_GAMEMODE_OTHER).replace(new String[] {Placeholders.PLAYER, Placeholders.VALUE}, Component.text(player.name()), getComponent(locale, path)).get());
			} else src.sendMessage(getText(locale, LocalesPaths.COMMANDS_GAMEMODE_SUCCESS).replace(Placeholders.VALUE, getText(locale, path)).get());
		} else {
			ServerPlayer target = getPlayer(context).get();
			target.offer(Keys.GAME_MODE, gameMode.get());
			target.sendMessage(getText(target, LocalesPaths.COMMANDS_GAMEMODE_SUCCESS).replace(Placeholders.VALUE, getComponent(locale, path)).get());
			src.sendMessage(getText(locale, LocalesPaths.COMMANDS_GAMEMODE_OTHER).replace(new String[] {Placeholders.PLAYER, Placeholders.VALUE}, Component.text(target.name()), getComponent(locale, path)).get());
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.getGameModePermission(name);
	}

	@Override
	public String command() {
		return name;
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(Permissions.GAMEMODE_OTHER_STAFF, true), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT));
	}

	@Override
	public Settings getCommandSettings() {
		return Settings.builder().setEnable(false).build();
	}

}
