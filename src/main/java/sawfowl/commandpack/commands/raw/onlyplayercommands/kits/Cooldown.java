package sawfowl.commandpack.commands.raw.onlyplayercommands.kits;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
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
		Kit kit = getKit(args, 0).get();
		KitData kitData = (KitData) (kit instanceof KitData ? kit : Kit.builder().copyFrom(kit));
		Duration duration = getDurationArg(args, 1, locale).get();
		kitData.setCooldown(duration.getSeconds());
		kit.save();
		src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_KITS_COOLDOWN_SUCCESS), Placeholders.VALUE, kit.getLocalizedName(locale)));
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
		return text("&c/kits cooldown <Kit> <Duration>");
	}

	@Override
	public String command() {
		return "cooldown";
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			kitArgument(0, false, false),
			RawArguments.createStringArgument(new ArrayList<>(), false, false, 1, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

}
