package sawfowl.commandpack.commands.parameterized.punishments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.commandpack.utils.TimeConverter;
import sawfowl.localeapi.api.TextUtils;
import sawfowl.commandpack.api.data.punishment.Mute;

public class MuteList extends AbstractParameterizedCommand {

	public MuteList(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		Collection<Mute> allMutes = plugin.getPunishmentService().getAllMutes();
		if(allMutes.isEmpty()) exception(locale, LocalesPaths.COMMANDS_MUTELIST_EMPTY);
		if(!isPlayer) {
			int i = 1;
			for(Mute mute : plugin.getPunishmentService().getAllMutes()) {
				if(i >= 50) return;
				src.sendMessage(text(i + " ➤ " + mute.getName()));
				i++;
			}
		} else {
			Component title = TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_MUTELIST_TITLE), Placeholders.VALUE, text(allMutes.size()));
			List<Component> mutes = new ArrayList<Component>();
			for(Mute mute : allMutes) {
				Component element = TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_MUTELIST_ELEMENT), Placeholders.VALUE, mute.getName());
				if(isPlayer && context.hasPermission(Permissions.MUTEINFO)) element = element.hoverEvent(HoverEvent.showText(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_MUTELIST_INFO), new String[] {Placeholders.VALUE, Placeholders.SOURCE, Placeholders.CREATED, Placeholders.EXPIRE, Placeholders.REASON}, new Component[] {text(mute.getName()), mute.getSource().orElse(text("n/a")), text(TimeConverter.toString(mute.getCreated())), mute.getExpirationTimeString().map(s -> text(s)).orElse(getText(locale, LocalesPaths.COMMANDS_BANINFO_PERMANENT)), mute.getReason().orElse(text("-"))})));
				mutes.add(context.hasPermission(Permissions.UNMUTE_STAFF) ? getText(locale, LocalesPaths.REMOVE).append(element) : element);
			}
			delay((ServerPlayer) src, locale, consumer -> {
				sendPaginationList(src, title, text("=").color(title.color()), 10, mutes);
			});
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.MUTELIST;
	}

	@Override
	public String command() {
		return "mutelist";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

}
