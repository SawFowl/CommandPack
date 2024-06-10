package sawfowl.commandpack.configure.locale.locales.abstractlocale.comments;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.commandsconfig.DelayData;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.commandsconfig.Price;

public interface CommandsConfig {

	DelayData getDelayData();

	Price getPrice();

}
