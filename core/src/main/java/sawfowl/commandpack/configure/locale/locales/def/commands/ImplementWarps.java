package sawfowl.commandpack.configure.locale.locales.def.commands;

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
	private Component empty = TextUtils.deserializeLegacy("&cThe list of warps is empty.");
	@Setting("List")
	private Component list = TextUtils.deserializeLegacy("&eWarps: ");
	@Setting("Wait")
	private Component wait = TextUtils.deserializeLegacy("&eThe list of warps is being compiled. Please wait.");
	@Setting("Header")
	private Component header = TextUtils.deserializeLegacy("&bWarps: ============ " + Placeholders.SERVER + " | " + Placeholders.PLAYER + " ================");
	@Setting("ServerGroup")
	private Component server = TextUtils.deserializeLegacy("&7[&4Server&7]");
	@Setting("PlayerGroup")
	private Component player = TextUtils.deserializeLegacy("&7[&ePlayers&7]");

	@Override
	public Component getEmpty() {
		return empty;
	}

	@Override
	public Component getList() {
		return list;
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
