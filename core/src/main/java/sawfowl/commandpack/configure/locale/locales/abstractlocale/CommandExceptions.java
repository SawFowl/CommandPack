package sawfowl.commandpack.configure.locale.locales.abstractlocale;

import net.kyori.adventure.text.Component;

public interface CommandExceptions {

	Component getOnlyPlayer();

	Component getPlayerIsOffline(String player);

	Component getUserNotPresent();

	Component getPlayerNotPresent();

	Component getNameNotPresent();

	Component getTypeNotPresent();

	Component getValueNotPresent();

	Component getDurationNotPresent();

	Component getBooleanNotPresent();

	Component getLocationNotPresent();

	Component getWorldNotPresent();

	Component getTargetSelf();

	Component getCooldown(Component delay);

	Component getWait(Component delay);

}
