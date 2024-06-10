package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.CommandSpy;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementCommandSpy implements CommandSpy {

	public ImplementCommandSpy() {}

	@Setting("Enable")
	private Component enable = TextUtils.deserializeLegacy("&aТеперь вы увидите, какие команды используют другие игроки.");
	@Setting("Disable")
	private Component disable = TextUtils.deserializeLegacy("&aВы больше не будете видеть, какие команды используют другие игроки.");
	@Setting("Spy")
	private Component spy = TextUtils.deserializeLegacy("&8[&bCommand&9Spy&8] &e" + Placeholders.PLAYER + "&7 использует команду&f: &d" + Placeholders.COMMAND).clickEvent(ClickEvent.suggestCommand("/tell " + Placeholders.PLAYER));

	@Override
	public Component getEnable() {
		return enable;
	}

	@Override
	public Component getDisable() {
		return disable;
	}

	@Override
	public Component getSpy(ServerPlayer player, String command) {
		return Text.of(spy).replace(Placeholders.PLAYER, player.name()).replace(Placeholders.COMMAND, command).get();
	}

}
