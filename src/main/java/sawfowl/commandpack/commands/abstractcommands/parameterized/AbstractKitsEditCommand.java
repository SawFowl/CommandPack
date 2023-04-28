package sawfowl.commandpack.commands.abstractcommands.parameterized;

import java.util.Optional;

import org.spongepowered.api.command.parameter.CommandContext;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.kits.Kit;

public abstract class AbstractKitsEditCommand extends AbstractPlayerCommand {

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
