package sawfowl.commandpack.configure.locale.locales.ru.comments;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.MainConfig;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Afk;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Economy;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Punishment;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.RandomTeleport;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.RestrictEntitySpawn;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.RestrictMods;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.SpawnData;
import sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig.ImplementAfk;
import sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig.ImplementEconomy;
import sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig.ImplementPunishment;
import sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig.ImplementRandomTeleport;
import sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig.ImplementRestrictEntitySpawn;
import sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig.ImplementRestrictMods;
import sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig.ImplementSpawnData;

@ConfigSerializable
public class ImplementMainConfig implements MainConfig {

	public ImplementMainConfig() {}

	@Setting("Afk")
	private ImplementAfk afk = new ImplementAfk();
	@Setting("Economy")
	private ImplementEconomy economy = new ImplementEconomy();
	@Setting("Punishment")
	private ImplementPunishment punishment = new ImplementPunishment();
	@Setting("RandomTeleport")
	private ImplementRandomTeleport randomTeleport = new ImplementRandomTeleport();
	@Setting("RestrictEntitySpawn")
	private ImplementRestrictEntitySpawn restrictEntitySpawn = new ImplementRestrictEntitySpawn();
	@Setting("RestrictMods")
	private ImplementRestrictMods restrictMods = new ImplementRestrictMods();
	@Setting("SpawnData")
	private ImplementSpawnData spawnData = new ImplementSpawnData();
	@Setting("HideTeleportCommandSource")
	private String hideTeleportCommandSource = "Если значение равно true, игрок не увидит, что кто-то применил к нему команду телепортации с административным разрешением.";
	@Setting("EnableMotd")
	private String enableMotd = "Сообщение игроку при входе в игру будет взято из файлов локализации.\nИгрок должен иметь разрешение: \'" + Permissions.MOTD_ACCESS + "\'.";
	@Setting("ChangeConnectionMessages")
	private String changeConnectionMessages = "Изменение сообщений о входе/выходе игрока.\nСообщения находятся в файлах локализации.";
	@Setting("DebugMods")
	private String debugMods = "Если true, то при подключении игрока к серверу в консоль будет отправлено сообщение со списком модов.\nЭта опция работает только в том случае, если сервер использует Forge.";
	@Setting("DebugPackets")
	private String debugPackets = "Если true, то при подключении игрока в консоли будет показана информация о получаемых пакетах с клиента игрока.";
	@Setting("MySQL")
	private String mySQL = "Выполните настройку соединения, если вам нужно хранить данные о наказаниях игроков и экономике в базе данных MySQL.";
	@Setting("FixTopCommand")
	private String fixTopCommand = "Вместо того чтобы телепортироваться на самую вершину мира, будет сделана попытка найти подходящее место под бедроком.";
	@Setting("PreventDamage")
	private String preventDamage = "Предотвращение нанесения урона другим игрокам, если у игрока есть неуязвимость или невидимость.\nЭти настройки нацелены на эффекты, получаемые игроком от команд плагина.";
	@Setting("ItemSerializer")
	private String itemSerializer = "Выбор варианта сериализации для предметов.\n1 - Все компоненты будут записываться в 1 строку. Этот вариант наиболее надежен, но значительно усложняет ручное редактирование компонентов в конфиге.\n2 - Расширенная запись. Проще вносить изменения в конфиг вручную. Если у вас возникли проблемы с этим типом сериализации, вам следует сообщить об ошибках разработчику плагина LocaleAPI.\n3 - Использование сериализатора Sponge. Некоторые данные будут записаны в 1 строку. Если у вас возникнут проблемы с этим типом сериализации, вам следует сообщить об ошибках разработчикам Sponge.\nОпции 1 и 2 полностью взаимозаменяемы и могут загружать данные, сохраненные опцией 3. Вариант 3 не может загружать данные, сохраненные другими вариантами.";

	@Override
	public Afk getAfk() {
		return afk;
	}

	@Override
	public Economy getEconomy() {
		return economy;
	}

	@Override
	public Punishment getPunishment() {
		return punishment;
	}

	@Override
	public RandomTeleport getRandomTeleport() {
		return randomTeleport;
	}

	@Override
	public RestrictEntitySpawn getRestrictEntitySpawn() {
		return restrictEntitySpawn;
	}

	@Override
	public RestrictMods getRestrictMods() {
		return restrictMods;
	}

	@Override
	public SpawnData getSpawnData() {
		return spawnData;
	}

	@Override
	public String getHideTeleportCommandSource() {
		return hideTeleportCommandSource;
	}

	@Override
	public String getEnableMotd() {
		return enableMotd;
	}

	@Override
	public String getChangeConnectionMessages() {
		return changeConnectionMessages;
	}

	@Override
	public String getDebugMods() {
		return debugMods;
	}

	@Override
	public String getPackets() {
		return null;
	}

	@Override
	public String getMySQL() {
		return mySQL;
	}

	@Override
	public String getFixTopCommand() {
		return fixTopCommand;
	}

	@Override
	public String getPreventDamage() {
		return preventDamage;
	}

	@Override
	public String getItemSerializer() {
		return itemSerializer;
	}

}
