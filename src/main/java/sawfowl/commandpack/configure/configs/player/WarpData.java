package sawfowl.commandpack.configure.configs.player;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.Queries;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.api.data.miscellaneous.Location;
import sawfowl.commandpack.api.data.player.Warp;
import sawfowl.commandpack.configure.configs.miscellaneous.LocationData;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class WarpData implements Warp {

	public WarpData(){}
	public WarpData(String name, Location locationData) {
		this.name = name;
		this.locationData = (LocationData) locationData;
	}

	@Setting("Name")
	private String name;
	@Setting("Location")
	private LocationData locationData;
	@Setting("Private")
	private Boolean privated;

	@Override
	public Component asComponent() {
		Component text = TextUtils.deserialize(name);
		return !text.hasStyling() && text.toString().contains("&") ? TextUtils.deserializeLegacy(name) : text;
	}

	public Builder builder() {
		return new Builder();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getPlainName() {
		return TextUtils.clearDecorations(TextUtils.deserialize(name));
	}

	@Override
	public Location getLocation() {
		return locationData;
	}

	@Override
	public boolean isPrivate() {
		return privated != null && privated;
	}

	@Override
	public boolean moveHere(Entity entity) {
		return locationData.moveHere(entity);
	}

	Warp toInterface() {
		return this;
	}

	@Override
	public String toString() {
		return "WarpData [name=" + name + ", locationData=" + locationData + ", privated=" + privated + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if (!(obj instanceof WarpData)) return false;
		return Objects.equals(name, ((WarpData) obj).name);
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
				.set(DataQuery.of("Private"), privated)
				.set(Queries.CONTENT_VERSION, contentVersion());
	}

	public class Builder implements Warp.Builder {

		@Override
		public Builder setName(String name) {
			WarpData.this.name = name;
			return this;
		}

		@Override
		public Builder setLocation(Location location) {
			WarpData.this.locationData = location instanceof LocationData ? (LocationData) location : new LocationData(location);
			return this;
		}

		@Override
		public Builder setPrivate(boolean value) {
			privated = value;
			return this;
		}

		@Override
		public @NotNull Warp build() {
			return WarpData.this;
		}
		
	}

}
