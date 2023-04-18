package sawfowl.commandpack.commands.parameterized.time;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.DefaultWorldKeys;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.commands.settings.CommandSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Add extends AbstractParameterizedCommand {

	public Add(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			delay((ServerPlayer) src, locale, consumer -> {
				setTime(src, locale, getArgument(context, ServerWorld.class, "World").orElse(((ServerPlayer) src).world()), getArgument(context, Integer.class, "Value").get());
			});
		} else setTime(src, locale, getArgument(context, ServerWorld.class, "World").orElse(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get()), getArgument(context, Integer.class, "Value").get());
	}

	@Override
	public Parameterized build() {
		return builderNoPerm()
				.executionRequirements(cause -> (!(cause.root() instanceof ServerPlayer) || cause.hasPermission(Permissions.TIME_STAFF) || (cause.hasPermission(permission()) && cause.hasPermission(Permissions.getTimeWorldPermission(command(), ((ServerPlayer) cause.root()).world())))))
				.build();
	}

	@Override
	public String permission() {
		return Permissions.TIME;
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
	return Arrays.asList(
			ParameterSettings.of(CommandParameters.createInteger("Value", false), false, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
			ParameterSettings.of(CommandParameters.createWorld(Permissions.TIME_STAFF, true), false, LocalesPaths.COMMANDS_EXCEPTION_WORLD_NOT_PRESENT)
		);
	}

	@Override
	public String command() {
		return "add";
	}

	@Override
	public String trackingName() {
		return "time";
	}

	@Override
	public CommandSettings getCommandSettings() {
		return CommandSettings.builder().setEnable(false).build();
	}

	private void setTime(Audience src, Locale locale, ServerWorld world, int time) {
		world.properties().setDayTime(world.properties().dayTime().add(Ticks.of(time)));
		src.sendMessage(getText(locale, LocalesPaths.COMMANDS_TIME_NIGHT));
	}

}
