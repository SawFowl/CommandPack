package sawfowl.commandpack.commands.raw.onlyplayercommands.kits;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawArgument;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractKitsEditCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.configs.kits.KitData;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Cooldown extends AbstractKitsEditCommand {

	public Cooldown(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, ServerPlayer src, Locale locale, String[] args, Mutable arguments) throws CommandException {
		Optional<Kit> optKit = getKit(args);
		if(!optKit.isPresent()) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT);
		KitData kit = (KitData) (optKit.get() instanceof KitData ? optKit.get() : Kit.builder().copyFrom(optKit.get()));
		if(args.length < 2) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT);
		Duration duration = getDuration(args[1], locale);
		kit.setCooldown(duration.getSeconds());
		kit.save();
		src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_KITS_COOLDOWN_SUCCESS), Placeholders.VALUE, kit.getLocalizedName(locale)));
	}

	@Override
	public List<CommandCompletion> complete(CommandCause cause, List<String> args, Mutable arguments, String currentInput) throws CommandException {
		if(args.size() == 0) return plugin.getKitService().getKits().stream().map(kit -> CommandCompletion.of(kit.id())).collect(Collectors.toList());
		if(args.size() == 1 && !currentInput.endsWith(" ")) return plugin.getKitService().getKits().stream().filter(kit -> (kit.id().startsWith(args.get(0)))).map(kit -> CommandCompletion.of(kit.id())).collect(Collectors.toList());
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
		return null;
	}

	@Override
	public String command() {
		return "cooldown";
	}

	@Override
	public List<RawArgument<?>> getArguments() {
		return null;
	}

}
