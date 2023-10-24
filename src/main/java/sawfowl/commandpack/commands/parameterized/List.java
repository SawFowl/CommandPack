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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

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
					java.util.List<Component> list = Sponge.server().onlinePlayers().stream().filter(p -> !p.get(Keys.VANISH_STATE).isPresent() || !p.get(Keys.VANISH_STATE).get().invisible()).map(p -> text(p.name())).collect(Collectors.toList());
					player.sendMessage(getText(locale, LocalesPaths.COMMANDS_LIST_SUCCESS).replace(Placeholders.VALUE, String.valueOf(list.size())).append(Component.join(JoinConfiguration.separator(text(", ")), list)).get());
				} else src.sendMessage(getText(locale, LocalesPaths.COMMANDS_LIST_SUCCESS).replace(Placeholders.VALUE, Sponge.server().onlinePlayers().size()).get().append(Component.join(JoinConfiguration.separator(text(", ")), Sponge.server().onlinePlayers().stream().map(p -> p.get(Keys.VANISH_STATE).isPresent() && p.get(Keys.VANISH_STATE).get().invisible() ? getText(locale, LocalesPaths.COMMANDS_LIST_VANISHED).replace(Placeholders.PLAYER, p.name()).get() : text(p.name())).collect(Collectors.toList()))));
			});
		} else src.sendMessage(getText(locale, LocalesPaths.COMMANDS_LIST_SUCCESS).replace(Placeholders.VALUE, Sponge.server().onlinePlayers().size()).get().append(Component.join(JoinConfiguration.separator(text(", ")), Sponge.server().onlinePlayers().stream().map(p -> p.get(Keys.VANISH_STATE).isPresent() && p.get(Keys.VANISH_STATE).get().invisible() ? getText(locale, LocalesPaths.COMMANDS_LIST_VANISHED).replace(Placeholders.PLAYER, p.name()).get() : text(p.name())).collect(Collectors.toList()))));
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
