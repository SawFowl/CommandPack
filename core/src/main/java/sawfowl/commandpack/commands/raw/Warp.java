package sawfowl.commandpack.commands.raw;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.commands.raw.arguments.RawBasicArgumentData;
import sawfowl.commandpack.api.commands.raw.arguments.RawOptional;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Warp extends AbstractRawCommand {

	public Warp(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		if(args.getInput().length == 0) exception(getExceptions(locale).getWarpNotPresent());
		Optional<ServerPlayer> optTarget = cause.hasPermission(Permissions.WARP_STAFF) && args.getInput().length >= 2 ? getPlayer(args.getInput()[args.getInput().length - 1]) : Optional.empty();
		String[] input = args.getInput();
		if(optTarget.isPresent()) input = Arrays.copyOf(input, input.length - 1);
		String find = "";
		for(String string : input) find = find.isEmpty() ? string : find + " " + string;
		Optional<sawfowl.commandpack.api.data.player.Warp> optWarp = args.get(0);
		if(!optWarp.isPresent()) exception(getWarp(locale).getNotFound());
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) audience;
			if(optTarget.isPresent()) {
				boolean equal = optTarget.get().uniqueId().equals(player.uniqueId());
				if(optWarp.isPresent()) {
					if(!equal) {
						teleport(optWarp.get(), optTarget.get());
						player.sendMessage(getWarp(locale).getSuccessStaff(optTarget.get(), optWarp.get().asComponent()));
						optTarget.get().sendMessage(getWarp(optTarget.get()).getSuccessOther(optWarp.get().asComponent()));
					} else {
						delay(player, locale, consumer -> {
							teleport(optWarp.get(), player);
							player.sendMessage(getWarp(locale).getSuccess(optWarp.get().asComponent()));
						});
					}
				}
			} else {
				delay(player, locale, consumer -> {
					if(optWarp.isPresent()) {
						teleport(optWarp.get(), player);
						player.sendMessage(getWarp(locale).getSuccess(optWarp.get().asComponent()));
					}
				});
			}
		} else {
			if(args.getInput().length == 1 || !optTarget.isPresent()) exception(getExceptions(locale).getPlayerNotPresent());
			if(optWarp.isPresent()) {
				teleport(optWarp.get(), optTarget.get());
				audience.sendMessage(getWarp(locale).getSuccessStaff(optTarget.get(), optWarp.get().asComponent()));
				optTarget.get().sendMessage(getWarp(optTarget.get()).getSuccessOther(optWarp.get().asComponent()));
			}
		}
	}

	@Override
	public Component shortDescription(Locale locale) {
		return text("Warp command.");
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return text("Teleport to a warp point.");
	}

	@Override
	public String permission() {
		return Permissions.WARP;
	}

	@Override
	public Component usage(CommandCause cause) {
		return cause.hasPermission(Permissions.WARP_STAFF) ? text("&c/warp <Warp> [Player]") : text("&c/warp <Warp>");
	}

	@Override
	public String command() {
		return "warp";
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			RawArguments.createWarpArgument(RawBasicArgumentData.createWarp(0, null, null), RawOptional.notOptional(), locale -> getExceptions(locale).getWarpNotPresent()),
			RawArguments.createPlayerArgument(RawBasicArgumentData.createPlayer(1, Permissions.WARP_STAFF, null), RawOptional.player(), locale -> getExceptions(locale).getPlayerNotPresent())
		);
	}

	private void teleport(sawfowl.commandpack.api.data.player.Warp warp, ServerPlayer player) {
		plugin.getPlayersData().getTempData().setPreviousLocation(player);
		warp.moveHere(player);
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Warp getWarp(Locale locale) {
		return getCommands(locale).getWarp();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Warp getWarp(ServerPlayer player) {
		return getWarp(player.locale());
	}

}
