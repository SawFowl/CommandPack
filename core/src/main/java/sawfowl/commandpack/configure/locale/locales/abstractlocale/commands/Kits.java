package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import net.kyori.adventure.text.Component;

public interface Kits {

	Component getTitle();

	Component getAlreadyExist();

	Component getSaved(Component name);

	Component getEmpty();

	Component getCooldownIncorectTime();

	Component getCooldownSuccess(Component value);

	Component getSetName();

	Component getGiveRule();

	Component getCreateLore();

	Component getGiveLimit();

	Component getEnableFirstTime();

	Component getDisableFirstTime();

	Component getEnableGiveOnJoin();

	Component getDisableGiveOnJoin();

	Component getEnablePerm();

	Component getDisablePerm();

	Component getAddCommand();

	Component getCommandRemoveFail();

	Component getCommandRemoveSuccess();

	Component getCommandsEmpty();

	Component getCommandsTitle();

	Component getSetPrice();

}
