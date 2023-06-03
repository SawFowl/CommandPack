package sawfowl.commandpack.commands.parameterized.onlyplayercommands;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.ContainerTypes;
import org.spongepowered.api.item.inventory.menu.InventoryMenu;
import org.spongepowered.api.item.inventory.type.ViewableInventory;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class InventorySee extends AbstractPlayerCommand {

	public InventorySee(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		ServerPlayer target = getPlayer(context).get();
		if(target.uniqueId().equals(src.uniqueId())) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_TARGET_SELF);
		open(src, target);
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(false), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT));
	}

	@Override
	public String permission() {
		return Permissions.ENDERCHEST;
	}

	@Override
	public String command() {
		return "inventorysee";
	}

	private void open(ServerPlayer src, ServerPlayer target) {
		InventoryMenu.of(ViewableInventory.builder()
				.type(ContainerTypes.GENERIC_9X4)
				.slots(target.inventory().storage().slots(), 0)
				.slots(target.inventory().hotbar().slots(), 27)
				.completeStructure()
				.plugin(getContainer())
				.identity(UUID.randomUUID()).build()).open(src);
	}

}
