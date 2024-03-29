package sawfowl.commandpack.commands.raw.onlyplayercommands.kits;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.spongepowered.api.adventure.SpongeComponents;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractKitsEditCommand;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Edit extends AbstractKitsEditCommand {

	public Edit(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, ServerPlayer src, Locale locale, String[] args, Mutable arguments) throws CommandException {
		if(plugin.getKitService().getKits().isEmpty()) exception(locale, LocalesPaths.COMMANDS_KITS_NO_KITS);
		Optional<Kit> optKit = getKit(args, cause, 0);
		if(optKit.isPresent()) {
			optKit.get().asMenu(getContainer(), src, false).open(src);
		} else {
			sendKitsList(src, locale);
		}
	}

	@Override
	public Component shortDescription(Locale locale) {
		return text("&3Edit kits");
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return text("&3Edit kits");
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/kits edit <Name>");
	}

	@Override
	public String command() {
		return "edit";
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(kitArgument(0, true, true));
	}

	private void sendKitsList(ServerPlayer src, Locale locale) {
		Component title = getComponent(locale, LocalesPaths.COMMANDS_KITS_LIST_HEADER);
		sendPaginationList(src, title, Component.text("=").color(title.color()), 15, plugin.getKitService().getKits().stream().map(k -> TextUtils.createCallBack(getComponent(locale, LocalesPaths.REMOVE), () -> {
			if(!plugin.getKitService().kitExist(k.id())) return;
			plugin.getKitService().removeKit(k);
			sendKitsList(src, locale);
		}).append(Component.text(" ").append(k.getLocalizedName(locale).clickEvent(SpongeComponents.executeCallback(consumer -> {
			if(plugin.getKitService().getKit(TextUtils.clearDecorations(k.id())).isPresent()) k.asMenu(getContainer(), src, false).open(src);
		}))))).collect(Collectors.toList()));
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
