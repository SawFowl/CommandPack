package sawfowl.commandpack.commands.abstractcommands.parameterized;

import java.util.Optional;

import org.spongepowered.api.command.parameter.CommandContext;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public abstract class AbstractKitsEditCommand extends AbstractPlayerCommand {

	protected final ParameterSettings kitsParameter = ParameterSettings.of(CommandParameters.KITS, false, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT);
	public AbstractKitsEditCommand(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public String permission() {
		return Permissions.KIT_STAFF;
	}

	protected Optional<Kit> getKit(CommandContext context) {
		return getString(context, "Kit").isPresent() ? plugin.getKitService().getKit(getString(context, "Kit").get()) : Optional.empty();
	}

}
