package sawfowl.commandpack.commands.parameterized.onlyplayercommands;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.ContainerTypes;
import org.spongepowered.api.item.inventory.menu.InventoryMenu;
import org.spongepowered.api.item.inventory.menu.handler.CloseHandler;
import org.spongepowered.api.item.inventory.type.ViewableInventory;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Disposal extends AbstractPlayerCommand {

	public Disposal(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		InventoryMenu trash = ViewableInventory.builder().type(ContainerTypes.GENERIC_9X6).completeStructure().carrier(src).plugin(plugin.getPluginContainer()).build().asMenu();
		trash.setTitle(plugin.getLocales().getLocale(locale).getCommands().getDisposal().getTitle());
		trash.registerClose(new CloseHandler() {
			@Override
			public void handle(Cause cause, Container container) {
				trash.inventory().clear();
			}
		});
		trash.open(src);
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

	@Override
	public String command() {
		return "disposal";
	}

	@Override
	public String permission() {
		return Permissions.DISPOSAL;
	}

}
