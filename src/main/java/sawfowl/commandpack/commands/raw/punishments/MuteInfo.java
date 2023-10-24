package sawfowl.commandpack.commands.raw.punishments;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
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
import sawfowl.commandpack.utils.TimeConverter;

public class MuteInfo extends AbstractRawCommand {

	public MuteInfo(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		Mute mute = getArgument(Mute.class, args, 0).get();
		Component title = getText(locale, LocalesPaths.COMMANDS_MUTEINFO_TITLE).replace(Placeholders.PLAYER, mute.getName()).get();
		if(isPlayer) {
			delay((ServerPlayer) audience, locale, consumer -> {
				sendPaginationList(audience, title, text("=").color(title.color()), 10, Arrays.asList(getText(locale, LocalesPaths.COMMANDS_MUTEINFO_SUCCESS).replace(new String[] {Placeholders.SOURCE, Placeholders.CREATED, Placeholders.EXPIRE, Placeholders.REASON}, mute.getSource().orElse(text("n/a")), text(TimeConverter.toString(mute.getCreated())), mute.getExpiration().map(time -> text(TimeConverter.toString(time))).orElse(text(getText(locale, LocalesPaths.COMMANDS_MUTEINFO_PERMANENT))), mute.getReason().orElse(text("-"))).get()));
			});
		} else sendPaginationList(audience, title, text("=").color(title.color()), 10, Arrays.asList(getText(locale, LocalesPaths.COMMANDS_MUTEINFO_SUCCESS).replace(new String[] {Placeholders.SOURCE, Placeholders.CREATED, Placeholders.EXPIRE, Placeholders.REASON}, new Component[] {mute.getSource().orElse(text("n/a")), text(TimeConverter.toString(mute.getCreated())), mute.getExpiration().map(time -> text(TimeConverter.toString(time))).orElse(text(getText(locale, LocalesPaths.COMMANDS_MUTEINFO_PERMANENT))), mute.getReason().orElse(text("-"))}).get()));
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
		return Permissions.MUTEINFO;
	}

	@Override
	public String command() {
		return "muteinfo";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/muteinfo <Profile>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(RawArgument.of(Mute.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(CommandCause cause, String[] args) {
				return plugin.getPunishmentService().getAllProfileBans().stream().map(p -> p.profile().name().orElse(p.profile().examinableName()));
			}
		}, new RawResultSupplier<Mute>() {
			@Override
			public Optional<Mute> get(CommandCause cause, String[] args) {
				Collection<Mute> variants = plugin.getPunishmentService().getAllMutes();
				return args.length == 0 || variants.isEmpty() ? Optional.empty() : variants.stream().filter(p -> p.getName().equals(args[0])).findFirst();
			}
		}, false, false, 0, LocalesPaths.COMMANDS_MUTEINFO_NOT_PRESENT));
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
