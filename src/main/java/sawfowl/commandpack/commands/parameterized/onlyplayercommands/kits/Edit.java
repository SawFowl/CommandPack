package sawfowl.commandpack.commands.parameterized.onlyplayercommands.kits;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.spongepowered.api.adventure.SpongeComponents;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractKitsEditCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Edit extends AbstractKitsEditCommand {

	public Edit(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		if(plugin.getKitService().getKits().isEmpty()) exception(locale, LocalesPaths.COMMANDS_KITS_NO_KITS);
		Optional<Kit> kit = getKit(context);
		if(kit.isPresent()) {
			kit.get().asMenu(getContainer(), src, false).open(src);
		} else {
			Component title = getText(locale, LocalesPaths.COMMANDS_KITS_LIST_HEADER);
			sendPaginationList(src, title, Component.text("=").color(title.color()), 15, plugin.getKitService().getKits().stream().map(k -> k.getLocalizedName(locale).clickEvent(SpongeComponents.executeCallback(consumer -> {
				k.asMenu(getContainer(), src, false).open(src);
			}))).collect(Collectors.toList()));
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String command() {
		return "edit";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createString("Kit", true), false, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT));
	}

}
