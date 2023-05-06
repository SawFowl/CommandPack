package sawfowl.commandpack.utils;

import java.net.URL;

import net.kyori.adventure.text.Component;

import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.IModInfo.ModVersion;

import sawfowl.localeapi.api.TextUtils;

public class ModContainer {

	private final ModInfo mod;
	public ModContainer(ModInfo mod) {
		this.mod = mod;
	}

	public String getDisplayName() {
		return mod.getDisplayName();
	}

	public String getModId() {
		return mod.getModId();
	}

	public String getDescription() {
		return mod.getDescription();
	}

	public URL getUpdateURL() {
		return mod.getUpdateURL();
	}

	public URL getIssueURL() {
		return mod.getOwningFile().getIssueURL();
	}

	public String getVersion() {
		return mod.getVersion().getMajorVersion() + "." + mod.getVersion().getMinorVersion() + "." + mod.getVersion().getIncrementalVersion() + (mod.getVersion().getBuildNumber() == 0 ? "" : "-" + mod.getVersion().getBuildNumber());
	}

	public Component getDependencies() {
		if(mod.getDependencies().isEmpty()) return TextUtils.deserializeLegacy("&e-");
		String dependencies = "";
		int size = mod.getDependencies().size();
		for(ModVersion dependency : mod.getDependencies()) {
			dependencies = dependencies + (size > 1 ? "&e" + dependency.getModId() + (dependency.getVersionRange().getRecommendedVersion() == null ? "&f, " : " (" + dependency.getVersionRange().getRecommendedVersion().getMajorVersion() + "." + dependency.getVersionRange().getRecommendedVersion().getMinorVersion() + "." + dependency.getVersionRange().getRecommendedVersion().getIncrementalVersion() + ")" + "&f, ") : "&e" + dependency.getModId() + (dependency.getVersionRange().getRecommendedVersion() == null ? "&f." : " (" + dependency.getVersionRange().getRecommendedVersion().getMajorVersion() + "." + dependency.getVersionRange().getRecommendedVersion().getMinorVersion() + "." + dependency.getVersionRange().getRecommendedVersion().getIncrementalVersion() + ")" + "&f."));
			size--;
		}
		return TextUtils.deserializeLegacy(dependencies);
	}

}
