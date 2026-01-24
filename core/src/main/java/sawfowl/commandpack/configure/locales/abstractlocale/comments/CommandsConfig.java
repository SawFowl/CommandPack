package sawfowl.commandpack.configure.locales.abstractlocale.comments;

import sawfowl.commandpack.configure.locales.abstractlocale.comments.commandsconfig.DelayData;
import sawfowl.commandpack.configure.locales.abstractlocale.comments.commandsconfig.Price;

public interface CommandsConfig {

	DelayData getDelayData();

	Price getPrice();

	String getAutoCompleteRaw();

}
