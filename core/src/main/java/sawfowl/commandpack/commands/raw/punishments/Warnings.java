package sawfowl.commandpack.commands.raw.punishments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawCompleterSupplier;
import sawfowl.commandpack.api.commands.raw.arguments.RawResultSupplier;
import sawfowl.commandpack.api.data.punishment.Warns;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

@Register
public class Warnings extends AbstractRawCommand {

	public Warnings(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		if(args.length == 0 && !isPlayer) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_PRESENT);
		Optional<Warns> optWarns = getArgument(Warns.class, args, 0);
		if(!isPlayer) {
			if(optWarns.isPresent()) {
				audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_WARNS_ALLTIME_TARGET).replace(new String[] {Placeholders.PLAYER, Placeholders.VALUE}, args[0], optWarns.get().inAllTime()).get());
				sendWarnsList(audience, locale, optWarns.get(), false, isPlayer);
			} else audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_WARNS_ALLTIME_TARGET).replace(new String[] {Placeholders.PLAYER, Placeholders.VALUE}, args[0], 0).get());
		} else if(optWarns.isPresent() && !optWarns.get().getUniqueId().equals(((ServerPlayer) audience).uniqueId())) {
			audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_WARNS_ALLTIME_TARGET).replace(new String[] {Placeholders.PLAYER, Placeholders.VALUE}, optWarns.get().getName(), optWarns.get().inAllTime()).get());
			sendWarnsList(audience, locale, optWarns.get(), cause.hasPermission(Permissions.WARNS_STAFF), isPlayer);
		} else {
			delay((ServerPlayer) audience, locale, consumer -> {
				if(plugin.getPunishmentService().getWarns((ServerPlayer) audience).isPresent()) {
					sendWarnsList(audience, locale, plugin.getPunishmentService().getWarns((ServerPlayer) audience).get(), cause.hasPermission(Permissions.WARNS_STAFF), isPlayer);
				} else audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_WARNS_ALLTIME).replace(Placeholders.VALUE, 0).get());
			});
		};
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
		return Permissions.WARNS;
	}

	@Override
	public String command() {
		return "warnings";
	}

	@Override
	public Component usage(CommandCause cause) {
		return cause.subject() instanceof ServerPlayer ? text("&c/warnings [Profile]") : text("&c/warnings <Profile>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(RawArgument.of(Warns.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(CommandCause cause, String[] args) {
				return Stream.concat(Sponge.server().userManager().streamAll().map(u -> u.name().orElse(u.examinableName())), plugin.getPunishmentService().getAllWarns().stream().map(w -> w.getName()));
			}
		}, new RawResultSupplier<Warns>() {
			@Override
			public Optional<Warns> get(CommandCause cause, String[] args) {
				Collection<Warns> variants = plugin.getPunishmentService().getAllWarns();
				return args.length == 0 || variants.isEmpty() ? Optional.empty() : variants.stream().filter(w -> w.getName().equals(args[0])).findFirst();
			}
		}, true, true, 0, Permissions.WARNS_OTHER, LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_PRESENT));
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

	private Component timeFormat(Locale locale, long time) {
		SimpleDateFormat format = new SimpleDateFormat(getString(locale, LocalesPaths.COMMANDS_SERVERSTAT_TIMEFORMAT));
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(time);
		return text(format.format(calendar.getTime()));
	}

	private void sendWarnsList(Audience audience, Locale locale, Warns warns, boolean remove, boolean isPlayer) {
		if(warns.getWarns().isEmpty()) return;
		List<Component> list = new ArrayList<>();
		warns.getWarns().forEach(warn -> {
			Component removeText = TextUtils.createCallBack(getComponent(locale, LocalesPaths.REMOVE), consumer -> {
				Optional<Warns> find = plugin.getPunishmentService().getWarns(warns.getUniqueId());
				plugin.getPunishmentService().removeWarn(warns.getUniqueId(), warn);
				sendWarnsList(audience, locale, find.get(), remove, isPlayer);
			});
			Component w = isPlayer ? getText(locale, LocalesPaths.COMMANDS_WARNS_TIMES).replace(new String[] {Placeholders.TIME, Placeholders.LIMIT}, new Component[] {timeFormat(locale, warn.getCreated().toEpochMilli()), warn.getExpiration().map(i -> timeFormat(locale, i.toEpochMilli())).orElse(text("&c∞"))}).get().hoverEvent(HoverEvent.showText(getText(locale, LocalesPaths.COMMANDS_WARNS_REASON).replace(Placeholders.VALUE, warn.getReason().orElse(text("&e-"))).get())) : getText(locale, LocalesPaths.COMMANDS_WARNS_TIMES).replace(new String[] {Placeholders.TIME, Placeholders.LIMIT}, new Component[] {timeFormat(locale, warn.getCreated().toEpochMilli()), warn.getExpiration().map(i -> timeFormat(locale, i.toEpochMilli())).orElse(text("&c∞"))}).get().append(text("  ")).append(getText(locale, LocalesPaths.COMMANDS_WARNS_REASON).replace(Placeholders.VALUE, warn.getReason().orElse(text("&e-"))).get());
			list.add(remove ? removeText.append(text("    ")).append(w) : w);
		});
		Component title = getText(locale, LocalesPaths.COMMANDS_WARNS_TITLE).replace(Placeholders.PLAYER, warns.getName()).get();
		sendPaginationList(audience, title, text("=").color(title.color()), 10, list);
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
