package sawfowl.commandpack.apiclasses.economy.storage;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;

import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.reference.ConfigurationReference;
import org.spongepowered.configurate.reference.ValueReference;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.apiclasses.economy.CPAccount;
import sawfowl.commandpack.apiclasses.economy.CPUniqueAccount;
import sawfowl.commandpack.apiclasses.economy.EconomyServiceImpl;

public class FileStorage extends AbstractEconomyStorage {

	private Path playersPath;
	private Path otherPath;
	public FileStorage(CommandPack plugin, EconomyServiceImpl economyService) {
		super(plugin, economyService);
	}

	@Override
	public void load() throws ConfigurateException {
		checkPaths();
		for(File file : playersPath.toFile().listFiles()) if(file.getName().endsWith(".conf")) {
			ConfigurationReference<CommentedConfigurationNode> configReference = HoconConfigurationLoader.builder().defaultOptions(options).path(file.toPath()).build().loadToReference();
			ValueReference<CPUniqueAccount, CommentedConfigurationNode> config = configReference.referenceTo(CPUniqueAccount.class);
			uniqueAccounts.put(config.get().uniqueId(), config.get().setStorage(this));
		}
		for(File file : otherPath.toFile().listFiles()) if(file.getName().endsWith(".conf")) {
			ConfigurationReference<CommentedConfigurationNode> configReference = HoconConfigurationLoader.builder().defaultOptions(options).path(file.toPath()).build().loadToReference();
			ValueReference<CPAccount, CommentedConfigurationNode> config = configReference.referenceTo(CPAccount.class);
			accounts.put(config.get().identifier(), config.get().setStorage(this));
		}
	}

	private void checkPaths() {
		if(!plugin.getConfigDir().resolve("Modules").toFile().exists()) plugin.getConfigDir().resolve("Modules").toFile().mkdir();
		if(!plugin.getConfigDir().resolve("Modules" + File.separator + "Economy").toFile().exists()) plugin.getConfigDir().resolve("Modules" + File.separator + "Economy").toFile().mkdir();
		playersPath = plugin.getConfigDir().resolve("Modules" + File.separator + "Economy" + File.separator + "Players");
		otherPath = plugin.getConfigDir().resolve("Modules" + File.separator + "Economy" + File.separator + "Other");
		if(!playersPath.toFile().exists()) playersPath.toFile().mkdir();
		if(!otherPath.toFile().exists()) otherPath.toFile().mkdir();
	}

	@Override
	public UniqueAccount createUniqueAccount(UUID uuid) {
		return uniqueAccounts.put(uuid, new CPUniqueAccount(uuid, createDefaultBalances()).setStorage(this));
	}

	@Override
	public Account createAccount(String identifier) {
		return accounts.put(identifier, new CPAccount(identifier, createDefaultBalances()).setStorage(this));
	}

	@Override
	public void removeUniqueAccount(UUID uuid) {
		checkPaths();
		File file = playersPath.resolve(uuid.toString() + ".conf").toFile();
		if(file.exists()) file.delete();
		uniqueAccounts.remove(uuid);
	}

	@Override
	public void removeAccount(String identifier) {
		checkPaths();
		File file = otherPath.resolve(identifier + ".conf").toFile();
		if(file.exists()) file.delete();
		accounts.remove(identifier);
	}

	@Override
	public void saveUniqueAccount(CPUniqueAccount account) {
		checkPaths();
		try {
			ConfigurationReference<CommentedConfigurationNode> configReference = HoconConfigurationLoader.builder().defaultOptions(options).path(playersPath.resolve(account.uniqueId().toString() + ".conf")).build().loadToReference();
			ValueReference<CPUniqueAccount, CommentedConfigurationNode> config = configReference.referenceTo(CPUniqueAccount.class);
			config.setAndSave(account);
		} catch (ConfigurateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveAccount(CPAccount account) {
		checkPaths();
		try {
			ConfigurationReference<CommentedConfigurationNode> configReference = HoconConfigurationLoader.builder().defaultOptions(options).path(otherPath.resolve(account.identifier() + ".conf")).build().loadToReference();
			ValueReference<CPAccount, CommentedConfigurationNode> config = configReference.referenceTo(CPAccount.class);
			config.setAndSave(account);
		} catch (ConfigurateException e) {
			e.printStackTrace();
		}
	}

}
