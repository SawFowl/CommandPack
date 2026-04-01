package sawfowl.commandpack.apiclasses.economy.storage;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;

import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.apiclasses.economy.CPAccount;
import sawfowl.commandpack.apiclasses.economy.CPUniqueAccount;
import sawfowl.commandpack.apiclasses.economy.EconomyServiceImpl;
import sawfowl.commandpack.configure.configs.economy.SerializedAccount;
import sawfowl.commandpack.configure.configs.economy.SerializedUniqueAccount;
import sawfowl.localeapi.api.ConfigTypes;
import sawfowl.localeapi.api.services.ConfigurationService;

public class FileStorage extends AbstractEconomyStorage {

	private Path playersPath;
	private Path otherPath;
	public FileStorage(CommandPackInstance plugin, EconomyServiceImpl economyService) {
		super(plugin, economyService);
	}

	@Override
	public void load() {
		checkPaths();
		for(File file : playersPath.toFile().listFiles()) if(isValidFile(file)) loadPlayer(file);
		for(File file : otherPath.toFile().listFiles()) if(isValidFile(file)) loadOther(file);
	}

	private void loadPlayer(File file) {
		var account = loadUniqueAccountDataFromFile(file);
		uniqueAccounts.put(account.getUserId(), CPUniqueAccount.deserealize(account, this));
		account = null;
	}

	private void loadOther(File file) {
		var account = loadAccountDataFromFile(file);
		accounts.put(account.getName(), CPAccount.deserealize(account, this));
		account = null;
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
	public void saveUniqueAccount(UniqueAccount account) {
		checkPaths();
		ConfigurationService.getInstance().createReferencedConfig(new SerializedUniqueAccount(account)).setType(ConfigTypes.HOCON).setPath(playersPath).setName(account.uniqueId().toString()).build();
	}

	@Override
	public void saveAccount(Account account) {
		checkPaths();
		ConfigurationService.getInstance().createReferencedConfig(new SerializedAccount(account)).setType(ConfigTypes.HOCON).setPath(otherPath).setName(account.identifier()).build();
	}

	private SerializedUniqueAccount loadUniqueAccountDataFromFile(File file) {
		return ConfigurationService.getInstance().createReferencedConfig(SerializedUniqueAccount.class).fromFile(file).build().get();
	}

	private SerializedAccount loadAccountDataFromFile(File file) {
		return ConfigurationService.getInstance().createReferencedConfig(SerializedAccount.class).fromFile(file).build().get();
	}

	private boolean isValidFile(File file) {
		return ConfigTypes.isValidExtension(ConfigTypes.getExtension(file.getName()));
	}

}
