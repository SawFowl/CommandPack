package sawfowl.commandpack.configure.locale.locales.def.commands;

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
	private Component title = TextUtils.deserializeLegacy("&3Kits");
	@Setting("View")
	private Component view = TextUtils.deserializeLegacy("&7<&b&l⊙&7>&f⏊&7<&b&l⊙&7>");
	@Setting("Empty")
	private Component empty = TextUtils.deserializeLegacy("&cThere are no kits created.");
	@Setting("PermissionRequired")
	private Component permissionRequired = TextUtils.deserializeLegacy("&cYou don't have permission to get this kit.");
	@Setting("Wait")
	private Component wait = TextUtils.deserializeLegacy("&cYou will have to wait &e" + Placeholders.TIME + "&c before you can get this kit.");
	@Setting("InventoryFull")
	private Component inventoryFull = TextUtils.deserializeLegacy("&eThere is not enough free space in your inventory. Are you sure you want this kit? Some of the items will fall to the ground. Click on this message to confirm.");
	@Setting("GiveLimit")
	private Component giveLimit = TextUtils.deserializeLegacy("&cYou have reached the limit for getting this kit.");
	@Setting("NotEnoughMoney")
	private Component notEnoughMoney = TextUtils.deserializeLegacy("&cYou don't have enough money to buy this kit. You need at least  &e" + Placeholders.VALUE + "&c.");
	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aYou received a kit &e" + Placeholders.VALUE + "&a.");
	@Setting("SuccessStaff")
	private Component successStaff = TextUtils.deserializeLegacy("&aYou gave a kit &e" + Placeholders.VALUE + "&a to the player &e" + Placeholders.PLAYER + "&a.");

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
