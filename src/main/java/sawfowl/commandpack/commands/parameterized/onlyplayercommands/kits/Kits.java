package sawfowl.commandpack.commands.parameterized.onlyplayercommands.kits;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;

public class Kits extends AbstractPlayerCommand {

	public Kits(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
	}

	@Override
	public Parameterized build() {
		return builder()
				.addChild(new Create(plugin).build(), "create")
				.addChild(new Edit(plugin).build(), "edit")
				.addChild(new Cooldown(plugin).build(), "cooldown")
				.build();
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
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

}
