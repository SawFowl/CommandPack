package sawfowl.commandpack.apiclasses.economy.storage;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.reference.ConfigurationReference;
import org.spongepowered.configurate.reference.ValueReference;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.apiclasses.economy.CPAccount;
import sawfowl.commandpack.apiclasses.economy.CPUniqueAccount;
import sawfowl.commandpack.apiclasses.economy.EconomyServiceImpl;
import sawfowl.commandpack.configure.configs.economy.SerializedAccount;
import sawfowl.commandpack.configure.configs.economy.SerializedUniqueAccount;

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
			ValueReference<SerializedUniqueAccount, CommentedConfigurationNode> config = configReference.referenceTo(SerializedUniqueAccount.class);
			uniqueAccounts.put(config.get().getUserId(), CPUniqueAccount.deserealize(config.get(), this));
		}
		for(File file : otherPath.toFile().listFiles()) if(file.getName().endsWith(".conf")) {
			ConfigurationReference<CommentedConfigurationNode> configReference = HoconConfigurationLoader.builder().defaultOptions(options).path(file.toPath()).build().loadToReference();
			ValueReference<SerializedAccount, CommentedConfigurationNode> config = configReference.referenceTo(SerializedAccount.class);
			accounts.put(config.get().getName(), CPAccount.deserealize(config.get(), this));
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
			ValueReference<SerializedUniqueAccount, CommentedConfigurationNode> config = configReference.referenceTo(SerializedUniqueAccount.class);
			config.setAndSave(new SerializedUniqueAccount(account));
		} catch (ConfigurateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveAccount(CPAccount account) {
		checkPaths();
		try {
			ConfigurationReference<CommentedConfigurationNode> configReference = HoconConfigurationLoader.builder().defaultOptions(options).path(otherPath.resolve(account.identifier() + ".conf")).build().loadToReference();
			ValueReference<SerializedAccount, CommentedConfigurationNode> config = configReference.referenceTo(SerializedAccount.class);
			config.setAndSave(new SerializedAccount(account));
		} catch (ConfigurateException e) {
			e.printStackTrace();
		}
	}

}
