package sawfowl.commandpack.configure.configs.player;

import java.util.Objects;

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
		return TextUtils.deserialize(name);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Location getLocation() {
		return locationData;
	}

	@Override
	public void setLocation(Location location) {
		this.locationData = (LocationData) location;
	}

	@Override
	public boolean isPrivate() {
		return privated != null && privated;
	}

	@Override
	public Warp setPrivate(boolean value) {
		privated = value;
		return this;
	}

	@Override
	public boolean moveToThis(Entity entity) {
		return locationData.moveToThis(entity);
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

	

}
