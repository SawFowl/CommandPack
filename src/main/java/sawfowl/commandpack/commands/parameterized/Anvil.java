package sawfowl.commandpack.commands.parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.ContainerTypes;
import org.spongepowered.api.item.inventory.type.ViewableInventory;

import net.kyori.adventure.audience.Audience;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Anvil extends AbstractParameterizedCommand {

	public Anvil(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			ServerPlayer player = getPlayer(context).orElse((ServerPlayer) src);
			player.openInventory(ViewableInventory.builder().type(ContainerTypes.ANVIL).completeStructure().carrier(player).plugin(plugin.getPluginContainer()).build().asMenu().inventory());
		} else getPlayer(context).get().openInventory(ViewableInventory.builder().type(ContainerTypes.ANVIL).completeStructure().carrier(getPlayer(context).get()).plugin(plugin.getPluginContainer()).build().asMenu().inventory());
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
