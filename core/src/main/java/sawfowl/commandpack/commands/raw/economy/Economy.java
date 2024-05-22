package sawfowl.commandpack.commands.raw.economy;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Economy extends AbstractRawCommand {

	private List<RawCommand> childs;
	public Economy(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		Component message = null;
		for(RawCommand command : getChildExecutors().values()) {
			if(message == null) {
				message = command.usage(cause).clickEvent(ClickEvent.suggestCommand("/eco " + command.command() + " "));
			} else message = message.append(Component.newline()).append(command.usage(cause).clickEvent(ClickEvent.suggestCommand("/eco " + command.command() + " ")));
		}
		audience.sendMessage(message);
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
		return Permissions.ECONOMY_STAFF;
	}

	@Override
	public String command() {
		return "economy";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/eco " + String.join("|", getChildExecutors().keySet().toArray(new String[]{})));
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return null;
	}

	@Override
	public List<RawCommand> childCommands() {
		return childs != null ? childs : (Arrays.asList(new SetBalance(plugin), new AddToBalance(plugin), new RemoveFromBalance(plugin), new BalanceAll(plugin)));
	}

	@Override
	public boolean isEnable() {
		return plugin.getMainConfig().getEconomy().isEnable();
	}

}
