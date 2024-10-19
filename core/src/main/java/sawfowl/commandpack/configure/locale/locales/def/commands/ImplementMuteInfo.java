package sawfowl.commandpack.configure.locale.locales.def.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.MuteInfo;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementMuteInfo implements MuteInfo {

	public ImplementMuteInfo() {}

	@Setting("Permanent")
	private Component permanent = TextUtils.deserializeLegacy("&cPermanent");
	@Setting("NotPresent")
	private Component notPresent = TextUtils.deserializeLegacy("&cYou need to specify the nickname of the player with blocked chat.");
	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aSource&f: &e" + Placeholders.SOURCE + "\n&aCreated&f: &e" + Placeholders.CREATED + "\n&aExpire&f: &e" + Placeholders.EXPIRE + "\n&aReason&f: &e" + Placeholders.REASON);
	@Setting("Title")
	private Component title = TextUtils.deserializeLegacy("&3Mute Info " + Placeholders.PLAYER);

	@Override
	public Component getPermanent() {
		return permanent;
	}

	@Override
	public Component getNotPresent() {
		return notPresent;
	}

	@Override
	public Component getSuccess(Component source, String created, Component expire, Component reason) {
		return Text.of(success).replace(Placeholders.SOURCE, source).replace(Placeholders.CREATED, created).replace(Placeholders.EXPIRE, expire).replace(Placeholders.REASON, reason).get();
	}

	@Override
	public Component getTitle(String player) {
		return Text.of(title).replace(Placeholders.PLAYER, player).get();
	}

}
