package sawfowl.commandpack.utils;

import java.net.URL;
import java.util.Objects;
import net.kyori.adventure.text.Component;

import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.IModInfo.ModVersion;
import sawfowl.localeapi.api.TextUtils;

public class ModContainer {

	private final String name;
	private final String id;
	private final String description;
	private final URL updateURL;
	private final URL IssueURL;
	private final String version;
	private final Component dependencies;
	public ModContainer(ModInfo mod) {
		name = mod.getDisplayName();
		id = mod.getModId();
		description = mod.getDescription();
		updateURL = mod.getUpdateURL();
		IssueURL = mod.getOwningFile().getIssueURL();
		version = mod.getVersion().getMajorVersion() + "." + mod.getVersion().getMinorVersion() + "." + mod.getVersion().getIncrementalVersion() + (mod.getVersion().getBuildNumber() == 0 ? "" : "-" + mod.getVersion().getBuildNumber());
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

	public String getDisplayName() {
		return name;
	}

	public String getModId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public URL getUpdateURL() {
		return updateURL;
	}

	public URL getIssueURL() {
		return IssueURL;
	}

	public String getVersion() {
		return version;
	}

	public Component getDependencies() {
		return dependencies;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getModId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		return Objects.equals(getModId(), ((ModContainer) obj).getModId());
	}

}
