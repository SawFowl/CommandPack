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
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Repair extends AbstractParameterizedCommand {

	public Repair(CommandPack plugin) {
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
				repairItems(target, select);
				if(!target.uniqueId().equals(player.uniqueId())) {
					target.sendMessage(getText(target, LocalesPaths.COMMANDS_REPAIR_SUCCES_OTHER));
					player.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_REPAIR_SUCCES_STAFF), Placeholders.PLAYER, target.name()));
				} else player.sendMessage(getText(locale, LocalesPaths.COMMANDS_REPAIR_SUCCES));
			} else {
				repairItems(player, select);
				player.sendMessage(getText(locale, LocalesPaths.COMMANDS_REPAIR_SUCCES));
			}
		} else {
			if(optTarget.isPresent()) {
				ServerPlayer target = optTarget.get();
				repairItems(target, select);
				target.sendMessage(getText(target, LocalesPaths.COMMANDS_REPAIR_SUCCES_OTHER));
				src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_REPAIR_SUCCES_STAFF), Placeholders.PLAYER, target.name()));
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
			ParameterSettings.of(CommandParameters.createPlayer(Permissions.REPAIR_STAFF, true), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT),
			ParameterSettings.of(CommandParameters.REPAIR, true, LocalesPaths.COMMANDS_EXCEPTION_TYPE_NOT_PRESENT)
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

}
