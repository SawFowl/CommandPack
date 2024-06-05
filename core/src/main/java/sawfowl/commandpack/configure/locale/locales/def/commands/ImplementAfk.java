package sawfowl.commandpack.configure.locale.locales.def.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Afk;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementAfk implements Afk {

	public ImplementAfk() {}

	@Setting("EnableBroadcast")
	private Component enableBroadcast = TextUtils.deserializeLegacy("&e" + Placeholders.PLAYER + " &fis now afk.");
	@Setting("DisableBroadcast")
	private Component disableBroadcast = TextUtils.deserializeLegacy("&e" + Placeholders.PLAYER + " &fis no longer afk.");
	@Setting("Title")
	private Component title = TextUtils.deserializeLegacy("&4&lAFK");
	@Setting("Subtitle")
	private Component subtitle = TextUtils.deserializeLegacy("&eYou will be kicked in " + Placeholders.TIME + "&e.");
	@Setting("EnableInVanish")
	private Component enableInVanish = TextUtils.deserializeLegacy("&eYou have gone AFK, but you are currently vanished, so this has not been broadcasted.");
	@Setting("DisableInVanish")
	private Component disableInVanish = TextUtils.deserializeLegacy("&eYou have returned from AFK, but you are currently vanished, so this has not been broadcasted.");
	@Setting("KickBroadcast")
	private Component kickBroadcast = TextUtils.deserializeLegacy("&eYou have been kicked for being AFK for too long.");

	@Override
	public Component getEnableBroadcast(ServerPlayer player) {
		return Text.of(enableBroadcast).replace(Placeholders.PLAYER, player.name()).get();
	}

	@Override
	public Component getDisableBroadcast(ServerPlayer player) {
		return Text.of(disableBroadcast).replace(Placeholders.PLAYER, player.name()).get();
	}

	@Override
	public Component getTitle() {
		return title;
	}

	@Override
	public Component getSubtitle(Component kickAfter) {
		return Text.of(subtitle).replace(Placeholders.TIME, kickAfter).get();
	}

	@Override
	public Component getEnableInVanish() {
		return enableInVanish;
	}

	@Override
	public Component getDisableInVanish() {
		return disableInVanish;
	}

	@Override
	public Component getKick() {
		return kickBroadcast;
	}

}
