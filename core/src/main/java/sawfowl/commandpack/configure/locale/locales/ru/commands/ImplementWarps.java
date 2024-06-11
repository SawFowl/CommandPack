package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Warps;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementWarps implements Warps {

	public ImplementWarps() {}

	@Setting("Empty")
	private Component empty = TextUtils.deserializeLegacy("&cСписок варпов пуст.");
	@Setting("List")
	private Component list = TextUtils.deserializeLegacy("&eВарпы&7(&e"+ Placeholders.SIZE + "&7)&e: " + Placeholders.VALUE);
	@Setting("Wait")
	private Component wait = TextUtils.deserializeLegacy("&eСписок варпов находится в процессе составления. Пожалуйста, подождите.");
	@Setting("Header")
	private Component header = TextUtils.deserializeLegacy("&bВарпы: ============ " + Placeholders.SERVER + " | " + Placeholders.PLAYER + " ================");
	@Setting("ServerGroup")
	private Component server = TextUtils.deserializeLegacy("&7[&4Сервер&7]");
	@Setting("PlayerGroup")
	private Component player = TextUtils.deserializeLegacy("&7[&eИгроки&7]");

	@Override
	public Component getEmpty() {
		return empty;
	}

	@Override
	public Component getList(int size, Component list) {
		return Text.of(this.list).replace(Placeholders.SIZE, size).replace(Placeholders.VALUE, list).get();
	}

	@Override
	public Component getWait() {
		return wait;
	}

	@Override
	public Component getHeader(Component server, Component player) {
		return Text.of(header).replace(Placeholders.SERVER, server).replace(Placeholders.PLAYER, player).get();
	}

	@Override
	public Component getServer() {
		return server;
	}

	@Override
	public Component getPlayer() {
		return player;
	}

}
