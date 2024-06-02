package sawfowl.commandpack.configure.locale.locales.def.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.GameMode;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementGameMode implements GameMode {

	public ImplementGameMode() {}

	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aYour game mode has been changed to" + Placeholders.VALUE + "&a.");
	@Setting("SuccessStaff")
	private Component successStaff = TextUtils.deserializeLegacy("&aPlayer &e" + Placeholders.PLAYER + " &ais set to play mode " + Placeholders.VALUE + "&a.");
	@Setting("Creative")
	private Component creative = TextUtils.deserializeLegacy("&dcreative");
	@Setting("Spectator")
	private Component spectator = TextUtils.deserializeLegacy("&7spectator");
	@Setting("Survival")
	private Component survival = TextUtils.deserializeLegacy("&2survival");
	@Setting("Adventure")
	private Component adventure = TextUtils.deserializeLegacy("&aadventure");

	@Override
	public Component getSuccess(Component type) {
		return Text.of(success).replace(Placeholders.VALUE, type).get();
	}

	@Override
	public Component getSuccessStaff(ServerPlayer player, Component type) {
		return Text.of(success).replace(Placeholders.PLAYER, player.name()).replace(Placeholders.VALUE, type).get();
	}

	@Override
	public Component getCreative() {
		return creative;
	}

	@Override
	public Component getSpectator() {
		return spectator;
	}

	@Override
	public Component getSurvival() {
		return survival;
	}

	@Override
	public Component getAdventure() {
		return adventure;
	}

}
