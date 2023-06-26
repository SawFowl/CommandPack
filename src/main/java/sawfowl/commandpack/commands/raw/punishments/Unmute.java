package sawfowl.commandpack.commands.raw.punishments;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawCompleterSupplier;
import sawfowl.commandpack.api.commands.raw.arguments.RawResultSupplier;
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Unmute extends AbstractRawCommand {

	public Unmute(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		Mute mute = getArgument(Mute.class, args, 0).get();
		plugin.getPunishmentService().removeMute(mute);
		Sponge.systemSubject().sendMessage(TextUtils.replaceToComponents(getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_UNMUTE_ANNOUNCEMENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER}, new Component[] {(isPlayer ? ((ServerPlayer) audience).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) audience).name())) : text("&cServer")), text(mute.getName())}));
		if(!plugin.getMainConfig().getPunishment().getAnnounce().isUnban()) {
			audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_UNMUTE_SUCCESS), Placeholders.PLAYER, mute.getName()));
			Sponge.server().player(mute.getUniqueId()).ifPresent(player -> {
				player.sendMessage(getText(player, LocalesPaths.COMMANDS_UNMUTE_SUCCESS_TARGET));
			});
		} else Sponge.server().onlinePlayers().forEach(player -> {
			player.sendMessage(TextUtils.replaceToComponents(getText(player, LocalesPaths.COMMANDS_UNMUTE_ANNOUNCEMENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER}, new Component[] {(isPlayer ? ((ServerPlayer) audience).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) audience).name())) : text("&cServer")), text(mute.getName())}));
		});
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
	public String permission() {
		return Permissions.UNMUTE_STAFF;
	}

	@Override
	public String command() {
		return "unmute";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/unmute <Player>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(RawArgument.of(Mute.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(String[] args) {
				return plugin.getPunishmentService().getAllMutes().stream().map(m -> m.getName());
			}
		}, new RawResultSupplier<Mute>() {
			@Override
			public Optional<Mute> get(String[] args) {
				Collection<Mute> variants = plugin.getPunishmentService().getAllMutes();
				return args.length == 0 || variants.isEmpty() ? Optional.empty() : variants.stream().filter(m -> m.getName().equals(args[0])).findFirst();
			}
		}, false, false, 0, LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_PRESENT));
	}

	@Override
	public void register(RegisterCommandEvent<Raw> event) {
		if(!plugin.getMainConfig().getPunishment().isEnable()) return;
		if(getCommandSettings() == null) {
			event.register(getContainer(), this, command());
		} else {
			if(!getCommandSettings().isEnable()) return;
			if(getCommandSettings().getAliases() != null && getCommandSettings().getAliases().length > 0) {
				event.register(getContainer(), this, command(), getCommandSettings().getAliases());
			} else event.register(getContainer(), this, command());
		}
	}

}
