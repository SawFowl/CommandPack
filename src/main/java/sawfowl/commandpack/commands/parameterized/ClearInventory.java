package sawfowl.commandpack.commands.parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class ClearInventory extends AbstractParameterizedCommand {

	public ClearInventory(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		String invType = getString(context, "InventoryType", "primary");
		Optional<ServerPlayer> optTarget = getPlayer(context);
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) src;
			if(optTarget.isPresent()) {
				ServerPlayer target = optTarget.get();
				clear(target, invType);
				if(!player.uniqueId().equals(target.uniqueId())) {
					player.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_CLEAR_SUCCES_STAFF), Placeholders.PLAYER, target.name()));
					target.sendMessage(getText(target, LocalesPaths.COMMANDS_CLEAR_SUCCES_OTHER));
				} else player.sendMessage(getText(locale, LocalesPaths.COMMANDS_CLEAR_SUCCES));
			} else {
				clear(player, invType);
				player.sendMessage(getText(locale, LocalesPaths.COMMANDS_CLEAR_SUCCES));
			}
		} else {
			ServerPlayer target = optTarget.get();
			clear(target, invType);
			src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_CLEAR_SUCCES_STAFF), Placeholders.PLAYER, target.name()));
			target.sendMessage(getText(target, LocalesPaths.COMMANDS_CLEAR_SUCCES_OTHER));
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(
			ParameterSettings.of(CommandParameters.createPlayer(Permissions.CLEAR_STAFF, true), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT),
			ParameterSettings.of(CommandParameters.createInventoryTypes(true), true, LocalesPaths.COMMANDS_EXCEPTION_TYPE_NOT_PRESENT)
		);
	}
/*
	@Override
	public List<ParameterSettingsImpl> getParameterSettings() {
		return Arrays.asList(
					new ParameterSettingsImpl(CommandParameters.createPlayer(Permissions.CLEAR_STAFF, true), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT),
					new ParameterSettingsImpl(CommandParameters.createInventoryTypes(true), true, LocalesPaths.COMMANDS_EXCEPTION_TYPE_NOT_PRESENT)
				);
	}
*/
	@Override
	public String permission() {
		return Permissions.CLEAR;
	}

	@Override
	public String command() {
		return "clearinventory";
	}

	private void clear(ServerPlayer player, String invType) {
		switch(invType) {
			case "all":
				player.inventory().clear();
				player.enderChestInventory().clear();
				break;
			case "equipment":
				player.inventory().equipment().clear();
				break;
			case "hotbar":
				player.inventory().hotbar().clear();
				break;
			case "primary":
				player.inventory().primary().clear();
				break;
			case "enderchest":
				player.enderChestInventory().clear();
				break;
			default:
				player.inventory().primary().clear();
				break;
		}
	}

}
