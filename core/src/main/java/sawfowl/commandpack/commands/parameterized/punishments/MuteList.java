package sawfowl.commandpack.commands.parameterized.punishments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.api.data.punishment.Mute;

@Register
public class MuteList extends AbstractParameterizedCommand {

	public MuteList(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		Collection<Mute> allMutes = plugin.getPunishmentService().getAllMutes();
		if(allMutes.isEmpty()) exception(getMuteList(locale).getEmpty());
		if(!isPlayer) {
			int i = 1;
			for(Mute mute : plugin.getPunishmentService().getAllMutes()) {
				if(i >= 50) return;
				src.sendMessage(text(i + " ➤ " + mute.getName()));
				i++;
			}
		} else {
			Component title = getMuteList(locale).getTitle(allMutes.size());
			List<Component> mutes = new ArrayList<Component>();
			for(Mute mute : allMutes) {
				Component element = getMuteList(locale).getElement(mute.getName());
				if(isPlayer && context.hasPermission(Permissions.MUTEINFO)) element = element.hoverEvent(HoverEvent.showText(getMuteList(locale).getInfo(mute.getName(), mute.getSource().orElse(text("&4Server")), created(locale, mute), expire(locale, mute), mute.getReason().orElse(text("-")))));
				mutes.add(context.hasPermission(Permissions.UNMUTE_STAFF) ? plugin.getLocales().getLocale(locale).getButtons().getRemove().append(element) : element);
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

	private String created(Locale locale, sawfowl.commandpack.api.data.punishment.Mute mute) {
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getLocale(locale).getTime().getFormat());
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(mute.getCreated().toEpochMilli());
		return format.format(calendar.getTime());
	}

	private Component expire(Locale locale, sawfowl.commandpack.api.data.punishment.Mute mute) {
		if(!mute.getExpiration().isPresent()) return plugin.getLocales().getLocale(locale).getCommands().getMuteInfo().getPermanent();
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getLocale(locale).getTime().getFormat());
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(mute.getExpiration().get().toEpochMilli());
		return text(format.format(calendar.getTime()));
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.MuteList getMuteList(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getMuteList();
	}

}
