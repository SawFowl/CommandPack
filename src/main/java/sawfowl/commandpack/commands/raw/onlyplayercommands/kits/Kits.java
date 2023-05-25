package sawfowl.commandpack.commands.raw.onlyplayercommands.kits;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.RawArgument;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractPlayerCommand;

public class Kits extends AbstractPlayerCommand {

	public Kits(CommandPack plugin) {
		super(plugin);
		Sponge.eventManager().registerListeners(getContainer(), this);
		addChilds();
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
	public void process(CommandCause cause, ServerPlayer src, Locale locale, String[] args, Mutable arguments) throws CommandException {
		src.sendMessage(usage(cause));
	}

	@Override
	public List<CommandCompletion> complete(CommandCause cause, List<String> args, Mutable arguments, String currentInput) throws CommandException {
		return getEmptyCompletion();
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
		return text("&c/kits create|edit|setname|cooldown");
	}

	private void addChilds() {
		getChildExecutors().put("addcommand", new AddCommand(plugin));
		getChildExecutors().put("commands", new Commands(plugin));
		getChildExecutors().put("cooldown", new Cooldown(plugin));
		getChildExecutors().put("create", new Create(plugin));
		getChildExecutors().put("createlore", new CreateLore(plugin));
		getChildExecutors().put("edit", new Edit(plugin));
		getChildExecutors().put("firsttime", new FirstTime(plugin));
		getChildExecutors().put("givelimit", new GiveLimit(plugin));
		getChildExecutors().put("giveonjoin", new GiveOnJoin(plugin));
		getChildExecutors().put("giverule", new GiveRule(plugin));
		getChildExecutors().put("needperm", new NeedPerm(plugin));
		getChildExecutors().put("setname", new SetName(plugin));
	}

	@Listener(order = Order.LAST)
	public void onStarted(StartedEngineEvent<Server> event) {
		if(plugin.getEconomy().isPresent()) getChildExecutors().put("setprice", new SetPrice(plugin));
		Sponge.eventManager().unregisterListeners(this);
	}

	@Override
	public List<RawArgument<?>> getArguments() {
		return null;
	}

}
