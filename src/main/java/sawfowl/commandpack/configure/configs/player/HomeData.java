package sawfowl.commandpack.configure.configs.player;

import java.util.Objects;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.api.data.miscellaneous.Location;
import sawfowl.commandpack.api.data.player.Home;
import sawfowl.commandpack.configure.configs.miscellaneous.LocationData;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class HomeData implements Home {

	public HomeData(){}
	public HomeData(String name, LocationData locationData, boolean def) {
		this.name = name;
		this.locationData = locationData;
		this.def = def;
	}

	@Setting("Name")
	private String name;
	@Setting("Location")
	private LocationData locationData;
	@Setting("Default")
	private boolean def;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Component asComponent() {
		return TextUtils.deserialize(name);
	}

	@Override
	public Location getLocation() {
		return locationData;
	}

	@Override
	public boolean isDefault() {
		return def;
	}

	void setDefault() {
		def = true;
	}

	void setBasic() {
		def = false;
	}

	@Override
	public String toString() {
		return "HomeData [Name=" + name + ", Location=" + locationData + ", Default=" + def + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof HomeData)) return false;
		return Objects.equals(name, ((HomeData) obj).name);
	}

}
