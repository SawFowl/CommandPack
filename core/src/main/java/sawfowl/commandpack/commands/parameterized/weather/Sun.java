package sawfowl.commandpack.commands.parameterized.weather;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.DefaultWorldKeys;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.api.world.weather.WeatherTypes;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Sun extends AbstractParameterizedCommand {

	private Random random;
	public Sun(CommandPackInstance plugin) {
		super(plugin);
		random = new Random();
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			delay((ServerPlayer) src, locale, consumer -> {
				setWeather(src, locale, getArgument(context, ServerWorld.class, "World").orElse(((ServerPlayer) src).world()), getArgument(context, Integer.class, "Duration"));
			});
		} else setWeather(src, locale, getArgument(context, ServerWorld.class, "World").orElse(Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).get()), getArgument(context, Integer.class, "Duration"));
	}

	@Override
	public Parameterized build() {
		return builderNoPerm()
				.executionRequirements(cause -> (!(cause.root() instanceof ServerPlayer) || cause.hasPermission(Permissions.WEATHER_STAFF) || (cause.hasPermission(Permissions.WEATHER) && cause.hasPermission(permission()) && cause.hasPermission(Permissions.getWeatherWorldPermission(command(), ((ServerPlayer) cause.root()).world())))))
				.build();
	}

	@Override
	public String permission() {
		return Permissions.getWeatherPermission(command());
	}

	@Override
	public String command() {
		return "sun";
	}

	@Override
	public String trackingName() {
		return "weather";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(
			ParameterSettings.of(CommandParameters.createWorld(Permissions.WEATHER_STAFF, true), true, locale -> getExceptions(locale).getWorldNotPresent()),
			ParameterSettings.of(CommandParameters.createInteger("Duration", true), true, locale -> getExceptions(locale).getValueNotPresent())
		);
	}

	private void setWeather(Audience src, Locale locale, ServerWorld world, Optional<Integer> duration) {
		if(duration.isPresent()) {
			world.setWeather(WeatherTypes.CLEAR.get(), Ticks.of(duration.get() * 20));
		} else world.setWeather(WeatherTypes.CLEAR.get(), Ticks.of(random.nextInt(10000) * 20));
		src.sendMessage(plugin.getLocales().getLocale(locale).getCommands().getWeather().getSun(world));
	}

}
