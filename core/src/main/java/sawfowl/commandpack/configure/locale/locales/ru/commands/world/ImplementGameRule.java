package sawfowl.commandpack.configure.locale.locales.ru.commands.world;

import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.world.GameRule;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementGameRule implements GameRule {

	public ImplementGameRule() {}

	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aGame rule &e" + Placeholders.RULE + "&a in world &e\"" + Placeholders.WORLD + "\"&a is set to &e" + Placeholders.VALUE + "&a.");
	@Setting("List")
	private Component list = TextUtils.deserializeLegacy("&6Game rules&f: &e" + Placeholders.VALUE + "&a.");
	@Setting("IncorrectValue")
	private Component incorrectValue = TextUtils.deserializeLegacy("&eInvalid value specified for game rule &e" + Placeholders.RULE + "&a.");
	@Setting("UnknownType")
	private Component unknownType = TextUtils.deserializeLegacy("&cFailed to determine the type of game rule &e" + Placeholders.RULE + "&c. No changes have been made.");

	@Override
	public Component getSuccess(String gamerule, ServerWorld world, Object value) {
		return Text.of(success).replace(Placeholders.RULE, gamerule).replace(Placeholders.WORLD, world.key().asString()).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getList(String value) {
		return Text.of(success).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getIncorrectValue(String value) {
		return Text.of(success).replace(Placeholders.RULE, value).get();
	}

	@Override
	public Component getUnknownType(String gamerule) {
		return Text.of(success).replace(Placeholders.RULE, gamerule).get();
	}

}
