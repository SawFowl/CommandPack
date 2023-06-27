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
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Glow extends AbstractParameterizedCommand {

	public Glow(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		Optional<ServerPlayer> optTarget = getPlayer(context);
		if(optTarget.isPresent() && isPlayer && !optTarget.get().uniqueId().equals(((ServerPlayer) src).uniqueId())) {
			if(setGlow(optTarget.get())) {
				sendStaffMessage(src, locale, optTarget.get(), LocalesPaths.COMMANDS_GLOW_ENABLE_STAFF, LocalesPaths.COMMANDS_GLOW_ENABLE);
			} else sendStaffMessage(src, locale, optTarget.get(), LocalesPaths.COMMANDS_GLOW_DISABLE_STAFF, LocalesPaths.COMMANDS_GLOW_DISABLE);
		} else {
			if(!isPlayer) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT);
			if(context.hasPermission(Permissions.GLOW_STAFF)) {
				Optional<RayTraceResult<Entity>> optEntity = targetEntity((ServerPlayer) src);
				if(optEntity.isPresent()) {
					Entity target = optEntity.get().selectedObject();
					boolean glow = setGlow(target);
					boolean ifPlayer = target instanceof ServerPlayer;
					src.sendMessage(TextUtils.replace(getText(locale, glow ? LocalesPaths.COMMANDS_GLOW_ENABLE_STAFF : LocalesPaths.COMMANDS_GLOW_DISABLE_STAFF), Placeholders.PLAYER, ifPlayer ? ((ServerPlayer) target).name() : EntityTypes.registry().valueKey(target.type()).asString()));
					if(ifPlayer) ((ServerPlayer) target).sendMessage(getText(((ServerPlayer) target).locale(), glow ? LocalesPaths.COMMANDS_GLOW_ENABLE : LocalesPaths.COMMANDS_GLOW_DISABLE));
					return;
				}
			}
			delay((ServerPlayer) src, locale, consumer -> {
				if(setGlow((ServerPlayer) src)) {
					src.sendMessage(getText(locale, LocalesPaths.COMMANDS_GLOW_ENABLE));
				} else src.sendMessage(getText(locale, LocalesPaths.COMMANDS_GLOW_DISABLE));
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
		return "glow";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(Permissions.GLOW_STAFF, true), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT));
	}

	private boolean setGlow(Entity target) {
		boolean newValue = !isGlow(target);
		target.offer(Keys.IS_GLOWING, newValue);
		return isGlow(target);
	}

	private boolean isGlow(Entity target) {
		return target.get(Keys.IS_GLOWING).orElse(false);
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
