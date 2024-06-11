package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Sudo;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementSudo implements Sudo {

	public ImplementSudo() {}

	@Setting("CommandNotFound")
	private Component commandNotFound = TextUtils.deserializeLegacy("&cКоманда не найдена.");
	@Setting("CommandNotAllowed")
	private Component commandNotAllowed = TextUtils.deserializeLegacy("&cВы не можете использовать эту команду, или у целевого игрока нет прав на ее выполнение. Принудительное выполнение недоступно.");
	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&eПредпринимается попытка принудительного выполнения команды игроком &6" + Placeholders.PLAYER + "&e.");

	@Override
	public Component getCommandNotFound() {
		return commandNotFound;
	}

	@Override
	public Component getCommandNotAllowed() {
		return commandNotAllowed;
	}

	@Override
	public Component getSuccess(ServerPlayer player) {
		return Text.of(success).replace(Placeholders.PLAYER, player.name()).get();
	}

}
