package sawfowl.commandpack.commands.parameterized;

import java.util.Locale;
import java.util.stream.Collectors;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class List extends AbstractParameterizedCommand {

	public List(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) src;
			delay(player, locale, consumer -> {
				if(!player.hasPermission(Permissions.LIST_STAFF)) {
					player.sendMessage(plugin.getLocales().getLocale(locale).getCommands().getList().getSuccess(Sponge.server().onlinePlayers().stream().filter(p -> !p.get(Keys.VANISH_STATE).isPresent() || !p.get(Keys.VANISH_STATE).get().invisible()).map(p -> text(p.name())).collect(Collectors.toList())));
				} else src.sendMessage(plugin.getLocales().getLocale(locale).getCommands().getList().getSuccess(Sponge.server().onlinePlayers().stream().map(p -> isVanished(p) ? plugin.getLocales().getLocale(locale).getCommands().getList().getVanished(p) : text(p.name())).collect(Collectors.toList())));
			});
		} else src.sendMessage(plugin.getLocales().getLocale(locale).getCommands().getList().getSuccess(Sponge.server().onlinePlayers().stream().map(p -> isVanished(p) ? plugin.getLocales().getLocale(locale).getCommands().getList().getVanished(p) : text(p.name())).collect(Collectors.toList())));
	}

	private boolean isVanished(ServerPlayer player) {
		return player.get(Keys.VANISH_STATE).isPresent() && player.get(Keys.VANISH_STATE).get().invisible();
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.LIST;
	}

	@Override
	public String command() {
		return "list";
	}

	@Override
	public java.util.List<ParameterSettings> getParameterSettings() {
		return null;
	}

}
