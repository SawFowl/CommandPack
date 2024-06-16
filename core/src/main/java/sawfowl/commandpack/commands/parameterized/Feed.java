package sawfowl.commandpack.commands.parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Feed extends AbstractParameterizedCommand {

	public Feed(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			Optional<ServerPlayer> target = getPlayer(context);
			if(target.isPresent() && !target.get().uniqueId().equals(((ServerPlayer) src).uniqueId())) {
				target.get().offer(Keys.FOOD_LEVEL, target.get().getOrElse(Keys.MAX_FOOD_LEVEL, 20));
				src.sendMessage(getFeed(locale).getSuccessStaff(target.get()));
				target.get().sendMessage(getFeed(target.get()).getSuccess());
			} else {
				((ServerPlayer) src).offer(Keys.FOOD_LEVEL, ((ServerPlayer) src).getOrElse(Keys.MAX_FOOD_LEVEL, 20));
				src.sendMessage(getFeed(locale).getSuccess());
			}
		} else {
			ServerPlayer player = getPlayer(context).get();
			player.offer(Keys.FOOD_LEVEL, player.getOrElse(Keys.MAX_FOOD_LEVEL, 20));
			src.sendMessage(getFeed(locale).getSuccessStaff(player));
			player.sendMessage(getFeed(player).getSuccess());
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.FEED;
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(Permissions.FEED_STAFF, true), false, locale -> getExceptions(locale).getPlayerNotPresent()));
	}

	@Override
	public String command() {
		return "feed";
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Feed getFeed(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getFeed();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Feed getFeed(ServerPlayer player) {
		return getFeed(player.locale());
	}

}
