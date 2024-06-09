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
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.util.blockray.RayTrace;
import org.spongepowered.api.util.blockray.RayTraceResult;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Glow extends AbstractParameterizedCommand {

	public Glow(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		Optional<ServerPlayer> optTarget = getPlayer(context);
		if(optTarget.isPresent() && (!isPlayer || !optTarget.get().uniqueId().equals(((ServerPlayer) src).uniqueId()))) {
			if(setGlow(optTarget.get())) {
				sendStaffMessage(src, optTarget.get(), getGlow(locale).getEnableStaff(optTarget.get(), true), getGlow(optTarget.get()).getEnable());
			} else sendStaffMessage(src, optTarget.get(), getGlow(locale).getDisableStaff(optTarget.get(), true), getGlow(optTarget.get()).getDisable());
		} else {
			if(!isPlayer) exception(getExceptions(locale).getPlayerNotPresent());
			if(context.hasPermission(Permissions.GLOW_STAFF)) {
				Optional<RayTraceResult<Entity>> optEntity = targetEntity((ServerPlayer) src);
				if(optEntity.isPresent()) {
					Entity target = optEntity.get().selectedObject();
					boolean glow = setGlow(target);
					boolean ifPlayer = target instanceof ServerPlayer;
					src.sendMessage(glow ? getGlow(locale).getEnableStaff(target, ifPlayer) : getGlow(locale).getDisableStaff(target, ifPlayer));
					if(ifPlayer) ((ServerPlayer) target).sendMessage(glow ? getGlow((ServerPlayer) target).getEnable() : getGlow((ServerPlayer) target).getDisable());
					return;
				}
			}
			delay((ServerPlayer) src, locale, consumer -> {
				if(setGlow((ServerPlayer) src)) {
					src.sendMessage(getGlow(locale).getEnable());
				} else src.sendMessage(getGlow(locale).getDisable());
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
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(Permissions.GLOW_STAFF, true), false, locale -> getExceptions(locale).getPlayerNotPresent()));
	}

	private boolean setGlow(Entity target) {
		boolean newValue = !isGlow(target);
		target.offer(Keys.IS_GLOWING, newValue);
		return isGlow(target);
	}

	private boolean isGlow(Entity target) {
		return target.get(Keys.IS_GLOWING).orElse(false);
	}

	private void sendStaffMessage(Audience src, ServerPlayer target, Component staff, Component player) {
		src.sendMessage(staff);
		target.sendMessage(player);
	}

	private Optional<RayTraceResult<Entity>> targetEntity(ServerPlayer source) {
		return RayTrace.entity()
				.world(source.world())
				.sourceEyePosition(source)
				.limit(10)
				.direction(source)
				.execute();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Glow getGlow(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getGlow();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Glow getGlow(ServerPlayer player) {
		return getGlow(player.locale());
	}

}
