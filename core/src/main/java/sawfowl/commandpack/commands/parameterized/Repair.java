package sawfowl.commandpack.commands.parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.ItemStack;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Repair extends AbstractParameterizedCommand {

	public Repair(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		String select = getString(context, "Repair", "");
		Optional<ServerPlayer> optTarget = getPlayer(context);
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) src;
			if(optTarget.isPresent()) {
				ServerPlayer target = optTarget.get();
				if(!target.uniqueId().equals(player.uniqueId())) {
					repairItems(target, select);
					target.sendMessage(getRepair(target).getSuccessOther());
					player.sendMessage(getRepair(locale).getSuccessStaff(target));
				} else delay(player, locale, consumer -> {
					repairItems(player, select);
					player.sendMessage(getRepair(locale).getSuccess());
				});
			} else delay(player, locale, consumer -> {
				repairItems(player, select);
				player.sendMessage(getRepair(locale).getSuccess());
			});
		} else {
			if(optTarget.isPresent()) {
				ServerPlayer target = optTarget.get();
				repairItems(target, select);
				target.sendMessage(getRepair(target).getSuccessOther());
				src.sendMessage(getRepair(locale).getSuccessStaff(target));
			}
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(
			ParameterSettings.of(CommandParameters.createPlayer(Permissions.REPAIR_STAFF, true), false, locale -> getExceptions(locale).getPlayerNotPresent()),
			ParameterSettings.of(CommandParameters.REPAIR, true, locale -> getExceptions(locale).getTypeNotPresent())
		);
	}

	@Override
	public String permission() {
		return Permissions.REPAIR;
	}

	@Override
	public String command() {
		return "repair";
	}

	private void repairItems(ServerPlayer player, String select) {
		switch (select) {
			case "all":
				player.inventory().slots().forEach(slot -> {
					repairItem(slot.peek());
				});
				break;
			case "armor":
				player.inventory().armor().slots().forEach(slot -> {
					repairItem(slot.peek());
				});
				break;
			case "hands":
				repairItem(player.itemInHand(HandTypes.MAIN_HAND));
				repairItem(player.itemInHand(HandTypes.OFF_HAND));
				break;
			default:
				repairItem(player.itemInHand(HandTypes.MAIN_HAND));
				break;
		}
	}

	private void repairItem(ItemStack itemStack) {
		if(!itemStack.isEmpty()) itemStack.offer(Keys.ITEM_DURABILITY, itemStack.get(Keys.MAX_DURABILITY).orElse(Integer.MAX_VALUE));
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Repair getRepair(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getRepair();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Repair getRepair(ServerPlayer player) {
		return getRepair(player.locale());
	}

}
