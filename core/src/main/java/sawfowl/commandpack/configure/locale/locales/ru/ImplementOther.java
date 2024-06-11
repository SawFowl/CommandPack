package sawfowl.commandpack.configure.locale.locales.ru;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Other;
import sawfowl.commandpack.configure.locale.locales.ru.other.ImplementConnectionMessages;
import sawfowl.commandpack.configure.locale.locales.ru.other.ImplementExecuteCommand;
import sawfowl.commandpack.configure.locale.locales.ru.other.ImplementKeep;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementOther implements Other {

	public ImplementOther() {}

	@Setting("ConnectionMessages")
	@Comment("Для данных сообщений используются плейсхолдеры зарегистрированные в LocaleAPI")
	private ImplementConnectionMessages connectionMessages = new ImplementConnectionMessages();
	@Setting("Keep")
	private ImplementKeep keep = new ImplementKeep();
	@Setting("ExecuteCommand")
	private ImplementExecuteCommand executeCommand = new ImplementExecuteCommand();
	@Setting("IllegalMods")
	private Component illegalMods = TextUtils.deserializeLegacy("&cНа сервере действует запрет на использование некоторых модов, которые у вас есть&f:\n&c" + Placeholders.VALUE + ".");
	@Setting("BackPack")
	private Component backPack = TextUtils.deserializeLegacy("&6&lРюкзак " + Placeholders.PLAYER);

	@Override
	public ConnectionMessages getConnectionMessages() {
		return connectionMessages;
	}

	@Override
	public Keep getKeep() {
		return keep;
	}

	@Override
	public ExecuteCommand getExecuteCommand() {
		return executeCommand;
	}

	@Override
	public Component getIllegalMods(String mods) {
		return Text.of(illegalMods).replace(Placeholders.VALUE, mods).get();
	}

	@Override
	public Component getBackPack(String player) {
		return Text.of(backPack).replace(Placeholders.PLAYER, player).get();
	}

}
