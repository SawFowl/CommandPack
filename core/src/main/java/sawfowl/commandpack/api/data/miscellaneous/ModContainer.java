package sawfowl.commandpack.api.data.miscellaneous;

import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import net.kyori.adventure.text.Component;

/**
 * Interface providing information about the mod installed on the server.<br>
 * You cannot access the ForgeAPI for the mod, but you can access the mod file.
 */
public interface ModContainer {

	String getDisplayName();

	String getModId();

	String getDescription();

	Optional<URL> getUpdateURL();

	URL getIssueURL();

	String getVersion();

	List<String> getLoaders();

	Component getDependencies();

	Path getPath();

}
