package sawfowl.commandpack.commands.parameterized.onlyplayercommands.kits;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractKitsEditCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.configs.kits.KitData;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Cooldown extends AbstractKitsEditCommand {

	public Cooldown(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		Optional<Kit> optKit = getKit(context);
		if(!optKit.isPresent()) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT);
		KitData kit = (KitData) (optKit.get() instanceof KitData ? optKit.get() : Kit.builder().copyFrom(optKit.get()));
		try {
			Duration duration = getArgument(context, CommandParameters.DURATION).get();
			kit.setCooldown(duration.getSeconds());
			kit.save();
			src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_KITS_COOLDOWN_SUCCESS), Placeholders.VALUE, kit.getLocalizedName(locale)));
		} catch (DateTimeParseException e) {
			exception(locale, LocalesPaths.COMMANDS_KITS_COOLDOWN_INCORRECT_TIME);
		}
	}

	@Override
	public Parameterized build() {
		return Command.builder()
				.permission(permission())
				.executor(this)
				.addParameter(CommandParameters.createString("Kit", false))
				.addParameter(CommandParameters.DURATION)
				.build();
	}

	@Override
	public String command() {
		return "cooldown";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createString("Kit", false), false, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT), 
			ParameterSettings.of(CommandParameters.DURATION, false, LocalesPaths.COMMANDS_KITS_COOLDOWN_INCORRECT_TIME)
		);
	}

}
