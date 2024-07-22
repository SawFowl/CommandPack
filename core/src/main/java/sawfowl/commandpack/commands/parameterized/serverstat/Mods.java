package sawfowl.commandpack.commands.parameterized.serverstat;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractInfoCommand;
import sawfowl.commandpack.commands.containerinfo.ModInfo;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Mods extends AbstractInfoCommand {

	public Mods(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(getPlayer(context).isPresent()) {
			ServerPlayer target = getPlayer(context).get();
			List<Component> mods = MixinServerPlayer.cast(target).getModList().stream().map(mod -> mod.asComponent()).toList();
			if(mods.isEmpty()) {
				src.sendMessage(plugin.getLocales().getLocale(locale).getCommands().getServerStat().getModsNotFound());
				return;
			}
			Component title = plugin.getLocales().getLocale(locale).getCommands().getServerStat().getPlayerMods(target, mods.size());
			if(isPlayer) {
				delay((ServerPlayer) src, locale, consumer -> sendPaginationList(src, title, Component.text("=").color(NamedTextColor.DARK_AQUA), linesPerPage, mods));
			} else src.sendMessage(title.append(Component.text(": ")).append(Component.join(JoinConfiguration.separators(Component.text(", "), Component.text(".")), mods)));
		} else if(isPlayer) {
			delay((ServerPlayer) src, locale, consumer -> sendModsInfo(src, locale, isPlayer));
		} else sendModsInfo(src, locale, isPlayer);
	}

	@Override
	public Parameterized build() {
		return !plugin.isModifiedServer() ? fastBuild() : builder()
				.addChild(new ModInfo(plugin).build(), "info")
				.build();
	}

	@Override
	public String permission() {
		return Permissions.SERVER_STAT_STAFF_MODS_LIST;
	}

	@Override
	public String command() {
		return "mods";
	}

	@Override
	public String trackingName() {
		return "serverstat";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(Permissions.SERVER_STAT_STAFF_PLAYER_MODS_LIST, plugin.isModifiedServer()), plugin.isModifiedServer(), locale -> getExceptions(locale).getPlayerNotPresent()));
	}

}
