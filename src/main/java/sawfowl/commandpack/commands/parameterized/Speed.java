package sawfowl.commandpack.commands.parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Speed extends AbstractParameterizedCommand {

	public Speed(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		double multiplier = getArgument(context, Integer.class, "Speed").get();
		Optional<ServerPlayer> optTarget = getPlayer(context);
		if(isPlayer) {
			ServerPlayer source = (ServerPlayer) src;
			if(optTarget.isPresent()) {
				ServerPlayer target = optTarget.get();
				boolean fly = isFlying(target);
				setSpeed(target, multiplier, fly);
				Component staff = TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_SPEED_STAFF), new String[] {Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {text(target.name()), multiplier == 1 ? getText(locale, LocalesPaths.COMMANDS_SPEED_DEFAULT) : text((int) multiplier)});
				Component other = TextUtils.replace(getText(target, LocalesPaths.COMMANDS_SPEED_OTHER), Placeholders.VALUE, multiplier == 1 ? getText(locale, LocalesPaths.COMMANDS_SPEED_DEFAULT) : text((int) multiplier));
				if(fly) {
					staff = staff.append(getText(locale, LocalesPaths.COMMANDS_SPEED_FLY));
					other = other.append(getText(target, LocalesPaths.COMMANDS_SPEED_FLY));
				}
				src.sendMessage(staff);
				target.sendMessage(other);
			} else {
				boolean fly = isFlying(source);
				if(!source.hasPermission(Permissions.SPEED_STAFF)) {
					double limit = fly ? Permissions.getSpeedFlyLimit(source) : Permissions.getSpeedLimit(source);
					if(limit < multiplier) multiplier = limit;
				}
				final double mult = multiplier;
				delay(source, locale, consumer -> {
					setSpeed(source, mult, fly);
					Component text = TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SPEED_SELF), Placeholders.VALUE, mult == 1 ? getText(locale, LocalesPaths.COMMANDS_SPEED_DEFAULT) : text((int) mult));
					if(fly) text = text.append(getText(locale, LocalesPaths.COMMANDS_SPEED_FLY));
					source.sendMessage(text);
				});
			}
		} else {
			ServerPlayer target = optTarget.get();
			boolean fly = isFlying(target);
			setSpeed(target, multiplier, fly);
			Component staff = TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_SPEED_STAFF), new String[] {Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {text(target.name()), multiplier == 1 ? getText(locale, LocalesPaths.COMMANDS_SPEED_DEFAULT) : text((int) multiplier)});
			Component other = TextUtils.replace(getText(target, LocalesPaths.COMMANDS_SPEED_OTHER), Placeholders.VALUE, multiplier == 1 ? getText(locale, LocalesPaths.COMMANDS_SPEED_DEFAULT) : text((int) multiplier));
			if(fly) {
				staff = staff.append(getText(locale, LocalesPaths.COMMANDS_SPEED_FLY));
				other = other.append(getText(target, LocalesPaths.COMMANDS_SPEED_FLY));
			}
			src.sendMessage(staff);
			target.sendMessage(other);
		}
	}

	@Override
	public Parameterized build() {
		return builder()
				.reset()
				.addParameters(getParameterSettings().stream().map(ParameterSettings::getParameterUnknownType).toArray(Value[]::new))
				.executionRequirements(cause -> (cause.hasPermission(Permissions.SPEED) || cause.hasPermission(Permissions.SPEED_FLY)))
				.executor(this)
				.build();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(
			ParameterSettings.of(CommandParameters.createRangedInteger("Speed", 0, 5, false), false, LocalesPaths.COMMANDS_EXCEPTION_TYPE_NOT_PRESENT),
			ParameterSettings.of(CommandParameters.createPlayer(Permissions.SPEED_STAFF, true), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT)
		);
	}

	@Override
	public String command() {
		return "speed";
	}

	@Override
	public String permission() {
		return Permissions.SPEED;
	}

	private void setSpeed(ServerPlayer player, double multiplier, boolean fly) {
		if(fly) {
			player.offer(Keys.FLYING_SPEED, multiplier * 0.05000000074505806);
		} else {
			player.offer(Keys.WALKING_SPEED, multiplier * 0.10000000149011612);
		}
	}

	private boolean isFlying(ServerPlayer player) {
		return player.get(Keys.CAN_FLY).orElse(false) && player.get(Keys.IS_FLYING).orElse(false);
	}

}
