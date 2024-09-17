package sawfowl.commandpack.configure.configs.player;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.Queries;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.api.data.miscellaneous.Location;
import sawfowl.commandpack.api.data.player.Home;
import sawfowl.commandpack.configure.configs.miscellaneous.LocationData;
import sawfowl.commandpack.configure.configs.miscellaneous.PositionData;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class HomeData implements Home {

	public HomeData(){}
	public HomeData(String name, Location locationData, boolean def) {
		this.name = name;
		this.locationData = (LocationData) locationData;
		this.def = def;
	}

	public Builder builder() {
		return new Builder();
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

	Home toInterface() {
		return this;
	}
	@Override
	public int contentVersion() {
		return 1;
	}
	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew()
				.set(DataQuery.of("Name"), name)
				.set(DataQuery.of("Location"), locationData)
				.set(DataQuery.of("Default"), def)
				.set(Queries.CONTENT_VERSION, contentVersion());
	}

	public class Builder implements Home.Builder {

		@Override
		public Builder setName(String name) {
			HomeData.this.name = name;
			return this;
		}

		@Override
		public Builder setLocation(Location location) {
			HomeData.this.locationData = new LocationData(location.worldKey(), new PositionData(location.getPosition().asVector3d(), location.getPosition().getRotation().map(r -> (r.asVector3d())).orElse(null)));
			return this;
		}

		@Override
		public Builder setDefault(boolean def) {
			HomeData.this.def = def;
			return this;
		}

		@Override
		public @NotNull Home build() {
			return HomeData.this;
		}
		
	}

}
