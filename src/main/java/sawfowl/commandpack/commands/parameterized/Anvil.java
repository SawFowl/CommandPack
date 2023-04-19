package sawfowl.commandpack.commands.parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.ContainerTypes;
import org.spongepowered.api.item.inventory.menu.InventoryMenu;
import org.spongepowered.api.item.inventory.type.ViewableInventory;

import net.kyori.adventure.audience.Audience;
import sawfowl.commandpack.CommandPackPlugin;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Anvil extends AbstractParameterizedCommand {

	public Anvil(CommandPackPlugin plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			Optional<ServerPlayer> target = getPlayer(context);
			InventoryMenu menu = ViewableInventory.builder().type(ContainerTypes.ANVIL).completeStructure().carrier(target.isPresent() ? target.get() : (ServerPlayer) src).plugin(plugin.getPluginContainer()).build().asMenu();
			if(target.isPresent()) {
				menu.open(target.get());
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_ANVIL));
			} else delay((ServerPlayer) src, locale, consumer -> {
				menu.open((ServerPlayer) src);
			});
		} else {
			ServerPlayer target = getPlayer(context).get();
			ViewableInventory.builder().type(ContainerTypes.ANVIL).completeStructure().carrier(target).plugin(plugin.getPluginContainer()).build().asMenu().open(target);
			src.sendMessage(getText(locale, LocalesPaths.COMMANDS_ANVIL));
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.ANVIL;
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(Permissions.ANVIL_STAFF, true), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT));
	}

	@Override
	public String command() {
		return "anvil";
	}

}
