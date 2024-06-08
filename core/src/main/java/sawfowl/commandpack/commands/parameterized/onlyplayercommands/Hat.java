package sawfowl.commandpack.commands.parameterized.onlyplayercommands;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.adventure.SpongeComponents;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.equipment.EquipmentTypes;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Hat extends AbstractPlayerCommand {

	public Hat(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		ServerPlayer player = getPlayer(context).orElse(src);
		ItemStack handItem = src.itemInHand(HandTypes.MAIN_HAND.get()).copy();
		if(handItem.type().equals(getAir())) exception(getHat(locale).getNotPresent());
		if(plugin.getMainConfig().isBlackListHat(handItem)) exception(getHat(locale).getBlackListItem());
		if(player.uniqueId().equals(src.uniqueId())) {
			delay(player, locale, consumer -> {
				player.equipment().peek(EquipmentTypes.HEAD.get()).ifPresent(headItem -> {
					player.setItemInHand(HandTypes.MAIN_HAND.get(), headItem);
				});
				player.equipment().set(EquipmentTypes.HEAD.get(), handItem);
			});
		} else {
			if(player.inventory().primary().freeCapacity() > 0) {
				player.equipment().peek(EquipmentTypes.HEAD.get()).ifPresent(headItem -> {
					player.inventory().primary().offer(headItem);
				});
				player.equipment().set(EquipmentTypes.HEAD.get(), handItem);
				src.sendMessage(getHat(locale).getSuccessStaff(player));
			} else {
				src.sendMessage(getHat(locale).getFullInventory(player).clickEvent(SpongeComponents.executeCallback(cause -> {
					player.equipment().set(EquipmentTypes.HEAD.get(), handItem);
					src.sendMessage(getHat(locale).getSuccessStaff(player));
				})));
			}
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.HAT;
	}

	private ItemType getAir() {
		return ItemTypes.AIR.get();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(Permissions.HAT_STAFF, true), true, locale -> getExceptions(locale).getPlayerNotPresent()));
	}

	@Override
	public String command() {
		return "hat";
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Hat getHat(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getHat();
	}

}
