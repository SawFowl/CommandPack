package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.serverstat;

import net.kyori.adventure.text.Component;

public interface Memory {

	public Component getMax(long value);

	public Component getAllocated(long value);

	public Component getUtilised(long value, long allocated, long max);

	public Component getFree(long value);

}
