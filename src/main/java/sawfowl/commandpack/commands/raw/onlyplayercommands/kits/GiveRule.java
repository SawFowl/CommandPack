package sawfowl.commandpack.commands.raw.onlyplayercommands.kits;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractKitsEditCommand;
import sawfowl.commandpack.configure.configs.kits.KitData;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class GiveRule extends AbstractKitsEditCommand {

	public GiveRule(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, ServerPlayer src, Locale locale, String[] args, Mutable arguments) throws CommandException {
		Kit kit = getKit(args, 0).get();
		KitData kitData = (KitData) (kit instanceof KitData ? kit : Kit.builder().copyFrom(kit));
		kitData.setRule(sawfowl.commandpack.api.data.kits.GiveRule.getRule(args[1]));
		kitData.save();
		src.sendMessage(getComponent(locale, LocalesPaths.COMMANDS_KITS_GIVE_RULE));
	}

	@Override
	public List<CommandCompletion> complete(CommandCause cause, List<String> args, Mutable arguments, String currentInput) throws CommandException {
		if(args.size() == 0) return plugin.getKitService().getKits().stream().map(kit -> CommandCompletion.of(kit.id())).collect(Collectors.toList());
		if(args.size() == 1 && !currentInput.endsWith(" ")) return plugin.getKitService().getKits().stream().filter(kit -> (kit.id().startsWith(args.get(0)))).map(kit -> CommandCompletion.of(kit.id())).collect(Collectors.toList());
		if(args.size() == 1 && currentInput.endsWith(" ")) return Stream.of(sawfowl.commandpack.api.data.kits.GiveRule.values()).map(rule -> CommandCompletion.of(rule.getName())).collect(Collectors.toList());
		if(args.size() == 2 && !currentInput.endsWith(" ")) return Stream.of(sawfowl.commandpack.api.data.kits.GiveRule.values()).filter(rule -> (rule.getName().startsWith(args.get(1)))).map(rule -> CommandCompletion.of(rule.getName())).collect(Collectors.toList());
		return getEmptyCompletion();
	}

	@Override
	public Component shortDescription(Locale locale) {
		return text("&3Changing the rule for issuing a kit.");
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return text("&3Changing the rule for issuing a kit.");
	}

	@Override
	public String command() {
		return "giverule";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/kits giverule <Kit> <Rule>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			kitArgument(0, false, false),
			RawArguments.createStringArgument(sawfowl.commandpack.api.data.kits.GiveRule.getAllRules(), false, false, 1, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

}
