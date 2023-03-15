package sawfowl.commandpack.commands.abstractcommands.parameterized;

import java.math.BigDecimal;
import java.util.Locale;

import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.Command.Builder;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.economy.Currency;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.commands.parameterized.settings.ParameterSettings;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.configs.commands.CommandPrice;
import sawfowl.commandpack.configure.configs.commands.CommandSettings;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public abstract class AbstractPlayerCommand extends AbstractParameterizedCommand {

	public AbstractPlayerCommand(CommandPack plugin, String command, CommandSettings commandSettings) {
		super(plugin, command, commandSettings);
	}

	public abstract void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException;

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		execute(context, (ServerPlayer) src, locale);
	}

	@Override
	public Builder builder() {
		return parameterSettings.isEmpty() ? 
				Command.builder()
					.executionRequirements(cause -> (
							cause.audience() instanceof ServerPlayer && cause.hasPermission(permission()))
						)
					.executor(this) :
				Command.builder()
					.executionRequirements(cause -> (
							cause.audience() instanceof ServerPlayer && cause.hasPermission(permission()))
						)
					.addParameters(parameterSettings.values().stream().map(ParameterSettings::getParameterUnknownType).toArray(Value[]::new))
					.executor(this);
	}

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

	public CommandException exception(ServerPlayer player, Object... path) throws CommandException {
		return exception(getText(player, path));
	}

	public CommandException exception(ServerPlayer player, String[] keys, String[] values, Object... path) throws CommandException {
		return exception(player.locale(), keys, values, path);
	}

	public CommandException exception(ServerPlayer player, String[] keys, Component[] values, Object... path) throws CommandException {
		return exception(player.locale(), keys, values, path);
	}

}
