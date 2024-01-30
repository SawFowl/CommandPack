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
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.Ban.Profile;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawCompleterSupplier;
import sawfowl.commandpack.api.commands.raw.arguments.RawResultSupplier;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

@Register
public class Unban extends AbstractRawCommand {

	public Unban(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		GameProfile profile = getArgument(GameProfile.class, args, 0).get();
		plugin.getPunishmentService().pardon(profile);
		Sponge.systemSubject().sendMessage(getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_UNBAN_ANNOUNCEMENT).replace(new String[] {Placeholders.SOURCE, Placeholders.PLAYER}, (isPlayer ? ((ServerPlayer) audience).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) audience).name())) : text("&cServer")), text(profile.name().orElse(profile.examinableName()))).get());
		if(plugin.getMainConfig().getPunishment().getAnnounce().isUnban()) {
			Sponge.server().onlinePlayers().forEach(player -> {
				player.sendMessage(getText(player, LocalesPaths.COMMANDS_UNBAN_ANNOUNCEMENT).replace(new String[] {Placeholders.SOURCE, Placeholders.PLAYER}, (isPlayer ? ((ServerPlayer) audience).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) audience).name())) : text("&cServer")), text(profile.name().orElse(profile.examinableName()))).get());
			});
		} else audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_UNBAN_SUCCESS).replace(Placeholders.PLAYER, profile.name().orElse(profile.examinableName())).get());
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
		return Permissions.UNBAN_STAFF;
	}

	@Override
	public String command() {
		return "unban";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/unban <Profile>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(RawArgument.of(GameProfile.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(CommandCause cause, String[] args) {
				return plugin.getPunishmentService().getAllProfileBans().stream().map(p -> p.profile().name().orElse(p.profile().examinableName()));
			}
		}, new RawResultSupplier<GameProfile>() {
			@Override
			public Optional<GameProfile> get(CommandCause cause, String[] args) {
				Collection<Profile> variants = plugin.getPunishmentService().getAllProfileBans();
				return args.length == 0 || variants.isEmpty() ? Optional.empty() : variants.stream().filter(p -> p.profile().name().orElse(p.profile().examinableName()).equals(args[0])).findFirst().map(Profile::profile);
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

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
