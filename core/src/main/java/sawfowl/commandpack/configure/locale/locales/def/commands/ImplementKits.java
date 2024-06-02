package sawfowl.commandpack.configure.locale.locales.def.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Kits;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementKits implements Kits {

	public ImplementKits() {}

	@Setting("Title")
	Component title = TextUtils.deserializeLegacy("&3Kits");
	@Setting("AlreadyExist")
	Component alreadyExist = TextUtils.deserializeLegacy("&cA kit with this id already exists.");
	@Setting("Saved")
	Component saved = TextUtils.deserializeLegacy("&aA kit &e" + Placeholders.VALUE + "&a has been saved.");
	@Setting("Empty")
	Component empty = TextUtils.deserializeLegacy("&cThere are no kits created.");
	@Setting("CooldownIncorectTime")
	Component cooldownIncorectTime = TextUtils.deserializeLegacy("&cThe time is not correct. Specify the duration in ISO time format.");
	@Setting("CooldownSuccess")
	Component cooldownSuccess = TextUtils.deserializeLegacy("&aThe time to restore access to the kit &e" + Placeholders.VALUE + "&a is set.");
	@Setting("SetName")
	Component setName = TextUtils.deserializeLegacy("&aThe localized kit name is set.");
	@Setting("GiveRule")
	Component giveRule = TextUtils.deserializeLegacy("&aA rule for the issuance of a kit has been set.");
	@Setting("CreateLore")
	Component createLore = TextUtils.deserializeLegacy("&aA description has been added to the kit for the default localization. To change it, edit the kit configuration file.");
	@Setting("GiveLimit")
	Component giveLimit = TextUtils.deserializeLegacy("&aThe limit for a player to get a kit has been set.");
	@Setting("EnableFirstTime")
	Component enableFirstTime = TextUtils.deserializeLegacy("&aNow the kit will be automatically issued to the player at one time.");
	@Setting("DisableFirstTime")
	Component disableFirstTime = TextUtils.deserializeLegacy("&aThe kit will no longer be automatically given to the player.");
	@Setting("EnableGiveOnJoin")
	Component enableGiveOnJoin = TextUtils.deserializeLegacy("&aThe server will now try to automatically give the player a kit when he login.");
	@Setting("DisableGiveOnJoin")
	Component disableGiveOnJoin = TextUtils.deserializeLegacy("&aThe kit will no longer be automatically given to the player.");
	@Setting("EnablePerm")
	Component enablePerm = TextUtils.deserializeLegacy("&aPermission is now required to obtain a kit.");
	@Setting("DisablePerm")
	Component disablePerm = TextUtils.deserializeLegacy("&aPermission is no longer required to obtain a kit.");
	@Setting("AddCommand")
	Component addCommand = TextUtils.deserializeLegacy("&aA command was added to the kit.");
	@Setting("CommandRemoveFail")
	Component commandRemoveFail = TextUtils.deserializeLegacy("&cThe kit does not contain this command.");
	@Setting("CommandRemoveSuccess")
	Component commandRemoveSuccess = TextUtils.deserializeLegacy("&aCommand deleted.");
	@Setting("CommandsEmpty")
	Component commandsEmpty = TextUtils.deserializeLegacy("&aThe kit does not contain any commands.");
	@Setting("CommandsTitle")
	Component commandsTitle = TextUtils.deserializeLegacy("&3&lCommands");
	@Setting("SetPrice")
	Component setPrice = TextUtils.deserializeLegacy("&aA new price has been set to get a kit.");

	@Override
	public Component getTitle() {
		return title;
	}

	@Override
	public Component getAlreadyExist() {
		return alreadyExist;
	}

	@Override
	public Component getSaved(Component name) {
		return Text.of(saved).replace(Placeholders.VALUE, name).get();
	}

	@Override
	public Component getEmpty() {
		return empty;
	}

	@Override
	public Component getCooldownIncorectTime() {
		return cooldownIncorectTime;
	}

	@Override
	public Component getCooldownSuccess(Component value) {
		return Text.of(cooldownSuccess).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getSetName() {
		return setName;
	}

	@Override
	public Component getGiveRule() {
		return giveRule;
	}

	@Override
	public Component getCreateLore() {
		return createLore;
	}

	@Override
	public Component getGiveLimit() {
		return giveLimit;
	}

	@Override
	public Component getEnableFirstTime() {
		return enableFirstTime;
	}

	@Override
	public Component getDisableFirstTime() {
		return disableFirstTime;
	}

	@Override
	public Component getEnableGiveOnJoin() {
		return enableGiveOnJoin;
	}

	@Override
	public Component getDisableGiveOnJoin() {
		return disableGiveOnJoin;
	}

	@Override
	public Component getEnablePerm() {
		return enablePerm;
	}

	@Override
	public Component getDisablePerm() {
		return disablePerm;
	}

	@Override
	public Component getAddCommand() {
		return addCommand;
	}

	@Override
	public Component getCommandRemoveFail() {
		return commandRemoveFail;
	}

	@Override
	public Component getCommandRemoveSuccess() {
		return commandRemoveSuccess;
	}

	@Override
	public Component getCommandsEmpty() {
		return commandsEmpty;
	}

	@Override
	public Component getCommandsTitle() {
		return commandsTitle;
	}

	@Override
	public Component getSetPrice() {
		return setPrice;
	}

}
