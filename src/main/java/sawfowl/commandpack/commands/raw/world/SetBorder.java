package sawfowl.commandpack.commands.raw.world;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.math.NumberUtils;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.border.WorldBorder;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class SetBorder extends AbstractWorldCommand {

	public SetBorder(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		ServerPlayer player = (ServerPlayer) audience;
		if(args.length == 0 || !NumberUtils.isParsable(args[0])) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT);
		int radius = NumberUtils.toInt(args[0]);
		WorldBorder border = WorldBorder.builder().center(player.world().properties().spawnPosition().x(), player.world().properties().spawnPosition().z()).targetDiameter(radius).damagePerBlock(player.world().border().damagePerBlock()).safeZone(player.world().border().safeZone()).build();
		player.world().setBorder(border);
		player.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WORLD_SETBORDER), new String[] {Placeholders.WORLD, Placeholders.VALUE, Placeholders.LOCATION}, new Object[] {player.world().key().asString(), radius, player.world().properties().spawnPosition()}));
	}

	@Override
	public List<CommandCompletion> complete(CommandCause cause, List<String> args, Mutable arguments, String currentInput) throws CommandException {
		return getEmptyCompletion();
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
	public String command() {
		return "setborder";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/cworld setborder");
	}

	@Override
	public boolean canExecute(CommandCause cause) {
		return cause.hasPermission(permission()) && cause.audience() instanceof ServerPlayer;
	}

}
