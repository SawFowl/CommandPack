package sawfowl.commandpack.commands.parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.util.blockray.RayTrace;
import org.spongepowered.api.util.blockray.RayTraceResult;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

@Register
public class Extinguish extends AbstractParameterizedCommand {

	public Extinguish(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		Optional<ServerPlayer> optTarget = getPlayer(context);
		if(optTarget.isPresent() && isPlayer && !optTarget.get().uniqueId().equals(((ServerPlayer) src).uniqueId())) {
			removeFlame(optTarget.get());
			sendStaffMessage(src, locale, optTarget.get(), LocalesPaths.COMMANDS_EXTINGUISH_SUCCESS_STAFF, LocalesPaths.COMMANDS_EXTINGUISH_SUCCESS);
		} else {
			if(!isPlayer) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT);
			if(context.hasPermission(Permissions.EXTINGUISH_STAFF)) {
				Optional<RayTraceResult<Entity>> optEntity = targetEntity((ServerPlayer) src);
				if(optEntity.isPresent()) {
					Entity target = optEntity.get().selectedObject();
					removeFlame(target);
					boolean ifPlayer = target instanceof ServerPlayer;
					src.sendMessage(getText(locale, LocalesPaths.COMMANDS_EXTINGUISH_SUCCESS_STAFF).replace(Placeholders.PLAYER, ifPlayer ? ((ServerPlayer) target).name() : EntityTypes.registry().valueKey(target.type()).asString()).get());
					if(ifPlayer) ((ServerPlayer) target).sendMessage(getComponent(((ServerPlayer) target).locale(), LocalesPaths.COMMANDS_EXTINGUISH_SUCCESS));
					return;
				}
			}
			delay((ServerPlayer) src, locale, consumer -> {
				removeFlame((Entity) src);
				src.sendMessage(getComponent(locale, LocalesPaths.COMMANDS_EXTINGUISH_SUCCESS));
			});
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.GLOW;
	}

	@Override
	public String command() {
		return "extinguish";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(Permissions.EXTINGUISH_STAFF, true), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT));
	}

	private void removeFlame(Entity target) {
		if(target.get(Keys.FIRE_TICKS).isPresent()) target.remove(Keys.FIRE_TICKS);
		if(target.get(Keys.IS_AFLAME).orElse(false)) target.remove(Keys.IS_AFLAME);
	}

	private void sendStaffMessage(Audience src, Locale staffLocale, ServerPlayer target, Object[] pathStaff, Object[] pathPlayer) {
		src.sendMessage(getText(staffLocale, pathPlayer).replace(Placeholders.PLAYER, target.name()).get());
		target.sendMessage(getComponent(staffLocale, pathPlayer));
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
