package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Tpa;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementTpa implements Tpa {

	public ImplementTpa() {}

	@Setting("DisabledRequest")
	private Component disabledRequest = TextUtils.deserializeLegacy("&cThis player has disabled receiving teleportation requests.");
	@Setting("Offline")
	private Component offline = TextUtils.deserializeLegacy("&cThe request is not valid. The player is offline.");
	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aYou sent a teleportation request.");
	@Setting("Accepted")
	private Component accepted = TextUtils.deserializeLegacy("&aThe teleportation request is accepted.");
	@Setting("Request")
	private Component request = TextUtils.deserializeLegacy("&e" + Placeholders.PLAYER + "&a is requesting permission to teleport to you. Click this message to accept the request.");
	@Setting("RequestHere")
	private Component requestHere = TextUtils.deserializeLegacy("&e" + Placeholders.PLAYER + "&a asks you to teleport to him/her. Click this message to accept the request.");

	@Override
	public Component getDisabledRequest() {
		return disabledRequest;
	}

	@Override
	public Component getOffline() {
		return offline;
	}

	@Override
	public Component getSuccess() {
		return success;
	}

	@Override
	public Component getAccepted() {
		return accepted;
	}

	@Override
	public Component getRequest(ServerPlayer player) {
		return Text.of(request).replace(Placeholders.PLAYER, player.name()).get();
	}

	@Override
	public Component getRequestHere(ServerPlayer player) {
		return Text.of(requestHere).replace(Placeholders.PLAYER, player.name()).get();
	}

}
