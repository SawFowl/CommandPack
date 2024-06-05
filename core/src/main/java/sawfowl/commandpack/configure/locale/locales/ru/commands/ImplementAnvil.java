package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Anvil;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

public class ImplementAnvil implements Anvil {

	public ImplementAnvil() {}

	@Setting("SuccessStaff")
	private Component successStaff = TextUtils.deserializeLegacy("&aYou opened the anvil inventory to player &e" + Placeholders.PLAYER + "&a.");

	@Override
	public Component getSuccessStaff(ServerPlayer player) {
		return Text.of(successStaff).replace(Placeholders.PLAYER, player.name()).get();
	}

}
