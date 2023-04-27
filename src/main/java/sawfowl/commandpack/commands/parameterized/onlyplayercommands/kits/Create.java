package sawfowl.commandpack.commands.parameterized.onlyplayercommands.kits;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Create extends AbstractPlayerCommand {

	public Create(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		String id = getArgument(context, String.class, "Name").get();
		if(plugin.getKitService().getKit(id).isPresent()) exception(locale, LocalesPaths.COMMANDS_KITS_CREATE_EXIST);
		Kit kit = Kit.builder().id(id).build();
		kit.asMenu(getContainer(), src, false).open(src);
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.KIT_STAFF;
	}

	@Override
	public String command() {
		return "create";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createString("Name", false), false, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT));
	}

}
