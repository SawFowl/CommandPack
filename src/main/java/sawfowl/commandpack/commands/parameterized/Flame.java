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
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.util.blockray.RayTrace;
import org.spongepowered.api.util.blockray.RayTraceResult;

import net.kyori.adventure.audience.Audience;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Flame extends AbstractParameterizedCommand {

	public Flame(CommandPack plugin) {
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
			sendStaffMessage(src, locale, optTarget.get(), LocalesPaths.COMMANDS_FLAME_SUCCESS_STAFF, damage ? LocalesPaths.COMMANDS_FLAME_SUCCESS_DAMAGE : LocalesPaths.COMMANDS_FLAME_SUCCESS);
		} else {
			if(!isPlayer) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT);
			if(context.hasPermission(Permissions.FLAME_STAFF)) {
				Optional<RayTraceResult<Entity>> optEntity = targetEntity((ServerPlayer) src);
				if(optEntity.isPresent()) {
					Entity target = optEntity.get().selectedObject();
					if(damage) {
						target.offer(Keys.FIRE_TICKS, randomTicks());
					} else target.offer(Keys.IS_AFLAME, true);
					src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_FLAME_SUCCESS_STAFF), Placeholders.PLAYER, target instanceof ServerPlayer ? ((ServerPlayer) target).name() : EntityTypes.registry().valueKey(target.type()).asString()));
					if(target instanceof ServerPlayer) ((ServerPlayer) target).sendMessage(getText(((ServerPlayer) target).locale(), damage ? LocalesPaths.COMMANDS_FLAME_SUCCESS_DAMAGE : LocalesPaths.COMMANDS_FLAME_SUCCESS));
					return;
				}
			}
			delay((ServerPlayer) src, locale, consumer -> {
				if(damage) {
					((ServerPlayer) src).offer(Keys.FIRE_TICKS, randomTicks());
				} else ((ServerPlayer) src).offer(Keys.IS_AFLAME, true);
				src.sendMessage(getText(locale, damage ? LocalesPaths.COMMANDS_FLAME_SUCCESS_DAMAGE : LocalesPaths.COMMANDS_FLAME_SUCCESS));
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
			ParameterSettings.of(CommandParameters.createPlayer(Permissions.FLAME_STAFF, true), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT),
			ParameterSettings.of(CommandParameters.createBoolean("Damage", true), true, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

	private Ticks randomTicks() {
		return Ticks.of(ThreadLocalRandom.current().nextLong(20, 2000));
	}

	private void sendStaffMessage(Audience src, Locale staffLocale, ServerPlayer target, Object[] pathStaff, Object[] pathPlayer) {
		src.sendMessage(TextUtils.replace(getText(staffLocale, pathPlayer), Placeholders.PLAYER, target.name()));
		target.sendMessage(getText(staffLocale, pathPlayer));
	}

	private Optional<RayTraceResult<Entity>> targetEntity(ServerPlayer source) {
		return RayTrace.entity()
				.world(source.world())
				.sourceEyePosition(source)
				.limit(10)
				.direction(source)
				.execute();
	}

}
