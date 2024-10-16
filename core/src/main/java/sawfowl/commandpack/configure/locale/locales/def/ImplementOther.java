package sawfowl.commandpack.configure.locale.locales.def;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Other;
import sawfowl.commandpack.configure.locale.locales.def.other.ImplementConnectionMessages;
import sawfowl.commandpack.configure.locale.locales.def.other.ImplementExecuteCommand;
import sawfowl.commandpack.configure.locale.locales.def.other.ImplementKeep;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementOther implements Other {

	public ImplementOther() {}

	@Setting("ConnectionMessages")
	@Comment("These messages use placeholders registered in LocaleAPI.")
	private ImplementConnectionMessages connectionMessages = new ImplementConnectionMessages();
	@Setting("Keep")
	private ImplementKeep keep = new ImplementKeep();
	@Setting("ExecuteCommand")
	private ImplementExecuteCommand executeCommand = new ImplementExecuteCommand();
	@Setting("IllegalClient")
	private Component illegalClient = TextUtils.deserializeLegacy("&cThe server is set to prohibit connection from the client&f:&e " + Placeholders.VALUE + "&c.");
	@Setting("IllegalMods")
	private Component illegalMods = TextUtils.deserializeLegacy("&cThe server has a ban on the use of some of the mods you have&f:\n&e" + Placeholders.VALUE + "&c.");
	@Setting("RequiredMods")
	private Component requiredMods = TextUtils.deserializeLegacy("&cThe following mods are required to log into the server&f:\n&e" + Placeholders.VALUE + "&c.");
	@Setting("BackPack")
	private Component backPack = TextUtils.deserializeLegacy("&6&lBackpack " + Placeholders.PLAYER);

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
	public Component getIllegalClient(String client) {
		return Text.of(illegalClient).replace(Placeholders.VALUE, client).get();
	}

	@Override
	public Component getIllegalMods(boolean blackList, String mods) {
		return Text.of(blackList ? illegalMods : requiredMods).replace(Placeholders.VALUE, mods).get();
	}

	@Override
	public Component getBackPack(String player) {
		return Text.of(backPack).replace(Placeholders.PLAYER, player).get();
	}

}
