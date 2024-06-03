package sawfowl.commandpack.configure.locale.locales.def.commands.world;

import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.world.GameMode;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementGameMode implements GameMode {

	public ImplementGameMode() {}

	@Setting("Survival")
	private Component survival = TextUtils.deserializeLegacy("&aSurvival mode is set in world &e\"" + Placeholders.WORLD + "\"&a.");
	@Setting("Creative")
	private Component creative = TextUtils.deserializeLegacy("&aThe creative mode is set in world &e\"" + Placeholders.WORLD + "\"&a.");
	@Setting("Adventure")
	private Component adventure = TextUtils.deserializeLegacy("&aThe adventure mode is set in world &e\"" + Placeholders.WORLD + "\"&a.");
	@Setting("Spectator")
	private Component spectator = TextUtils.deserializeLegacy("&aSpectator mode is set to world &e\"" + Placeholders.WORLD + "\"&a.");

	@Override
	public Component getSurvival(ServerWorld world) {
		return Text.of(survival).replace(Placeholders.WORLD, world.key().asString()).get();
	}

	@Override
	public Component getCreative(ServerWorld world) {
		return Text.of(creative).replace(Placeholders.WORLD, world.key().asString()).get();
	}

	@Override
	public Component getAdventure(ServerWorld world) {
		return Text.of(adventure).replace(Placeholders.WORLD, world.key().asString()).get();
	}

	@Override
	public Component getSpectator(ServerWorld world) {
		return Text.of(spectator).replace(Placeholders.WORLD, world.key().asString()).get();
	}

}
