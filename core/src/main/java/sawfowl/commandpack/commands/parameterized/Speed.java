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

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Speed extends AbstractParameterizedCommand {

	public Speed(CommandPackInstance plugin) {
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
				Component staff = getSpeed(locale).getSetByStaff(target, (int) multiplier);
				Component other = getSpeed(target).getSetOther((int) multiplier);
				if(fly) {
					staff = staff.append(getSpeed(locale).getInFly());
					other = other.append(getSpeed(target).getInFly());
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
					Component text = getSpeed(locale).getSetSelf((int) mult);
					if(fly) text = text.append(getSpeed(locale).getInFly());
					source.sendMessage(text);
				});
			}
		} else {
			ServerPlayer target = optTarget.get();
			boolean fly = isFlying(target);
			setSpeed(target, multiplier, fly);
			Component staff = getSpeed(locale).getSetByStaff(target, (int) multiplier);
			Component other = getSpeed(target).getSetOther((int) multiplier);
			if(fly) {
				staff = staff.append(getSpeed(locale).getInFly());
				other = other.append(getSpeed(target).getInFly());
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
			ParameterSettings.of(CommandParameters.createRangedInteger("Speed", 0, 5, false), false, locale -> getExceptions(locale).getValueNotPresent()),
			ParameterSettings.of(CommandParameters.createPlayer(Permissions.SPEED_STAFF, true), false, locale -> getExceptions(locale).getPlayerNotPresent())
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

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Speed getSpeed(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getSpeed();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Speed getSpeed(ServerPlayer player) {
		return getSpeed(player.locale());
	}

}
