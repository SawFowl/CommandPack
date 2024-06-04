package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.entity.Entity;

import net.kyori.adventure.text.Component;

public interface Extinguish {

	Component getSuccess();

	Component getSuccessStaff(Entity target, boolean isPlayer);

}
