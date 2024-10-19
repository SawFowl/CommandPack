package sawfowl.commandpack.configure.locale.locales.def.commands;

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
	private Component notPresent = TextUtils.deserializeLegacy("&cYou must hold the item in your main hand.");
	@Setting("Blacklist")
	private Component blackListItem = TextUtils.deserializeLegacy("&cThis item cannot be put on your head.");
	@Setting("FullInventory")
	private Component fullInventory = TextUtils.deserializeLegacy("&cThe player's inventory &e" + Placeholders.PLAYER + "&c is full. Click on this message if you want to replace an item on a player's head. The item on the player's head will be lost!");
	@Setting("SuccessStaff")
	private Component successStaff = TextUtils.deserializeLegacy("&aYou put an item on the head of player " + Placeholders.PLAYER + "&a.");
	@Setting("SuccessSelf")
	private Component successSelf = TextUtils.deserializeLegacy("&aYou put the item on your head.");

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
