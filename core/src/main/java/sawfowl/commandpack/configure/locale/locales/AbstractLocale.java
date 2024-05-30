package sawfowl.commandpack.configure.locale.locales;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import sawfowl.commandpack.configure.locale.locales.abstractclasses.Commands;
import sawfowl.localeapi.api.LocaleReference;

@ConfigSerializable
public abstract class AbstractLocale implements LocaleReference {

	public AbstractLocale(){}

	public abstract Commands getCommands();

}
