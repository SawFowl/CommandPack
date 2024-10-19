package sawfowl.commandpack.configure.locale.locales;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.Buttons;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.CommandExceptions;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Commands;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Comments;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Debug;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Other;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Time;
import sawfowl.localeapi.api.LocaleReference;

public interface AbstractLocale extends LocaleReference {

	Buttons getButtons();

	CommandExceptions getCommandExceptions();

	Commands getCommands();

	Comments getComments();

	Debug getDebug();

	Other getOther();

	Time getTime();

}
