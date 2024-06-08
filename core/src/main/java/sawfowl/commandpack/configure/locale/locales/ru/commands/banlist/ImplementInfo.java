package sawfowl.commandpack.configure.locale.locales.ru.commands.banlist;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.banlist.Info;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementInfo implements Info {

	public ImplementInfo() {}

	@Setting("Player")
	private Component playerInfo = TextUtils.deserializeLegacy("&aPlayer&f: &e" + Placeholders.PLAYER + "\n&aSource&f: &e" + Placeholders.SOURCE + "\n&aCreated&f: &e" + Placeholders.CREATED + "\n&aExpire&f: &e" + Placeholders.EXPIRE + "\n&aReason&f: &e" + Placeholders.REASON);
	@Setting("IP")
	private Component ipInfo = TextUtils.deserializeLegacy("&aIP&f: &e" + Placeholders.VALUE + "\n&aSource&f: &e" + Placeholders.SOURCE + "\n&aCreated&f: &e" + Placeholders.CREATED + "\n&aExpire&f: &e" + Placeholders.EXPIRE + "\n&aReason&f: &e" + Placeholders.REASON);

	@Override
	public Component getPlayer(String player, Component source, String created, Component expire, Component reason) {
		return Text.of(playerInfo).replace(Placeholders.PLAYER, player).replace(Placeholders.SOURCE, source).replace(Placeholders.CREATED, created).replace(Placeholders.EXPIRE, expire).replace(Placeholders.REASON, reason).get();
	}

	@Override
	public Component getIP(String ip, Component source, String created, Component expire, Component reason) {
		return Text.of(ipInfo).replace(Placeholders.VALUE, ip).replace(Placeholders.SOURCE, source).replace(Placeholders.CREATED, created).replace(Placeholders.EXPIRE, expire).replace(Placeholders.REASON, reason).get();
	}

}
