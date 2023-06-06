package sawfowl.commandpack.commands.parameterized;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.ban.BanService;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Seen extends AbstractParameterizedCommand {

	public Seen(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) src;
			delay(player, locale, consumer -> {
				Sponge.server().userManager().load(getUser(context).orElse(player.name())).thenAccept(user -> {
					Sponge.server().scheduler().executor(getContainer()).execute(() -> {
						sendInfo(src, locale, user.orElse(player.user()), player.hasPermission(Permissions.SEEN_STAFF) || (user.isPresent() && user.get().uniqueId().equals(player.uniqueId())), 10);
					});
				});
			});
		} else {
			Sponge.server().userManager().load(getUser(context).get()).thenAccept(user -> {
				Sponge.server().scheduler().executor(getContainer()).execute(() -> {
					if(user.isPresent()) {
						sendInfo(src, locale, user.get(), true, 30);
					} else src.sendMessage(getText(locale, LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_PRESENT));
				});
			});
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.SEEN;
	}

	@Override
	public String command() {
		return "seen";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createUser(true), false, LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_PRESENT));
	}

	private void sendInfo(Audience audience, Locale locale, User target, boolean full, int size) {
		Optional<ServerPlayer> player = target.player();
		Component displayName = target.get(Keys.DISPLAY_NAME).orElse(text(target.name()));
		Component title = TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_TITLE), Placeholders.PLAYER, displayName);
		Component yes = getText(locale, LocalesPaths.COMMANDS_SEEN_YES);
		Component no = getText(locale, LocalesPaths.COMMANDS_SEEN_NO);
		List<Component> messages = new ArrayList<>();
		boolean online = target.isOnline() && player.isPresent();
		SimpleDateFormat format = new SimpleDateFormat(getString(locale, LocalesPaths.COMMANDS_SERVERSTAT_TIMEFORMAT));
		Calendar calendar = Calendar.getInstance(locale);
		if(online) {
			if(full) {
				messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_ONLINE), Placeholders.PLAYER, target.name()));
				messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_ONLINE_TIME), Placeholders.VALUE, timeFormat(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) - (plugin.getPlayersData().getPlayerData(target.uniqueId()).isPresent() ? plugin.getPlayersData().getPlayerData(target.uniqueId()).get().getLastJoinTime() : TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())), locale)));
			} else {
				calendar.setTimeInMillis(plugin.getPlayersData().getTempData().getVanishEnabledTime(player.get()).isPresent() ? System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(plugin.getPlayersData().getTempData().getVanishEnabledTime(player.get()).get()) : System.currentTimeMillis());
				messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_OFFLINE), Placeholders.PLAYER, target.name()));
				messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_LAST_ONLINE), Placeholders.VALUE, format.format(calendar.getTime())));
			}
		} else {
			messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_OFFLINE), Placeholders.PLAYER, target.name()));
			messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_LAST_ONLINE), Placeholders.VALUE, timeFormat(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) - (plugin.getPlayersData().getPlayerData(target.uniqueId()).isPresent() ? plugin.getPlayersData().getPlayerData(target.uniqueId()).get().getLastExitTime() : TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())), locale)));
		}
		messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_DISPLAY_NAME), Placeholders.VALUE, target.get(Keys.DISPLAY_NAME).orElse(text(target.name()))));
		if(full) {
			messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_UUID), Placeholders.VALUE, target.uniqueId().toString()));
			if(online) messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_IP), Placeholders.VALUE, player.get().connection().address().getHostString()));
		}
		if(target.get(Keys.FIRST_DATE_JOINED).isPresent()) {
			calendar.setTimeInMillis(target.get(Keys.FIRST_DATE_JOINED).get().toEpochMilli());
			messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_FIRST_PLAYED), Placeholders.VALUE, format.format(calendar.getTime())));
		}
		messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_WALKING_SPEED), Placeholders.VALUE, String.valueOf(BigDecimal.valueOf(target.get(Keys.WALKING_SPEED).orElse(0d)).setScale(2, RoundingMode.HALF_UP).doubleValue())));
		messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_FLYING_SPEED), Placeholders.VALUE, String.valueOf(BigDecimal.valueOf(target.get(Keys.FLYING_SPEED).orElse(0d)).setScale(2, RoundingMode.HALF_UP).doubleValue())));
		if(online && full) messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_CURRENT_LOCATION), Placeholders.VALUE, player.get().world().key().asString() + " " + player.get().blockPosition()));
		messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_CAN_FLY), Placeholders.VALUE, target.get(Keys.CAN_FLY).isPresent() && target.get(Keys.CAN_FLY).get() ? yes : no));
		messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_FLYING), Placeholders.VALUE, (target.get(Keys.IS_FLYING).isPresent() && target.get(Keys.IS_FLYING).get()) || (target.get(Keys.IS_ELYTRA_FLYING).isPresent() && target.get(Keys.IS_ELYTRA_FLYING).get()) ? yes : no));
		messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_GAMEMODE), Placeholders.VALUE, target.get(Keys.GAME_MODE).orElse(Sponge.server().gameMode()).asComponent()));
		messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_VANISHED), Placeholders.VALUE, full && online && target.get(Keys.VANISH_STATE).isPresent() && target.get(Keys.VANISH_STATE).get().invisible() ? yes : no));
		messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_GODMODE), Placeholders.VALUE, target.get(Keys.INVULNERABLE).isPresent() && target.get(Keys.INVULNERABLE).get() ? yes : no));
		messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_AFK), Placeholders.VALUE, full && online && plugin.getPlayersData().getTempData().isAfk(player.get()) ? yes : no));
		if(Sponge.serviceProvider().provide(BanService.class).isPresent()) {
			BanService service = Sponge.serviceProvider().provide(BanService.class).get();
			try {
				messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_BAN), Placeholders.VALUE, service.find(target.profile()).get().isPresent() ? yes : no));
			} catch (InterruptedException | ExecutionException e) {
			}
		}
		sendPaginationList(audience, title, getText(locale, LocalesPaths.COMMANDS_SEEN_PADDING), size, messages);
	}

}
