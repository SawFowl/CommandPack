package sawfowl.commandpack.configure.locale.locales.def.commands.serverstat;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.serverstat.Memory;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementMemory implements Memory {

	public ImplementMemory() {}

	@Setting("Max")
	private Component max = TextUtils.deserializeLegacy("&aMax(JVM) RAM&f: &e" + Placeholders.VALUE + "Mb");
	@Setting("Allocated")
	private Component allocated = TextUtils.deserializeLegacy("&aAllocated RAM&f: &e" + Placeholders.VALUE + "Mb");
	@Setting("Utilised")
	private Component utilised = TextUtils.deserializeLegacy("&aUtilised RAM&f: &e" + Placeholders.VALUE + "Mb(&6"+ Placeholders.FROM_ALLOCATED + "%&e of used, &6" + Placeholders.FROM_MAX + "%&e of max)");
	@Setting("Free")
	private Component free = TextUtils.deserializeLegacy("&aFree (but allocated) memory&f: &e" + Placeholders.VALUE + "Mb");

	@Override
	public Component getMax(long value) {
		return Text.of(max).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getAllocated(long value) {
		return Text.of(allocated).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getUtilised(long value, long allocated, long max) {
		return Text.of(utilised).replace(Placeholders.VALUE, value).replace(Placeholders.FROM_ALLOCATED, allocated).replace(Placeholders.FROM_MAX, max).get();
	}

	@Override
	public Component getFree(long value) {
		return Text.of(free).replace(Placeholders.VALUE, value).get();
	}

}
