package sawfowl.commandpack.configure.configs.miscellaneous;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.spongepowered.api.util.locale.Locales;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class MySqlConfig {

	public MySqlConfig(){}

	@Setting("Host")
	private String host = "localhost";
	@Setting("Port")
	private int port = 3306;
	@Setting("DataBase")
	private String database = "commandpack";
	@Setting("User")
	private String user = "user";
	@Setting("Password")
	private String password = "UNSET";
	@Setting("SSL")
	private String ssl = "false";
	@Setting("DateTimeFormat")
	@Comment("Don't change unnecessarily.")
	private String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
	@Setting("TimeZone")
	@Comment("Set your date time zone.\nAvailable options can be viewed at the link - https://gist.github.com/SawFowl/12dc8342e14bce41f95411f833d911f4")
	private String timeZone = "UTC";

	public boolean isEnable() {
		return !password.equals("UNSET");
	}

	public String getHost() {
		return host;
	}

	public String getPort() {
		return String.valueOf(port);
	}

	public int getPortInt() {
		return port;
	}

	public String getDatabase() {
		return database;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getSSL() {
		return ssl;
	}

	public DateFormat createDateTimeFormat() {
		return new SimpleDateFormat(dateTimeFormat, Locales.DEFAULT);
	}

	public String getDateTimeFormat() {
		return dateTimeFormat;
	}

	public TimeZone getTimeZone() {
		return TimeZone.getTimeZone(timeZone);
	}

}
