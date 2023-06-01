package sawfowl.commandpack.commands.raw.onlyplayercommands;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.CauseStackManager.StackFrame;
import org.spongepowered.api.event.item.inventory.AffectSlotEvent;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.ContainerTypes;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.menu.InventoryMenu;
import org.spongepowered.api.item.inventory.menu.handler.SlotChangeHandler;
import org.spongepowered.api.item.inventory.transaction.SlotTransaction;
import org.spongepowered.api.item.inventory.type.ViewableInventory;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawCompleterSupplier;
import sawfowl.commandpack.api.commands.raw.arguments.RawResultSupplier;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractPlayerCommand;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class InventorySee extends AbstractPlayerCommand {

	public InventorySee(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, ServerPlayer src, Locale locale, String[] args, Mutable arguments) throws CommandException {
		if(!Sponge.server().player(args[0]).isPresent()) {
			try {
				Sponge.server().userManager().load(args[0]).get().ifPresent(user -> {
					open(src, user);
				});
			} catch (InterruptedException | ExecutionException e) {
				exception(locale, LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_FOUND);
			}
		} else open(src, Sponge.server().player(args[0]).get().user());
	}

	@Override
	public Component shortDescription(Locale locale) {
		return null;
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return null;
	}

	@Override
	public String permission() {
		return Permissions.INVENTORYSEE_STAFF;
	}

	@Override
	public String command() {
		return "inventorysee";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/inventorysee <Player>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(RawArgument.of(String.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(String[] args) {
				return Sponge.server().onlinePlayers().stream().map(ServerPlayer::name);
			}
		}, new RawResultSupplier<String>() {
			@Override
			public Optional<String> get(String[] args) {
				return args.length > 0 && Sponge.server().player(args[0]).isPresent() ? Optional.ofNullable(args[0]) : Optional.empty();
			}
		}, false, false, 0, LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_FOUND));
	}

	private void open(ServerPlayer src, User target) {
		src.openInventory(target.inventory());
		InventoryMenu menu = ViewableInventory.builder()
				.type(ContainerTypes.GENERIC_9X4)
				.slots(target.inventory().slots(), 0)
				.completeStructure()
				.plugin(getContainer())
				.identity(UUID.randomUUID()).build().asMenu();
		target.inventory().intersect(menu.inventory()).slots().forEach(slot -> {
			menu.inventory().set(slot.get(Keys.SLOT_INDEX).get(), slot.peek());
		});
		menu.registerChange(new SlotChangeHandler() {
			@Override
			public boolean handle(Cause cause, Container container, Slot slot, int slotIndex, ItemStackSnapshot oldStack, ItemStackSnapshot newStack) {
				if(slotIndex > 35) return false;
				try(StackFrame frame = Sponge.server().causeStackManager().pushCauseFrame()) {
					Sponge.server().scheduler().executor(getContainer()).execute(() -> {
						Sponge.eventManager().post(new AffectSlotEvent() {

							@Override
							public Cause cause() {
								return frame.currentCause();
							}

							@Override
							public boolean isCancelled() {
								return false;
							}

							@Override
							public void setCancelled(boolean cancel) {
							}

							@Override
							public List<SlotTransaction> transactions() {
								return Arrays.asList(new SlotTransaction(target.inventory().slot(slotIndex).get(), target.inventory().slot(slotIndex).get().peek().createSnapshot(), newStack));
							}
							
						});
					});
				}
				return true;
			}
		});
		src.openInventory(menu.inventory());
	}

}
