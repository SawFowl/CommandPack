package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Hat;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementHat implements Hat {

	public ImplementHat(){}

	@Setting("NotPresent")
	private Component notPresent = TextUtils.deserializeLegacy("&cВы должны держать предмет в основной руке.");
	@Setting("Blacklist")
	private Component blackListItem = TextUtils.deserializeLegacy("&cЭтот предмет нельзя надевать на голову.");
	@Setting("FullInventory")
	private Component fullInventory = TextUtils.deserializeLegacy("&cИнвентарь игрока &e" + Placeholders.PLAYER + "&c полон. Нажмите на это сообщение, если хотите заменить предмет на голове игрока. Предмет на голове игрока будет потерян!");
	@Setting("SuccessStaff")
	private Component successStaff = TextUtils.deserializeLegacy("&aВы надели предмет на голову игрока " + Placeholders.PLAYER + "&a.");
	@Setting("SuccessSelf")
	private Component successSelf = TextUtils.deserializeLegacy("&aВы надели предмет на голову.");

	@Override
	public Component getNotPresent() {
		return notPresent;
	}

	@Override
	public Component getBlackListItem() {
		return blackListItem;
	}

	@Override
	public Component getFullInventory(ServerPlayer player) {
		return Text.of(fullInventory).replace(Placeholders.PLAYER, player.name()).get();
	}

	@Override
	public Component getSuccessStaff(ServerPlayer player) {
		return Text.of(successStaff).replace(Placeholders.PLAYER, player.name()).get();
	}

	@Override
	public Component getSuccessSelf() {
		return successSelf;
	}

}
