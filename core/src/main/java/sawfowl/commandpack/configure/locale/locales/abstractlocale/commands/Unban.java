package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.profile.GameProfile;

import net.kyori.adventure.text.Component;

public interface Unban {

	Component getSuccess(GameProfile player);

	Component getAnnouncement(Component source, GameProfile player);

}
