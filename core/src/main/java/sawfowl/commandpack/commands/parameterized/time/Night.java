package sawfowl.commandpack.commands.parameterized.time;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.util.MinecraftDayTime;
import org.spongepowered.api.world.DefaultWorldKeys;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Night extends AbstractParameterizedCommand {

	public Night(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			delay((ServerPlayer) src, locale, consumer -> {
				setTime(src, locale, getArgument(context, ServerWorld.class, "World").orElse(((ServerPlayer) src).world()));
			});
		} else setTime(src, locale, getArgument(context, ServerWorld.class, "World").orElse(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get()));
	}

	@Override
	public Parameterized build() {
		return builderNoPerm()
				.executionRequirements(cause -> (!(cause.root() instanceof ServerPlayer) || cause.hasPermission(Permissions.TIME_STAFF) || (cause.hasPermission(Permissions.TIME) && cause.hasPermission(permission()) && cause.hasPermission(Permissions.getTimeWorldPermission(command(), ((ServerPlayer) cause.root()).world())))))
				.build();
	}

	@Override
	public String permission() {
		return Permissions.getTimePermission(command());
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createWorld(Permissions.TIME_STAFF, true), true, locale -> getExceptions(locale).getWorldNotPresent()));
	}

	@Override
	public String command() {
		return "night";
	}

	@Override
	public String trackingName() {
		return "time";
	}

	private void setTime(Audience src, Locale locale, ServerWorld world) {
		world.properties().setDayTime(MinecraftDayTime.of(world.properties().gameTime().day() + 1, 0, 0));
		src.sendMessage(plugin.getLocales().getLocale(locale).getCommands().getTime().getNight(world));
	}

}
