package sawfowl.commandpack.commands.parameterized.serverstat;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractInfoCommand;
import sawfowl.commandpack.commands.containerinfo.PluginInfo;

public class Plugins extends AbstractInfoCommand {

	public Plugins(CommandPack plugin) {
		super(plugin);
		fillLists();
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			delay((ServerPlayer) src, locale, consumer -> {
				sendPluginsInfo(src, locale, isPlayer);
			});
		} else {
			sendPluginsInfo(src, locale, isPlayer);
		}
	}

	@Override
	public Parameterized build() {
		return builder()
				.addChild(new RefreshPlugin(plugin).build(), "refresh", "reload")
				.addChild(new PluginInfo(plugin).build(), "info")
				.build();
	}

	@Override
	public String permission() {
		return Permissions.SERVER_STAT_STAFF_PLUGINS_LIST;
	}

	@Override
	public String command() {
		return "plugins";
	}

	@Override
	public String trackingName() {
		return "serverstat";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

}
