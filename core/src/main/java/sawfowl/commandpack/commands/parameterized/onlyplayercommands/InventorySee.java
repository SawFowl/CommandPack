package sawfowl.commandpack.commands.parameterized.onlyplayercommands;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.ContainerTypes;
import org.spongepowered.api.item.inventory.menu.InventoryMenu;
import org.spongepowered.api.item.inventory.type.ViewableInventory;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class InventorySee extends AbstractPlayerCommand {

	public InventorySee(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		if(!getUser(context).isPresent()) exception(getExceptions(locale).getPlayerNotPresent());
		if(Sponge.server().player(getUser(context).get()).isPresent()) {
			if(src.uniqueId().equals(Sponge.server().player(getUser(context).get()).get().uniqueId())) exception(getExceptions(locale).getTargetSelf());
			delay(src, locale, consumer -> {
				open(src, Sponge.server().player(getUser(context).get()).get());
			});
			return;
		}
		Sponge.server().userManager().load(getUser(context).get()).thenAccept(optUser -> {
			if(optUser.isPresent()) {
				try {
					delay(src, locale, consumer -> {
						open(src, optUser.get());
					});
				} catch (CommandException e) {
					src.sendMessage(e.componentMessage());
				}
			} else src.sendMessage(getExceptions(locale).getPlayerNotPresent());
		});
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createUser(false), false, locale -> getExceptions(locale).getUserNotPresent()));
	}

	@Override
	public String permission() {
		return Permissions.INVENTORYSEE;
	}

	@Override
	public String command() {
		return "inventorysee";
	}

	private void open(ServerPlayer src, ServerPlayer target) {
		InventoryMenu menu = InventoryMenu.of(ViewableInventory.builder()
				.type(ContainerTypes.GENERIC_9X4)
				.slots(target.inventory().storage().slots(), 0)
				.slots(target.inventory().hotbar().slots(), 27)
				.completeStructure()
				.plugin(getContainer())
				.carrier(target)
				.identity(UUID.randomUUID()).build());
		if(!src.hasPermission(Permissions.INVENTORYSEE_STAFF)) menu = menu.setReadOnly(true);
		menu.setTitle(plugin.getLocales().getLocale(src).getCommands().getInventorySee().getTitle(target));
		menu.open(src);
	}

	private void open(ServerPlayer src, User target) {
		InventoryMenu menu = InventoryMenu.of(ViewableInventory.builder()
				.type(ContainerTypes.GENERIC_9X4)
				.slots(target.inventory().slots().stream().filter(s -> s.get(Keys.SLOT_INDEX).isPresent() && s.get(Keys.SLOT_INDEX).get() > 8).collect(Collectors.toList()), 0)
				.slots(target.inventory().slots().stream().filter(s -> s.get(Keys.SLOT_INDEX).isPresent() && s.get(Keys.SLOT_INDEX).get() < 9).collect(Collectors.toList()), 27)
				.completeStructure()
				.plugin(getContainer())
				.carrier(target)
				.identity(UUID.randomUUID()).build());
		if(!src.hasPermission(Permissions.INVENTORYSEE_STAFF)) menu = menu.setReadOnly(true);
		menu.setTitle(plugin.getLocales().getLocale(src).getCommands().getInventorySee().getTitle(target));
		menu.open(src);
	}

}
