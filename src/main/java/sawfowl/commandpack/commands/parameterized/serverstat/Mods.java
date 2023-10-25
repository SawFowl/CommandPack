package sawfowl.commandpack.commands.parameterized.serverstat;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractInfoCommand;
import sawfowl.commandpack.commands.containerinfo.ModInfo;

public class Mods extends AbstractInfoCommand {

	public Mods(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			delay((ServerPlayer) src, locale, consumer -> {
				sendModsInfo(src, locale, isPlayer);
			});
		} else {
			sendModsInfo(src, locale, isPlayer);
		}
	}

	@Override
	public Parameterized build() {
		return builder()
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
		return null;
	}

	@Override
	public void register(RegisterCommandEvent<Parameterized> event) {
		if(!plugin.isForgeServer()) return;
		if(build() == null) return;
		if(getCommandSettings() == null) {
			event.register(getContainer(), build(), command());
		} else {
			if(!getCommandSettings().isEnable()) return;
			if(getCommandSettings().getAliases() != null && getCommandSettings().getAliases().length > 0) {
				event.register(getContainer(), build(), command(), getCommandSettings().getAliases());
			} else event.register(getContainer(), build(), command());
		}
		CommandPack.getInstance().getPlayersData().getTempData().addTrackingCooldownCommand(this);
	}

}
