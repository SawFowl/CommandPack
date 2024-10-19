package sawfowl.commandpack.configure.locale.locales.ru.other;

import java.util.Arrays;
import java.util.List;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.Other.ConnectionMessages;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.placeholders.Placeholders.DefaultPlaceholderKeys;

@ConfigSerializable
public class ImplementConnectionMessages implements ConnectionMessages {

	public ImplementConnectionMessages() {}

	@Setting("FirstJoin")
	private Component firstJoin = toText("&7[&2+&7]&r " + DefaultPlaceholderKeys.PLAYER_PREFIX.textKey() + " " + DefaultPlaceholderKeys.ENTITY_DISPLAY_NAME.textKey() + " " + DefaultPlaceholderKeys.PLAYER_SUFFIX.textKey());
	@Setting("Join")
	private Component join = toText("&7[&a+&7]&r " + DefaultPlaceholderKeys.PLAYER_PREFIX.textKey() + " " + DefaultPlaceholderKeys.ENTITY_DISPLAY_NAME.textKey() + " " + DefaultPlaceholderKeys.PLAYER_SUFFIX.textKey());
	@Setting("Leave")
	private Component leave = toText("&7[&c-&7]&r " + DefaultPlaceholderKeys.PLAYER_PREFIX.textKey() + " " + DefaultPlaceholderKeys.ENTITY_DISPLAY_NAME.textKey() + " " + DefaultPlaceholderKeys.PLAYER_SUFFIX.textKey());
	@Setting("Motd")
	private List<Component> motd = Arrays.asList(
			toText("&b================================"),
			toText("&dПриветствуем &e" + DefaultPlaceholderKeys.ENTITY_DISPLAY_NAME.textKey() + "&d. Добро пожаловать на сервер."),
			toText("&dНадеемся вам тут понравится."),
			toText("&b================================")
			);

	@Override
	public Component getFirstJoin(ServerPlayer player) {
		return Text.of(firstJoin).applyPlaceholders(Component.empty(), player).get();
	}

	@Override
	public Component getJoin(ServerPlayer player) {
		return Text.of(join).applyPlaceholders(Component.empty(), player).get();
	}

	@Override
	public Component getLeave(ServerPlayer player) {
		return Text.of(leave).applyPlaceholders(Component.empty(), player).get();
	}

	@Override
	public Component getMotd(ServerPlayer player) {
		return Text.of(Component.join(JoinConfiguration.newlines(), motd)).applyPlaceholders(Component.empty(), player).get();
	}

	private Component toText(String string) {
		return LegacyComponentSerializer.legacyAmpersand().deserialize(string);
	}

}
