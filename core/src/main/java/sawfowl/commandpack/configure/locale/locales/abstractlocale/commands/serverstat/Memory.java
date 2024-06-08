package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.serverstat;

import net.kyori.adventure.text.Component;

public interface Memory {

	Component getMax(long value);

	Component getAllocated(long value);

	Component getUtilised(long value, long allocated, long max);

	Component getFree(long value);

}
