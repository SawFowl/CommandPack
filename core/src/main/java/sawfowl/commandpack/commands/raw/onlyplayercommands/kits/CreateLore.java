package sawfowl.commandpack.commands.raw.onlyplayercommands.kits;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.util.locale.Locales;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractKitsEditCommand;
import sawfowl.commandpack.configure.configs.kits.KitData;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class CreateLore extends AbstractKitsEditCommand {

	public CreateLore(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, ServerPlayer src, Locale locale, String[] args, Mutable arguments) throws CommandException {
		Kit kit = getKit(args, 0).get();
		KitData kitData = (KitData) (kit instanceof KitData ? kit : Kit.builder().copyFrom(kit));
		kitData.setLore(Locales.DEFAULT, Arrays.asList("&dFirst line", "&eSecond line"));
		kitData.setLore(Locales.RU_RU, Arrays.asList("&dСтрока 1", "&eСтрока 2"));
		kitData.save();
		src.sendMessage(getComponent(locale, LocalesPaths.COMMANDS_KITS_CREATE_LORE));
	}

	@Override
	public Component shortDescription(Locale locale) {
		return text("&3Create a kit description for manual editing later.");
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return text("&3Create a kit description for manual editing later.");
	}

	@Override
	public String command() {
		return "createlore";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/kits createlore <Kit>");
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
