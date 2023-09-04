package sawfowl.commandpack.commands.raw.punishments;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.service.ban.Ban.IP;

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
import sawfowl.localeapi.api.TextUtils;

public class Unbanip extends AbstractRawCommand {

	public Unbanip(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		InetAddress address = getArgument(InetAddress.class, args, 0).get();
		plugin.getPunishmentService().pardon(address);
		audience.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_UNBANIP_SUCCESS), Placeholders.VALUE, address.getHostAddress()));
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
		return Permissions.UNBANIP_STAFF;
	}

	@Override
	public String command() {
		return "unbanip";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/unbanip <IP>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(RawArgument.of(InetAddress.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(String[] args) {
				return plugin.getPunishmentService().getAllIPBans().stream().map(i -> i.address().getHostAddress());
			}
		}, new RawResultSupplier<InetAddress>() {
			@Override
			public Optional<InetAddress> get(String[] args) {
				Collection<IP> variants = plugin.getPunishmentService().getAllIPBans();
				return args.length == 0 || variants.isEmpty() ? Optional.empty() : variants.stream().filter(i -> i.address().getHostAddress().equals(args[0])).findFirst().map(IP::address);
			}
		}, false, false, 0, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT));
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
