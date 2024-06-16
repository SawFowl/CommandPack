package sawfowl.commandpack.commands.raw.economy;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.commands.raw.arguments.RawBasicArgumentData;
import sawfowl.commandpack.api.commands.raw.arguments.RawOptional;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class HideBalance extends AbstractRawCommand {

	public HideBalance(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		if(isPlayer) {
			Optional<UniqueAccount> optAccount = args.get(0);
			if(optAccount.isPresent()) {
				UniqueAccount account = optAccount.get();
				plugin.getEconomy().getEconomyServiceImpl().hide(account.uniqueId());
				audience.sendMessage(getHideBalance(locale).getResultStaff(plugin.getEconomy().getEconomyServiceImpl().isHiden(account), account.displayName()));
				Sponge.server().player(account.uniqueId()).ifPresent(player -> {
					player.sendMessage(getHideBalance(player).getResult(plugin.getEconomy().getEconomyServiceImpl().isHiden(((ServerPlayer) audience).uniqueId())));
				});
			} else {
				delay((ServerPlayer) audience, locale, consumer -> {
					plugin.getEconomy().getEconomyServiceImpl().hide(((ServerPlayer) audience).uniqueId());
					audience.sendMessage(getHideBalance(locale).getResult(plugin.getEconomy().getEconomyServiceImpl().isHiden(((ServerPlayer) audience).uniqueId())));
				});
			}
		} else {
			UniqueAccount account = args.<UniqueAccount>get(0).get();
			plugin.getEconomy().getEconomyServiceImpl().hide(account.uniqueId());
			audience.sendMessage(getHideBalance(locale).getResultStaff(plugin.getEconomy().getEconomyServiceImpl().isHiden(account), account.displayName()));
			Sponge.server().player(account.uniqueId()).ifPresent(player -> {
				player.sendMessage(getHideBalance(player).getResult(plugin.getEconomy().getEconomyServiceImpl().isHiden(((ServerPlayer) audience).uniqueId())));
			});
		}
	}

	@Override
	public Component shortDescription(Locale locale) {
		return null;
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return null;
	}

	@Override
	public String permission() {
		return Permissions.HIDE_BALANCE;
	}

	@Override
	public String command() {
		return "hidebalance";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text(cause.hasPermission(Permissions.ECONOMY_STAFF) ?  "&c/hidebalance [Player]" : "&c/hidebalance");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(RawArguments.createUniqueAccountArgument(RawBasicArgumentData.createUniqueAccount(0, Permissions.ECONOMY_STAFF, null), new RawOptional(true, false), locale -> getExceptions(locale).getUserNotPresent()));
	}

	@Override
	public void register(RegisterCommandEvent<Raw> event) {
		if(!plugin.getMainConfig().getEconomy().isEnable()) return;
		if(getCommandSettings() == null) {
			event.register(getContainer(), this, command());
		} else {
			if(!getCommandSettings().isEnable()) return;
			if(getCommandSettings().getAliases() != null && getCommandSettings().getAliases().length > 0) {
				event.register(getContainer(), this, command(), getCommandSettings().getAliases());
			} else event.register(getContainer(), this, command());
		}
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.HideBalance getHideBalance(Locale locale) {
		return getCommands(locale).getHideBalance();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.HideBalance getHideBalance(ServerPlayer player) {
		return getHideBalance(player.locale());
	}

}
