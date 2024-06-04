package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.entity.Entity;

import net.kyori.adventure.text.Component;

public interface Glow {

	Component getEnable();

	Component getEnableStaff(Entity target, boolean isPlayer);

	Component getDisable();

	Component getDisableStaff(Entity target, boolean isPlayer);

}
