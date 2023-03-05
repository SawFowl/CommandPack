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
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Warp extends AbstractCommand {

	public Warp(CommandPack plugin, String command) {
		super(plugin, command);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(src instanceof ServerPlayer) {
			ServerPlayer player = (ServerPlayer) src;
			String name = getString(context, "Warp", player.name());
			Optional<sawfowl.commandpack.api.data.player.Warp> findAdminWarp = plugin.getPlayersData().getAdminWarp(name);
			if(findAdminWarp.isPresent()) {
				delay(player, locale, consumer -> {
					findAdminWarp.get().moveToThis(player);
					return;
				});
			}
			Optional<sawfowl.commandpack.api.data.player.Warp> findPlayerWarp = plugin.getPlayersData().getWarp(name);
			if(!findPlayerWarp.isPresent()) exception("Варп с таким именем не существует.");
			Optional<sawfowl.commandpack.api.data.player.Warp> optWarp = plugin.getPlayersData().getWarp(name, new Predicate<sawfowl.commandpack.api.data.player.Warp>() {
				@Override
				public boolean test(sawfowl.commandpack.api.data.player.Warp t) {
					return !t.isPrivate() || plugin.getPlayersData().getOrCreatePlayerData(player).containsWarp(t.getName());
				}
			});
			if(!optWarp.isPresent()) exception("Вы не можете телепортироваться на этот варп");
			delay(player, locale, consumer -> {
				optWarp.get().moveToThis(player);
				return;
			});
		}
		// Закончить тут и дописать установку админских варпов. Создать конфиг для админских варпов.
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
					new ParameterSettings(CommandParameters.createPlayer(Permissions.WARP_STAFF, false), false, LocalesPaths.COMMANDS_EXCEPTION_NAME_NOT_PRESENT)
				);
	}

}
