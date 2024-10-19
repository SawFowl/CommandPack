package sawfowl.commandpack.apiclasses;

import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import net.kyori.adventure.text.Component;

import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.IModInfo.ModVersion;

import sawfowl.commandpack.api.data.miscellaneous.ModContainer;
import sawfowl.localeapi.api.TextUtils;

public class ModContainerImpl implements ModContainer {

	private final String name;
	private final String id;
	private final String description;
	private final Optional<URL> updateURL;
	private final URL IssueURL;
	private final String version;
	private final Component dependencies;
	private final Path path;
	public ModContainerImpl(ModInfo mod) {
		name = mod.getDisplayName();
		id = mod.getModId();
		description = mod.getDescription();
		updateURL = mod.getUpdateURL();
		IssueURL = mod.getOwningFile().getIssueURL();
		version = mod.getVersion().getMajorVersion() + "." + mod.getVersion().getMinorVersion() + "." + mod.getVersion().getIncrementalVersion() + (mod.getVersion().getBuildNumber() == 0 ? "" : "-" + mod.getVersion().getBuildNumber());
		path = mod.getOwningFile().getFile().getFilePath();
		if(mod.getDependencies().isEmpty()) {
			dependencies = TextUtils.deserializeLegacy("&e-");
			return;
		}
		String dependencies = "";
		int size = mod.getDependencies().size();
		for(ModVersion dependency : mod.getDependencies()) {
			dependencies = dependencies + (size > 1 ? "&e" + dependency.getModId() + (dependency.getVersionRange().getRecommendedVersion() == null ? "&f, " : " (" + dependency.getVersionRange().getRecommendedVersion().getMajorVersion() + "." + dependency.getVersionRange().getRecommendedVersion().getMinorVersion() + "." + dependency.getVersionRange().getRecommendedVersion().getIncrementalVersion() + ")" + "&f, ") : "&e" + dependency.getModId() + (dependency.getVersionRange().getRecommendedVersion() == null ? "&f." : " (" + dependency.getVersionRange().getRecommendedVersion().getMajorVersion() + "." + dependency.getVersionRange().getRecommendedVersion().getMinorVersion() + "." + dependency.getVersionRange().getRecommendedVersion().getIncrementalVersion() + ")" + "&f."));
			size--;
		}
		this.dependencies = TextUtils.deserializeLegacy(dependencies);
	}

	@Override
	public String getDisplayName() {
		return name;
	}

	@Override
	public String getModId() {
		return id;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Optional<URL> getUpdateURL() {
		return updateURL;
	}

	@Override
	public URL getIssueURL() {
		return IssueURL;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public Component getDependencies() {
		return dependencies;
	}

	@Override
	public Path getPath() {
		return path;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getModId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		return Objects.equals(getModId(), ((ModContainerImpl) obj).getModId());
	}

}
