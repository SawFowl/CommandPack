package sawfowl.commandpack.configure.locale.locales.ru.commands;

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
	private Component enableBroadcast = TextUtils.deserializeLegacy("&e" + Placeholders.PLAYER + " &fтеперь АФК.");
	@Setting("DisableBroadcast")
	private Component disableBroadcast = TextUtils.deserializeLegacy("&e" + Placeholders.PLAYER + " &fбольше не АФК.");
	@Setting("Title")
	private Component title = TextUtils.deserializeLegacy("&4&lАФК");
	@Setting("Subtitle")
	private Component subtitle = TextUtils.deserializeLegacy("&eВы будете кикнуты через " + Placeholders.TIME + "&e.");
	@Setting("EnableInVanish")
	private Component enableInVanish = TextUtils.deserializeLegacy("&eВы теперь АФК, но в данный момент вы невидимы, поэтому об этом не было оповещения других игроков.");
	@Setting("DisableInVanish")
	private Component disableInVanish = TextUtils.deserializeLegacy("&eВы теперь не АФК, но в данный момент вы невидимы, поэтому об этом не было оповещения других игроков.");
	@Setting("Kick")
	private Component kick = TextUtils.deserializeLegacy("&eВы были автоматически кикнуты за слишком долгую неактивность.");

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
		return kick;
	}

}
