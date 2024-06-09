package sawfowl.commandpack.commands.raw.punishments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.data.punishment.Warns;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.localeapi.api.TextUtils;

@Register
public class Warnings extends AbstractRawCommand {

	public Warnings(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		if(args.getInput().length == 0 && !isPlayer) exception(getExceptions(locale).getUserNotPresent());
		Optional<Warns> optWarns = args.get(0);
		String playerName = args.getInput()[0];
		if(!isPlayer) {
			if(optWarns.isPresent()) {
				audience.sendMessage(getWarns(locale).getAllTimeTarget(playerName, optWarns.get().inAllTime()));
				sendWarnsList(audience, locale, optWarns.get(), false, isPlayer);
			} else audience.sendMessage(getWarns(locale).getAllTimeTarget(playerName, 0));
		} else if(optWarns.isPresent() && !optWarns.get().getUniqueId().equals(((ServerPlayer) audience).uniqueId())) {
			audience.sendMessage(getWarns(locale).getAllTimeTarget(optWarns.get().getName(), optWarns.get().inAllTime()));
			sendWarnsList(audience, locale, optWarns.get(), cause.hasPermission(Permissions.WARNS_STAFF), isPlayer);
		} else {
			delay((ServerPlayer) audience, locale, consumer -> {
				if(plugin.getPunishmentService().getWarns((ServerPlayer) audience).isPresent()) {
					sendWarnsList(audience, locale, plugin.getPunishmentService().getWarns((ServerPlayer) audience).get(), cause.hasPermission(Permissions.WARNS_STAFF), isPlayer);
				} else audience.sendMessage(getWarns(locale).getAllTime(0));
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
		return Arrays.asList(RawArguments.createWarnsArgument(true, true, 0, Permissions.WARNS_OTHER, null, null, locale -> getExceptions(locale).getUserNotPresent()));
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

	private void sendWarnsList(Audience audience, Locale locale, Warns warns, boolean remove, boolean isPlayer) {
		if(warns.getWarns().isEmpty()) return;
		List<Component> list = new ArrayList<>();
		warns.getWarns().forEach(warn -> {
			Component removeText = TextUtils.createCallBack(plugin.getLocales().getLocale(locale).getButtons().getRemove(), consumer -> {
				Optional<Warns> find = plugin.getPunishmentService().getWarns(warns.getUniqueId());
				plugin.getPunishmentService().removeWarn(warns.getUniqueId(), warn);
				sendWarnsList(audience, locale, find.get(), remove, isPlayer);
			});
			Component w = isPlayer ? getWarns(locale).getTimes(created(locale, warn), expire(locale, warn)).hoverEvent(HoverEvent.showText(getWarns(locale).getReason(warn.getReason().orElse(text("&e-"))))) : getWarns(locale).getTimes(created(locale, warn), expire(locale, warn)).append(text("  ")).append(getWarns(locale).getReason(warn.getReason().orElse(text("&e-"))));
			list.add(remove ? removeText.append(text("    ")).append(w) : w);
		});
		Component title = getWarns(locale).getTitle(warns.getName());
		sendPaginationList(audience, title, text("=").color(title.color()), 10, list);
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Warns getWarns(Locale locale) {
		return getCommands(locale).getWarns();
	}

	private Component created(Locale locale, sawfowl.commandpack.api.data.punishment.Warn warn) {
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getLocale(locale).getTime().getFormat());
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(warn.getCreated().toEpochMilli());
		return text(format.format(calendar.getTime()));
	}

	private Component expire(Locale locale, sawfowl.commandpack.api.data.punishment.Warn warn) {
		if(!warn.getExpiration().isPresent()) return Component.empty();
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getLocale(locale).getTime().getFormat());
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(warn.getExpiration().get().toEpochMilli());
		return text(format.format(calendar.getTime()));
	}

}
