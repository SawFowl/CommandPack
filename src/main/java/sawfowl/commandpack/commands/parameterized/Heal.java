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
import sawfowl.localeapi.api.TextUtils;

public class Heal extends AbstractParameterizedCommand {

	public Heal(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			Optional<ServerPlayer> target = getPlayer(context);
			if(target.isPresent() && !target.get().uniqueId().equals(((ServerPlayer) src).uniqueId())) {
				target.get().offer(Keys.HEALTH, target.get().getOrElse(Keys.MAX_HEALTH, 20d));
				src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_HEAL_OTHER), Placeholders.PLAYER, target.get().name()));
				target.get().sendMessage(getText(target.get(), LocalesPaths.COMMANDS_HEAL_SELF));
			} else {
				((ServerPlayer) src).offer(Keys.HEALTH, ((ServerPlayer) src).getOrElse(Keys.MAX_HEALTH, 20d));
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_HEAL_SELF));
			}
		} else {
			ServerPlayer player = getPlayer(context).get();
			player.offer(Keys.HEALTH, player.getOrElse(Keys.MAX_HEALTH, 20d));
			src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_HEAL_OTHER), Placeholders.PLAYER, player.name()));
			player.sendMessage(getText(player, LocalesPaths.COMMANDS_HEAL_SELF));
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.HEAL;
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(Permissions.HEAL_STAFF, true), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT));
	}

	@Override
	public String command() {
		return "heal";
	}

}
