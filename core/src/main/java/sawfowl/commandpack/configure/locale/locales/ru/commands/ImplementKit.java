package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Kit;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementKit implements Kit {

	public ImplementKit() {}

	@Setting("Title")
	private Component title = TextUtils.deserializeLegacy("&3Наборы");
	@Setting("View")
	private Component view = TextUtils.deserializeLegacy("&7<&b&l⊙&7>&f⏊&7<&b&l⊙&7>");
	@Setting("Empty")
	private Component empty = TextUtils.deserializeLegacy("&cНикаких наборов не создано.");
	@Setting("PermissionRequired")
	private Component permissionRequired = TextUtils.deserializeLegacy("&cУ вас нет разрешения на получение этого набора.");
	@Setting("Wait")
	private Component wait = TextUtils.deserializeLegacy("&cВам нужно подождать &e" + Placeholders.TIME + "&c прежде чем вы сможете получить этот набор.");
	@Setting("InventoryFull")
	private Component inventoryFull = TextUtils.deserializeLegacy("&eВ вашем инвентаре недостаточно свободного места. Вы уверены, что хотите получить этот набор? Некоторые предметы упадут на землю. Нажмите на это сообщение, чтобы подтвердить.");
	@Setting("GiveLimit")
	private Component giveLimit = TextUtils.deserializeLegacy("&cВы достигли лимита получения этого набора.");
	@Setting("NotEnoughMoney")
	private Component notEnoughMoney = TextUtils.deserializeLegacy("&cУ вас недостаточно денег, чтобы купить этот набор. Вам нужно как минимум  &e" + Placeholders.VALUE + "&c.");
	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aВы получили набор &e" + Placeholders.VALUE + "&a.");
	@Setting("SuccessStaff")
	private Component successStaff = TextUtils.deserializeLegacy("&aВы дали набор &e" + Placeholders.VALUE + "&a игроку &e" + Placeholders.PLAYER + "&a.");

	@Override
	public Component getTitle() {
		return title;
	}

	@Override
	public Component getView() {
		return view;
	}

	@Override
	public Component getEmpty() {
		return empty;
	}

	@Override
	public Component getPermissionRequired() {
		return permissionRequired;
	}

	@Override
	public Component getWait(Component time) {
		return Text.of(wait).replace(Placeholders.TIME, time).get();
	}

	@Override
	public Component getInventoryFull() {
		return inventoryFull;
	}

	@Override
	public Component getGiveLimit() {
		return giveLimit;
	}

	@Override
	public Component getNotEnoughMoney(Component price) {
		return Text.of(notEnoughMoney).replace(Placeholders.VALUE, price).get();
	}

	@Override
	public Component getSuccess(Component kitName) {
		return Text.of(success).replace(Placeholders.VALUE, kitName).get();
	}

	@Override
	public Component getSuccessStaff(ServerPlayer player, Component kitName) {
		return Text.of(successStaff).replace(Placeholders.PLAYER, player.name()).replace(Placeholders.VALUE, kitName).get();
	}

}
