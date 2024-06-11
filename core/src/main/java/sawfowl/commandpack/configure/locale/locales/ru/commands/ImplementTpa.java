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
	private Component disabledRequest = TextUtils.deserializeLegacy("&cЭтот игрок отключил прием запросов на телепортацию.");
	@Setting("Offline")
	private Component offline = TextUtils.deserializeLegacy("&cЗапрос недействителен. Игрок не в сети.");
	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aВы отправили запрос на телепортацию.");
	@Setting("Accepted")
	private Component accepted = TextUtils.deserializeLegacy("&aЗапрос на телепортацию принят.");
	@Setting("Request")
	private Component request = TextUtils.deserializeLegacy("&e" + Placeholders.PLAYER + "&a запрашивает разрешение на телепортацию к вам. Щелкните это сообщение, чтобы принять запрос.");
	@Setting("RequestHere")
	private Component requestHere = TextUtils.deserializeLegacy("&e" + Placeholders.PLAYER + "&a просит вас телепортироваться к нему(ней). Щелкните это сообщение, чтобы принять запрос.");

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
