package sawfowl.commandpack.commands.parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.commands.CommandParameters;
import sawfowl.commandpack.commands.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractCommand;
import sawfowl.commandpack.configure.configs.commands.CommandSettings;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Warp extends AbstractCommand {

	public Warp(CommandPack plugin, String command, CommandSettings commandSettings) {
		super(plugin, command, commandSettings);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(src instanceof ServerPlayer) {
			ServerPlayer player = context.cause().hasPermission(Permissions.WARP_STAFF) && getPlayer(context).isPresent() ? getPlayer(context).get() : (ServerPlayer) src;
			String name = getString(context, "Warp", player.name());
			Optional<sawfowl.commandpack.api.data.player.Warp> findAdminWarp = plugin.getPlayersData().getAdminWarp(name);
			if(!context.cause().hasPermission(Permissions.getWarpPermission(name))) exception("Варп с таким именем не существует или не доступен вам.");
			if(findAdminWarp.isPresent()) {
				delay(player, locale, consumer -> {
					findAdminWarp.get().moveToThis(player);
				});
				return;
			}
			Optional<sawfowl.commandpack.api.data.player.Warp> findPlayerWarp = plugin.getPlayersData().getWarp(name);
			if(!findPlayerWarp.isPresent()) exception("Варп с таким именем не существует или не доступен вам.");
			Optional<sawfowl.commandpack.api.data.player.Warp> optWarp = plugin.getPlayersData().getWarp(name, new Predicate<sawfowl.commandpack.api.data.player.Warp>() {
				@Override
				public boolean test(sawfowl.commandpack.api.data.player.Warp warp) {
					return !warp.isPrivate() || context.cause().hasPermission(Permissions.WARP_STAFF) || plugin.getPlayersData().getOrCreatePlayerData(player).containsWarp(warp.getName());
				}
			});
			if(!optWarp.isPresent()) exception("Варп с таким именем не существует или не доступен вам.");
			delay(player, locale, consumer -> {
				optWarp.get().moveToThis(player);
			});
		} else {
			Optional<String> optName = getString(context, "Warp");
			if(!optName.isPresent()) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_NAME_NOT_PRESENT);
			Optional<ServerPlayer> optPlayer = getPlayer(context, src, false);
			if(!optPlayer.isPresent()) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT);
			String name = optName.get();
			Optional<sawfowl.commandpack.api.data.player.Warp> findAdminWarp = plugin.getPlayersData().getAdminWarp(name);
			Optional<sawfowl.commandpack.api.data.player.Warp> findPlayerWarp = plugin.getPlayersData().getWarp(name);
			if(!findAdminWarp.isPresent() && !findPlayerWarp.isPresent()) exception("Варпа с таким именем не существует");
			ServerPlayer player = optPlayer.get();
			sawfowl.commandpack.api.data.player.Warp warp = findAdminWarp.isPresent() ? findAdminWarp.get() : findPlayerWarp.get();
			warp.moveToThis(player);
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.WARP;
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(
					new ParameterSettings(CommandParameters.createString("Warp", false), false, LocalesPaths.COMMANDS_EXCEPTION_NAME_NOT_PRESENT),
					new ParameterSettings(CommandParameters.createPlayer(Permissions.WARP_STAFF, true), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT)
				);
	}

}
