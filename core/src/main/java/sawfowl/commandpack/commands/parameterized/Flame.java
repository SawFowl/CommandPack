package sawfowl.commandpack.commands.parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.util.blockray.RayTrace;
import org.spongepowered.api.util.blockray.RayTraceResult;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Flame extends AbstractParameterizedCommand {

	public Flame(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		Optional<ServerPlayer> optTarget = getPlayer(context);
		boolean damage = getBoolean(context, "Damage", false);
		if(optTarget.isPresent() && isPlayer && !optTarget.get().uniqueId().equals(((ServerPlayer) src).uniqueId())) {
			if(damage) {
				optTarget.get().offer(Keys.FIRE_TICKS, randomTicks());
			} else optTarget.get().offer(Keys.IS_AFLAME, true);
			src.sendMessage(getFlame(locale).getSuccessStaff(optTarget.get(), true));
			optTarget.get().sendMessage(damage ? getFlame(optTarget.get()).getDamage() : getFlame(optTarget.get()).getSuccess());
		} else {
			if(!isPlayer) exception(getExceptions(locale).getPlayerNotPresent());
			if(context.hasPermission(Permissions.FLAME_STAFF)) {
				Optional<RayTraceResult<Entity>> optEntity = targetEntity((ServerPlayer) src);
				if(optEntity.isPresent()) {
					Entity target = optEntity.get().selectedObject();
					if(damage) {
						target.offer(Keys.FIRE_TICKS, randomTicks());
					} else target.offer(Keys.IS_AFLAME, true);
					src.sendMessage(getFlame(locale).getSuccessStaff(target, target instanceof ServerPlayer));
					if(target instanceof ServerPlayer) ((ServerPlayer) target).sendMessage(damage ? getFlame((ServerPlayer) target).getDamage() : getFlame((ServerPlayer) target).getSuccess());
					return;
				}
			}
			delay((ServerPlayer) src, locale, consumer -> {
				if(damage) {
					((ServerPlayer) src).offer(Keys.FIRE_TICKS, randomTicks());
				} else ((ServerPlayer) src).offer(Keys.IS_AFLAME, true);
				src.sendMessage(damage ? getFlame(locale).getDamage() : getFlame(locale).getSuccess());
			});
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.FLAME;
	}

	@Override
	public String command() {
		return "flame";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(
			ParameterSettings.of(CommandParameters.createPlayer(Permissions.FLAME_STAFF, true), false, locale -> getExceptions(locale).getPlayerNotPresent()),
			ParameterSettings.of(CommandParameters.createBoolean("Damage", true), true, locale -> getExceptions(locale).getBooleanNotPresent())
		);
	}

	private Ticks randomTicks() {
		return Ticks.of(ThreadLocalRandom.current().nextLong(20, 2000));
	}

	private Optional<RayTraceResult<Entity>> targetEntity(ServerPlayer source) {
		return RayTrace.entity()
				.world(source.world())
				.sourceEyePosition(source)
				.limit(10)
				.direction(source)
				.execute();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Flame getFlame(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getFlame();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Flame getFlame(ServerPlayer player) {
		return getFlame(player.locale());
	}

}
