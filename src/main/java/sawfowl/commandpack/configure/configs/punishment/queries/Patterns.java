package sawfowl.commandpack.configure.configs.punishment.queries;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class Patterns {

	public Patterns(){}

	@Setting("Ban")
	private String ban = "%uuid%><%name%><%source%><%creation%><%expiration%><%reason%";
	@Setting("BanIP")
	private String banIP = "%ip%><%source%><%creation%><%expiration%><%reason%";
	@Setting("CombinetBan")
	private String combinetBan = "%uuid%><%name%><%ip%><%source%><%creation%><%expiration%><%reason%><%ipban%";
	@Setting("Mute")
	private String mute = "%uuid%><%name%><%source%><%creation%><%expiration%><%reason%";

	public String getBan() {
		return ban;
	}

	public String getBanIP() {
		return banIP;
	}

	public String getMute() {
		return mute;
	}

	public String getCombinetBan() {
		return combinetBan;
	}

}
