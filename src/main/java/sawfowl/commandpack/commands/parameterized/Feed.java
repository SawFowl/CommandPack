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

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Feed extends AbstractParameterizedCommand {

	public Feed(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			Optional<ServerPlayer> target = getPlayer(context);
			if(target.isPresent() && !target.get().uniqueId().equals(((ServerPlayer) src).uniqueId())) {
				target.get().offer(Keys.FOOD_LEVEL, target.get().getOrElse(Keys.MAX_FOOD_LEVEL, 20));
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_FEED_OTHER).replace(Placeholders.PLAYER, target.get().name()).get());
				target.get().sendMessage(getComponent(target.get(), LocalesPaths.COMMANDS_FEED_SELF));
			} else {
				((ServerPlayer) src).offer(Keys.FOOD_LEVEL, ((ServerPlayer) src).getOrElse(Keys.MAX_FOOD_LEVEL, 20));
				src.sendMessage(getComponent(locale, LocalesPaths.COMMANDS_FEED_SELF));
			}
		} else {
			ServerPlayer player = getPlayer(context).get();
			player.offer(Keys.FOOD_LEVEL, player.getOrElse(Keys.MAX_FOOD_LEVEL, 20));
			src.sendMessage(getText(locale, LocalesPaths.COMMANDS_FEED_OTHER).replace(Placeholders.PLAYER, player.name()).get());
			player.sendMessage(getComponent(player, LocalesPaths.COMMANDS_FEED_SELF));
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
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(Permissions.FEED_STAFF, true), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT));
	}

	@Override
	public String command() {
		return "feed";
	}

}
