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

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.localeapi.api.ComponentSupplier;

@Register
public class GameModeType extends AbstractParameterizedCommand {

	private DefaultedRegistryReference<GameMode> gameMode;
	private String name;
	private ComponentSupplier supplier;

	public GameModeType(CommandPackInstance plugin) {
		super(plugin);
	}

	public GameModeType(CommandPackInstance plugin, DefaultedRegistryReference<GameMode> gameMode, ComponentSupplier supplier) {
		super(plugin);
		this.gameMode = gameMode;
		name = gameMode.location().value();
		this.supplier = supplier;
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			ServerPlayer player = getPlayer(context).orElse((ServerPlayer) src);
			player.offer(Keys.GAME_MODE, gameMode.get());
			if(!player.uniqueId().equals(((ServerPlayer) src).uniqueId())) {
				player.sendMessage(getGameMode(player).getSuccess(supplier.get(player.locale())));
				src.sendMessage(getGameMode(locale).getSuccessStaff(player, supplier.get(locale)));
			} else src.sendMessage(getGameMode(locale).getSuccess(supplier.get(locale)));
		} else {
			ServerPlayer target = getPlayer(context).get();
			target.offer(Keys.GAME_MODE, gameMode.get());
			target.sendMessage(getGameMode(target).getSuccess(supplier.get(target.locale())));
			src.sendMessage(getGameMode(locale).getSuccessStaff(target, supplier.get(locale)));
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
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(Permissions.GAMEMODE_OTHER_STAFF, true), false, locale -> plugin.getLocales().getLocale(locale).getCommandExceptions().getPlayerNotPresent()));
	}

	@Override
	public Settings getCommandSettings() {
		return Settings.builder().setEnable(false).build();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.GameMode getGameMode(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getGameMode();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.GameMode getGameMode(ServerPlayer player) {
		return getGameMode(player.locale());
	}

}
