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
import org.spongepowered.api.service.ban.Ban.Profile;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawCompleterSupplier;
import sawfowl.commandpack.api.commands.raw.arguments.RawResultSupplier;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.commandpack.utils.TimeConverter;

public class BanInfo extends AbstractRawCommand {

	public BanInfo(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		Profile ban = getArgument(Profile.class, args, 0).get();
		Component title = getText(locale, LocalesPaths.COMMANDS_BANINFO_TITLE).replace(Placeholders.PLAYER, ban.profile().name().orElse(ban.profile().examinableName())).get();
		if(isPlayer) {
			delay((ServerPlayer) audience, locale, consumer -> {
				sendPaginationList(audience, title, text("=").color(title.color()), 10, Arrays.asList(getText(locale, LocalesPaths.COMMANDS_BANINFO_SUCCESS).replace(new String[] {Placeholders.SOURCE, Placeholders.CREATED, Placeholders.EXPIRE, Placeholders.REASON}, ban.banSource().orElse(text("n/a")), text(TimeConverter.toString(ban.creationDate())), ban.expirationDate().map(time -> text(TimeConverter.toString(time))).orElse(text(getText(locale, LocalesPaths.COMMANDS_BANINFO_PERMANENT))), ban.reason().orElse(text("-"))).get()));
			});
		} else sendPaginationList(audience, title, text("=").color(title.color()), 10, Arrays.asList(getText(locale, LocalesPaths.COMMANDS_BANINFO_SUCCESS).replace(new String[] {Placeholders.SOURCE, Placeholders.CREATED, Placeholders.EXPIRE, Placeholders.REASON}, ban.banSource().orElse(text("n/a")), text(TimeConverter.toString(ban.creationDate())), ban.expirationDate().map(time -> text(TimeConverter.toString(time))).orElse(text(getText(locale, LocalesPaths.COMMANDS_BANINFO_PERMANENT))), ban.reason().orElse(text("-"))).get()));
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
		return Permissions.BANINFO;
	}

	@Override
	public String command() {
		return "baninfo";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/baninfo <Profile>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(RawArgument.of(Profile.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(CommandCause cause, String[] args) {
				return plugin.getPunishmentService().getAllProfileBans().stream().map(p -> p.profile().name().orElse(p.profile().examinableName()));
			}
		}, new RawResultSupplier<Profile>() {
			@Override
			public Optional<Profile> get(CommandCause cause, String[] args) {
				Collection<Profile> variants = plugin.getPunishmentService().getAllProfileBans();
				return args.length == 0 || variants.isEmpty() ? Optional.empty() : variants.stream().filter(p -> p.profile().name().orElse(p.profile().examinableName()).equals(args[0])).findFirst();
			}
		}, false, false, 0, LocalesPaths.COMMANDS_BANINFO_NOT_PRESENT));
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
