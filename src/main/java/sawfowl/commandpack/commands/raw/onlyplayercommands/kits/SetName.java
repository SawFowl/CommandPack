package sawfowl.commandpack.commands.raw.onlyplayercommands.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractKitsEditCommand;
import sawfowl.commandpack.configure.configs.kits.KitData;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.EnumLocales;

public class SetName extends AbstractKitsEditCommand  {

	public SetName(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, ServerPlayer src, Locale locale, String[] args, Mutable arguments) throws CommandException {
		Optional<Kit> optKit = args.length > 0 ? getKit(args[0]) : Optional.empty();
		if(!optKit.isPresent()) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT);
		KitData kit = (KitData) (optKit.get() instanceof KitData ? optKit.get() : Kit.builder().copyFrom(optKit.get()));
		if(args.length < 2) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT);
		Locale localeForName = EnumLocales.find(args[1].replace("-", "_"));
		if(args.length < 3) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_NAME_NOT_PRESENT);
		List<String> list = new ArrayList<>(Arrays.asList(args));
		list.remove(0);
		list.remove(0);
		String name = "";
		for (int i = 0; i < list.size(); i++) {
			name = name + list.get(i);
			if(i < list.size() - 1) {
				name = name + " ";
			}
		}
		kit.setName(localeForName, name);
		kit.save();
		src.sendMessage(getText(locale, LocalesPaths.COMMANDS_KITS_SET_NAME));
	}

	@Override
	public List<CommandCompletion> complete(CommandCause cause, List<String> args, Mutable arguments, String currentInput) throws CommandException {
		if(args.size() == 0) return plugin.getKitService().getKits().stream().map(kit -> CommandCompletion.of(kit.id())).collect(Collectors.toList());
		if(args.size() == 1 && !currentInput.endsWith(" ")) return plugin.getKitService().getKits().stream().filter(kit -> (kit.id().startsWith(args.get(0)))).map(kit -> CommandCompletion.of(kit.id())).collect(Collectors.toList());
		if(args.size() == 1 && currentInput.endsWith(" ")) return Stream.of(EnumLocales.values()).map(locale -> CommandCompletion.of(locale.getTag())).collect(Collectors.toList());
		if(args.size() == 2 && !currentInput.endsWith(" ")) return Stream.of(EnumLocales.values()).filter(locale -> (locale.getTag().startsWith(args.get(1)))).map(locale -> CommandCompletion.of(locale.getTag())).collect(Collectors.toList());
		return getEmptyCompletion();
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
	public Component usage(CommandCause cause) {
		return text("&c/kits setname <Kit> <Locale> <Name>");
	}

	@Override
	public String command() {
		return "setname";
	}

}
