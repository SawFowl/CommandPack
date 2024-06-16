package sawfowl.commandpack.commands.raw.onlyplayercommands.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractKitsEditCommand;
import sawfowl.commandpack.configure.configs.kits.KitData;
import sawfowl.localeapi.api.TextUtils;

public class Commands extends AbstractKitsEditCommand {

	public Commands(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, ServerPlayer src, Locale locale, Mutable arguments, RawArgumentsMap args) throws CommandException {
		Kit kit = args.<Kit>get(0).get();
		KitData kitData = (KitData) (kit instanceof KitData ? kit : Kit.builder().copyFrom(kit));
		if(kitData.getExecuteCommands().isPresent() && kitData.getExecuteCommands().get().size() > 0) {
			Component title = getCommands(locale).getKits().getCommandsTitle();
			List<Component> commands = new ArrayList<>();
			kitData.getExecuteCommands().get().forEach(command -> {
				commands.add(TextUtils.createCallBack(plugin.getLocales().getLocale(locale).getButtons().getRemove(), () -> {
					if(kitData.getExecuteCommands().get().contains(command)) {
						kitData.removeCommand(command);
						kitData.save();
						src.sendMessage(getCommands(locale).getKits().getCommandRemoveSuccess());
					} else src.sendMessage(getCommands(locale).getKits().getCommandRemoveFail());
				}).append(Component.text(" " + command)));
			});
			sendPaginationList(src, title, Component.text("=").color(title.color()), 15, commands);
		} else src.sendMessage(getCommands(locale).getKits().getCommandsEmpty());
	}

	@Override
	public Component shortDescription(Locale locale) {
		return text("&3View and delete commands in a kit.");
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return text("&3View and delete commands in a kit.");
	}

	@Override
	public String command() {
		return "commands";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/kits commands <Kit>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(kitArgument(0, false, false));
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
