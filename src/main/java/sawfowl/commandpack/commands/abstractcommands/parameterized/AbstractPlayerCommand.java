package sawfowl.commandpack.commands.abstractcommands.parameterized;

import java.math.BigDecimal;
import java.util.Locale;

import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.Command.Builder;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.economy.Currency;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.configure.LocalesPaths;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.configs.CommandPrice;
import sawfowl.localeapi.api.TextUtils;

public abstract class AbstractPlayerCommand extends AbstractCommand {

	public AbstractPlayerCommand(CommandPack plugin, String command) {
		super(plugin, command);
	}

	public AbstractPlayerCommand(CommandPack plugin, String command, String[] aliases) {
		super(plugin, command, aliases);
	}

	public abstract void execute(CommandContext context, ServerPlayer player, Locale locale) throws CommandException;

	public boolean continueEconomy(ServerPlayer player) {
		if(plugin.getEconomy().isPresent() && !player.hasPermission(Permissions.getIgnorePrice(command))) {
			CommandPrice price = plugin.getCommandsConfig().getCommandConfig(this.command).getPrice();
			if(price.getMoney() > 0) {
				Currency currency = plugin.getEconomy().checkCurrency(price.getCurrency());
				BigDecimal money = createDecimal(price.getMoney());
				if(plugin.getEconomy().checkPlayerBalance(player.uniqueId(), currency, money)) {
					plugin.getEconomy().removeFromPlayerBalance(player, currency, money);
					player.sendMessage(TextUtils.replaceToComponents(getText(player, LocalesPaths.COMMANDS_TAKE_MONEY), new String[] {Placeholders.MONEY, Placeholders.COMMAND}, new Component[] {currency.symbol().append(text(money.toString())), text("/" + command)}));
				} else {
					player.sendMessage(TextUtils.replaceToComponents(getText(player, LocalesPaths.COMMANDS_ERROR_TAKE_MONEY), new String[] {Placeholders.MONEY, Placeholders.COMMAND}, new Component[] {currency.symbol().append(text(money.toString())), text("/" + command)}));
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void execute(CommandContext context, Audience audience, Locale locale) throws CommandException {
		ServerPlayer player = (ServerPlayer) audience;
		execute(context, player, locale);
	}

	@Override
	public Builder builder() {
		return Command.builder()
				.executionRequirements(cause -> (
						cause.audience() instanceof ServerPlayer && cause.hasPermission(permission()))
						)
				.executor(this);
	}

}
