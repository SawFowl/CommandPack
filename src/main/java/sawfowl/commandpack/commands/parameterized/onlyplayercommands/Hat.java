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
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Hat extends AbstractPlayerCommand {

	public Hat(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		ServerPlayer player = getPlayer(context).orElse(src);
		ItemStack handItem = src.itemInHand(HandTypes.MAIN_HAND.get()).copy();
		if(handItem.type().equals(getAir())) exception(src, LocalesPaths.COMMANDS_HAT_NO_ITEM);
		if(plugin.getMainConfig().isBlackListHat(handItem)) exception(src, LocalesPaths.COMMANDS_HAT_BLACKLIST_ITEM);
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
				src.sendMessage(TextUtils.replace(getText(src, LocalesPaths.COMMANDS_HAT_SUCCESS_OTHER), Placeholders.PLAYER, player.name()));
			} else {
				src.sendMessage(TextUtils.replace(getText(src, LocalesPaths.COMMANDS_HAT_FULL_INVENTORY), Placeholders.PLAYER, player.name()).clickEvent(SpongeComponents.executeCallback(cause -> {
					player.equipment().set(EquipmentTypes.HEAD.get(), handItem);
					src.sendMessage(TextUtils.replace(getText(src, LocalesPaths.COMMANDS_HAT_SUCCESS_OTHER), Placeholders.PLAYER, player.name()));
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
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(Permissions.HAT_STAFF, true), true, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT));
	}

	@Override
	public String command() {
		return "hat";
	}

}
