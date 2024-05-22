package sawfowl.commandpack.commands.raw.onlyplayercommands;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractPlayerCommand;
import sawfowl.commandpack.commands.raw.onlyplayercommands.kits.AddCommand;
import sawfowl.commandpack.commands.raw.onlyplayercommands.kits.Commands;
import sawfowl.commandpack.commands.raw.onlyplayercommands.kits.Cooldown;
import sawfowl.commandpack.commands.raw.onlyplayercommands.kits.Create;
import sawfowl.commandpack.commands.raw.onlyplayercommands.kits.CreateLore;
import sawfowl.commandpack.commands.raw.onlyplayercommands.kits.Edit;
import sawfowl.commandpack.commands.raw.onlyplayercommands.kits.FirstTime;
import sawfowl.commandpack.commands.raw.onlyplayercommands.kits.GiveLimit;
import sawfowl.commandpack.commands.raw.onlyplayercommands.kits.GiveOnJoin;
import sawfowl.commandpack.commands.raw.onlyplayercommands.kits.GiveRule;
import sawfowl.commandpack.commands.raw.onlyplayercommands.kits.NeedPerm;
import sawfowl.commandpack.commands.raw.onlyplayercommands.kits.SetName;
import sawfowl.commandpack.commands.raw.onlyplayercommands.kits.SetPrice;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Kits extends AbstractPlayerCommand {

	private List<RawCommand> childs;
	public Kits(CommandPack plugin) {
		super(plugin);
		Sponge.eventManager().registerListeners(getContainer(), this);
	}

	@Override
	public String permission() {
		return Permissions.KIT_STAFF;
	}

	@Override
	public String command() {
		return "kits";
	}

	@Override
	public void process(CommandCause cause, ServerPlayer src, Locale locale, Mutable arguments, RawArgumentsMap args) throws CommandException {
		Component message = null;
		for(RawCommand command : getChildExecutors().values()) {
			if(message == null) {
				message = command.usage(cause).clickEvent(ClickEvent.suggestCommand("/kits " + command.command() + " "));
			} else message = message.append(Component.newline()).append(command.usage(cause).clickEvent(ClickEvent.suggestCommand("/kits " + command.command() + " ")));
		}
		src.sendMessage(message);
	}

	@Override
	public Component shortDescription(Locale locale) {
		return text("Create and edit kits.");
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return text("Create and edit kits.");
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/kits " + String.join("|", getChildExecutors().keySet().toArray(new String[]{})));
	}

	@Listener(order = Order.LAST)
	public void onStarted(StartedEngineEvent<Server> event) {
		if(plugin.getEconomy().isPresent()) getChildExecutors().put("setprice", new SetPrice(plugin));
		Sponge.eventManager().unregisterListeners(this);
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return null;
	}

	@Override
	public List<RawCommand> childCommands() {
		return childs != null ? childs : (childs = Arrays.asList(
			new AddCommand(plugin),
			new Commands(plugin),
			new Cooldown(plugin),
			new Create(plugin),
			new CreateLore(plugin),
			new Edit(plugin),
			new FirstTime(plugin),
			new GiveLimit(plugin),
			new GiveOnJoin(plugin),
			new GiveRule(plugin),
			new NeedPerm(plugin),
			new SetName(plugin)
		));
	}

}
