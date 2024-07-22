package sawfowl.commandpack.configure.locale.locales.abstractlocale;

import java.math.BigDecimal;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.economy.Currency;

import net.kyori.adventure.text.Component;

public interface Other {

	interface ConnectionMessages {

		Component getFirstJoin(ServerPlayer player);

		Component getJoin(ServerPlayer player);

		Component getLeave(ServerPlayer player);

		Component getMotd(ServerPlayer player);

		default Component getJoin(boolean first, ServerPlayer player) {
			return first ? getFirstJoin(player) : getJoin(player);
		}

	}

	interface Keep {

		Component getInventory(double chance);

		Component getExp(double size);

	}

	interface ExecuteCommand {

		Component getOtherCommand(String command);

		Component getMoving(String command);

		Component getNoMoney(Currency currency, BigDecimal money, String command);

		Component getTakeMoney(Currency currency, BigDecimal money, String command);

	}

	ConnectionMessages getConnectionMessages();

	Keep getKeep();

	ExecuteCommand getExecuteCommand();

	Component getIllegalMods(String mods);

	Component getIllegalClient(String mods);

	Component getBackPack(String player);

}
